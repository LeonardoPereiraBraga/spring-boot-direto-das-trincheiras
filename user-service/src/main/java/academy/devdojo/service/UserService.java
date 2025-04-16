package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    //    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        var user = findByIdOrThrowNotFound(id);
        repository.delete(user);
    }

    public void update(User userToUpdate) {
        var savedUser = findByIdOrThrowNotFound(userToUpdate.getId());
        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());

        var userWithPasswordAndRoles = mapper.toUserWithPasswordAndRoles(userToUpdate, userToUpdate.getPassword(), savedUser);

        repository.save(userWithPasswordAndRoles);
    }

    public void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailExistsException);
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(user.getEmail()));
    }
}
