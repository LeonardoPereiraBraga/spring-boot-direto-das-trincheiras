package academy.devdojo.user_service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileGetResponse {
    public record UserProfileUser(Long id, String firstName){}
    public record UserProfileProfile(Long id, String name){}

    private Long id;
    private UserProfileUser user;
    private UserProfileProfile profile;

}
