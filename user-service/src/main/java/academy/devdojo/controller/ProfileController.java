package academy.devdojo.controller;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ProfileController {
    private final ProfileService service;
    @Qualifier("profileMapper")
    private final ProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        log.debug("Request received to list all profiles");

        var profiles = service.findAll();

        var profileGetResponses = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(profileGetResponses);
    }


    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {
        log.debug("Request to save profile : {}", request);

        var profile = mapper.toProfile(request);

        var profileSaved = service.save(profile);

        var profilePostResponse = mapper.toProfilePostResponse(profileSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(profilePostResponse);
    }
}
