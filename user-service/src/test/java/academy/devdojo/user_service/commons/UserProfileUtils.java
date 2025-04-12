package academy.devdojo.user_service.commons;

import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {
    private final UserUtils userUtils;
    private final ProfileUtils profileUtils;

    public List<UserProfile> newProfileList() {
        var regularUserProfile = newUserProfileSaved();

        return new ArrayList<>(List.of(regularUserProfile));
    }

    public UserProfile newUserProfileToSave() {
        return UserProfile.builder()
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }

    public UserProfile newUserProfileSaved() {
        return UserProfile.builder()
                .id(1L)
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }
}
