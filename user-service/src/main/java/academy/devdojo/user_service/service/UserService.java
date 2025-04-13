package academy.devdojo.user_service.service;

import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String name){
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }
    public User findByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found"));
    }
    public User save(User user){
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }
    public void delete(Long id){
        User userFounded = findByIdOrThrow(id);
        repository.delete(userFounded);
    }
    public void update(User user){
        User userToUpdate = findByIdOrThrow(user.getId());
        assertEmailDoesNotExist(user.getEmail(), user.getId());
        repository.save(userToUpdate);
    }
    public void assertEmailDoesNotExist(String email){
        repository.findByEmail(email).ifPresent(user -> {throw new EmailAlreadyExistsException("Email already exists");
        });
    }
    public void assertEmailDoesNotExist(String email, Long id){
        repository.findByEmailAndIdNot(email,id).ifPresent(user -> {throw new EmailAlreadyExistsException("Email already exists");
        });
    }
}
