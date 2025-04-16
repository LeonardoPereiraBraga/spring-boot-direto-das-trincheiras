package academy.devdojo.controller;

import academy.devdojo.mapper.UserProfileMapper;
import academy.devdojo.response.UserProfileGetResponse;
import academy.devdojo.response.UserProfileUserGetResponse;
import academy.devdojo.service.UserProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserProfileController {
    private final UserProfileService service;
    @Qualifier("userProfileMapper")
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        log.debug("Request received to list all user profiles");

        var userProfiles = service.findAll();
        var userProfileGetResponses = mapper.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(userProfileGetResponses);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAll(@PathVariable Long id) {
        log.debug("Request received to list all users by profile id '{}'", id);
        var users = service.findAllUsersByProfileId(id);
        var userProfileUserGetResponseList = mapper.toUserProfileUserGetResponseList(users);
        return ResponseEntity.ok(userProfileUserGetResponseList);
    }
}
