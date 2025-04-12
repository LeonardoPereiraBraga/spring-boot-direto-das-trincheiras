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
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository repository;


    @Test
    @DisplayName("findAll return list with all users by profile id")
    @Sql("/sql/init_user_profile_2_users_1_profile.sql")
    void findAllUsersByProfileId_ReturnsAllUsers_WhenSuccessful(){
        Long profileId = 1L;
        var allUsers = repository.findAllUsersByProfileId(profileId);
        Assertions.assertThat(allUsers).isNotEmpty();
    }


}