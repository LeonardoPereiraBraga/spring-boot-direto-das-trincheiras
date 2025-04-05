package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.mapper.UserMapper;
import academy.devdojo.user_service.request.UserPostRequest;
import academy.devdojo.user_service.request.UserPutRequest;
import academy.devdojo.user_service.response.UserGetResponse;
import academy.devdojo.user_service.response.UserPostResponse;
import academy.devdojo.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users")
public class UserController {
    private final UserService service;

    @Qualifier("userMapper")
    private final UserMapper MAPPER;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName){
        List<User> userList = service.findAll(firstName);
        List<UserGetResponse> getResponseList = MAPPER.toUserGetResponseList(userList);
        return ResponseEntity.ok(getResponseList);
    }
    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id){
        User user = service.findByIdOrThrow(id);
        UserGetResponse response = MAPPER.toUserGetResponse(user);
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest){
        User user = MAPPER.toUser(userPostRequest);
        User userSaved = service.save(user);
        System.out.println(userSaved.getId());
        UserPostResponse response = MAPPER.toUserPostResponse(userSaved);
        //response.setId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {

        var userToUpdate = MAPPER.toUser(request);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

}
