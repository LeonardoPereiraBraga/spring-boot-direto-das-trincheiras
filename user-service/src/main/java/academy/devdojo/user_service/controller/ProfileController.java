package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.mapper.ProfileMapper;
import academy.devdojo.user_service.request.ProfilePostRequest;
import academy.devdojo.user_service.request.ProfilePostResponse;
import academy.devdojo.user_service.response.ProfileGetResponse;
import academy.devdojo.user_service.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    @Qualifier("profileMapper")
    private final ProfileMapper MAPPER;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll(){
        List<Profile> profiles = service.findAll();
        List<ProfileGetResponse> responseList = MAPPER.toProfileGetResponseList(profiles);
        return ResponseEntity.ok(responseList);
    }
    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request){
        Profile profile = MAPPER.toProfile(request);
        Profile saved = service.save(profile);
        ProfilePostResponse postResponse = MAPPER.toProfilePostResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }
}
