package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.domain.UserProfile;
import academy.devdojo.user_service.mapper.UserMapper;
import academy.devdojo.user_service.mapper.UserProfileMapper;
import academy.devdojo.user_service.request.UserPostRequest;
import academy.devdojo.user_service.request.UserPutRequest;
import academy.devdojo.user_service.response.UserGetResponse;
import academy.devdojo.user_service.response.UserPostResponse;
import academy.devdojo.user_service.response.UserProfileGetResponse;
import academy.devdojo.user_service.response.UserProfileUserGetResponse;
import academy.devdojo.user_service.service.UserProfileService;
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
@RequestMapping("v1/user-profiles") //boa pratica usar -
public class UserProfileController {
    private final UserProfileService service;

    @Qualifier("userProfileMapper")
    private final UserProfileMapper MAPPER;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll(){
        List<UserProfile> userProfiles = service.findAll();
        List<UserProfileGetResponse> response = MAPPER.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(response);
    }
    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAll(@PathVariable Long id){
        List<User> users = service.findAllUsersByProfileId(id);
        List<UserProfileUserGetResponse> response = MAPPER.toUserProfileUserGetResponse(users);
        return ResponseEntity.ok(response);
    }

}
