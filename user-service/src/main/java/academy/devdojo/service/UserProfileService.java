package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.domain.UserProfile;
import academy.devdojo.respository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.findAll();
    }

    public List<User> findAllUsersByProfileId(Long id) {
        return repository.findAllUsersByProfileId(id);
    }
}
