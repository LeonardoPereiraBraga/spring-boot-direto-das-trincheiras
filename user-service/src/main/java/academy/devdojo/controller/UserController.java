package academy.devdojo.controller;

import academy.devdojo.exception.ApiError;
import academy.devdojo.exception.DefaultErrorMessage;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User API", description = "User related endpoints")
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserService service;
    @Qualifier("userMapper")
    private final UserMapper mapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users available in the system",
    responses = {
            @ApiResponse(description = "List all users",
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class)))
            )
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        log.debug("Request received to list all users, param first name '{}'", firstName);

        var users = service.findAll(firstName);

        var userGetResponses = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponses);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get user by id",
            responses = {
                    @ApiResponse(description = "Get user by its id",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGetResponse.class))
                    ),
                    @ApiResponse(description = "User Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            })
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find user by id: {}", id);

        var user = service.findByIdOrThrowNotFound(id);

        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user",
            responses = {
                    @ApiResponse(description = "Save user in the database",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserPostResponse.class))
                    ),
                    @ApiResponse(description = "Bad Request",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))
                    )
            })
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest request) {
        log.debug("Request to save user : {}", request);

        var user = mapper.toUser(request);

        var userSaved = service.save(user);

        var userPostResponse = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete user by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
        log.debug("Request to update user {}", request);

        var userToUpdate = mapper.toUser(request);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }
}
