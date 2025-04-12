package academy.devdojo.user_service.service;

import academy.devdojo.user_service.commons.ProfileUtils;
import academy.devdojo.user_service.commons.UserProfileUtils;
import academy.devdojo.user_service.commons.UserUtils;
import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.domain.UserProfile;
import academy.devdojo.user_service.repository.ProfileRepository;
import academy.devdojo.user_service.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService service;
    @Mock
    private UserProfileRepository repository;
    private List<UserProfile> userProfileList;
    @InjectMocks
    private UserProfileUtils userProfileUtils;
    @Spy
    private UserUtils userUtils;
    @Spy
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.newProfileList();
    }

    @Test
    @DisplayName("findAll returns a list with all user profiles")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfiles = service.findAll();
        Assertions.assertThat(userProfiles).isNotNull().hasSameElementsAs(userProfileList);
        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("findAllUsersByProfileId returns a list of users for a given profile")
    @Order(2)
    void findAllUsersByProfileId_ReturnsAllUsersForGivenProfile_WhenSuccessful() {
        var profileId = 99L;
        List<User> usersByProfile = this.userProfileList.stream().filter(userProfile -> userProfile.getProfile().getId().equals(profileId))
                .map(UserProfile::getUser).toList();
        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(usersByProfile);
        var users = service.findAllUsersByProfileId(profileId);
        Assertions.assertThat(users).hasSize(1).doesNotContainNull().hasSameElementsAs(usersByProfile);
        users.forEach(u -> Assertions.assertThat(u).hasNoNullFieldsOrProperties());


    }


}