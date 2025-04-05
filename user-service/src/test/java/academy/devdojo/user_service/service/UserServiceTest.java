package academy.devdojo.user_service.service;

import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    private final List<User> userList = new ArrayList<>();

    @BeforeEach
    void init(){
        User user1 = User.builder().id(1L).firstName("Joao").lastName("Cardoso").email("joao@gmail.com").build();
        User user2 = User.builder().id(2L).firstName("Maria").lastName("Pedroza").email("maria@gmail.com").build();
        User user3 = User.builder().id(3L).firstName("Alan").lastName("Ferro").email("alan@gmail.com").build();
        userList.addAll(List.of(user1,user2,user3));
    }

    @Test
    @DisplayName("findAll return list of all users when null is given")
    void findAll_ReturnsAllUsers_WhenSuccessful(){
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        List<User> allUsers = service.findAll(null);
        Assertions.assertThat(allUsers).isEqualTo(userList).isNotEmpty();
    }
    @Test
    @DisplayName("findAll return list of all users when name is given")
    void findAll_ReturnsTheUserWithTheNameGiven_WhenSuccessful(){
        User userToFound = userList.getFirst();
        BDDMockito.when(repository.findByFirstNameIgnoreCase(userToFound.getFirstName())).thenReturn(List.of(userToFound));
        List<User> userFounded = service.findAll(userToFound.getFirstName());
        Assertions.assertThat(userFounded).contains(userToFound).isNotEmpty();
    }
    @Test
    @DisplayName("findAll returns empty list when firstName is not found")
    void findByName_ReturnsEmptyList_WhenFirstNameIsNotFound() {
        var firstName = "not-found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(emptyList());

        var users = service.findAll(firstName);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("findById return one user when successful")
    void findById_ReturnsUser_WhenSuccessful(){
        User user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        User userFounded = service.findByIdOrThrow(user.getId());
        Assertions.assertThat(userFounded).isEqualTo(user).isNotNull();

    }
    @Test
    @DisplayName("findById throws ResponseException When the User is not Found")
    void findByName_ReturnsUserWithTheName_WhenSuccessful(){
        User user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.findByIdOrThrow(user.getId())).isInstanceOf(ResponseStatusException.class);
    }
    @Test
    @DisplayName("save creates the user")
    void save_CreatesTheUser_WhenSuccessful(){
        User userToSave = User.builder().id(5L).firstName("Caze").lastName("Dess").email("caze@gmail.com").build();
        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());
        User userSaved = service.save(userToSave);
        Assertions.assertThat(userSaved).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete deletes a user")
    void delete_DeletesTheUser_WhenSuccessful () {
        User userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);
        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }
    @Test
    @DisplayName("delete throws ResponseException when user is not found")
    void delete_ThrowsResponseException_WhenUserIsNotFound() {
        User userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.delete(userToDelete.getId()));
    }
    @Test
    @DisplayName("update updates a user")
    void update_UpdatesTheUser_WhenSuccessful(){
        User userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("UsuarioAtualizado");
        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);
        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }
    @Test
    @DisplayName("update throws EmailAlreadyExistsException when email already exist")
    void update_ThrowEmailAlreadyExistsException_WhenAlreadyExists(){
        User userToUpdate = userList.getFirst();
        User savedUser = userList.getLast();
        userToUpdate.setEmail(savedUser.getEmail());
        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), userToUpdate.getId())).thenReturn(Optional.of(savedUser));
        Assertions.assertThatException().isThrownBy(() -> service.update(userToUpdate)).isInstanceOf(EmailAlreadyExistsException.class);
    }
    @Test
    @DisplayName("save throws EmailAlreadyExistsException when email already exist")
    void save_ThrowEmailAlreadyExistsException_WhenAlreadyExists(){
        User userToUpdate = userList.getFirst();
        User savedUser = userList.getLast();
        userToUpdate.setEmail(savedUser.getEmail());
        BDDMockito.when(repository.findByEmail(userToUpdate.getEmail())).thenReturn(Optional.of(savedUser));
        Assertions.assertThatException().isThrownBy(() -> service.save(userToUpdate)).isInstanceOf(EmailAlreadyExistsException.class);
    }
    @Test
    @DisplayName("update throws ResponseException when user is not found")
    void update_ThrowsResponseException_WhenUserIsNotFound(){
        User userToUpdate = userList.getFirst();
        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());
        userToUpdate.setFirstName("UsuarioAtualizado");
        Assertions.assertThatThrownBy(() -> service.update(userToUpdate));
    }


}