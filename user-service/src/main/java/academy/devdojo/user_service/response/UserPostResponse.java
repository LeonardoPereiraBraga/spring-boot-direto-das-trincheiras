package academy.devdojo.user_service.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPostResponse {
    private Long id;
}
