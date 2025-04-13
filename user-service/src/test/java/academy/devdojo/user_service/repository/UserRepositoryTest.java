package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.config.TestcontainersConfiguration;
import academy.devdojo.user_service.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("itest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import(TestcontainersConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("save creates the user")
    void save_CreatesTheUser_WhenSuccessful(){
        User userToSave = User.builder().firstName("Caze").lastName("TV").email("caze@gmail.com").build();
        User userSaved = repository.save(userToSave);
        Assertions.assertThat(userSaved).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }
    @Test
    @DisplayName("findAll return list of all users when null is given")
    @Sql("/sql/user/init_one_user.sql")
    void findAll_ReturnsAllUsers_WhenSuccessful(){
        List<User> allUsers = repository.findAll();
        Assertions.assertThat(allUsers).isNotEmpty();
    }


}