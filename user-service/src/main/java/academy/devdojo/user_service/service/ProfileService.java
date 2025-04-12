package academy.devdojo.user_service.service;

import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll(){
        return repository.findAll();
    }

    public Profile findByIdorThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Profile not found"));
    }
    public Profile save(Profile profile){
        return repository.save(profile);
    }

}
