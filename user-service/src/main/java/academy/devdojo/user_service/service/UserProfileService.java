package academy.devdojo.user_service.service;

import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.domain.UserProfile;
import academy.devdojo.user_service.repository.ProfileRepository;
import academy.devdojo.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll(){
        return repository.findAll();
    }


    public List<User> findAllUsersByProfileId(Long id) {
        return repository.findAllUsersByProfileId(id);
    }
}
