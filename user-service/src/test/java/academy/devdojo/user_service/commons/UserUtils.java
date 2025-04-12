package academy.devdojo.user_service.commons;


import academy.devdojo.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        var toyohisa = User.builder().id(1L).firstName("Toyohisa").lastName("Shimazu").email("toyohisa@drifters.com").build();
        var ichigo = User.builder().id(2L).firstName("Ichigo").lastName("Kurosaki").email("ichigo@bleach.com").build();
        var ash = User.builder().id(3L).firstName("Ash").lastName("Ketchum").email("ash@pokemon.com").build();

        return new ArrayList<>(List.of(toyohisa, ichigo, ash));
    }

    public User newUserToSave() {
        return User.builder()
                .id(99L)
                .firstName("Yusuke")
                .lastName("Urameshi")
                .email("yusuke@yuyuhakusho.com")
                .build();
    }
    public User newUserSaved() {
        return User.builder()
                .id(99L)
                .firstName("Yusuke")
                .lastName("Urameshi")
                .email("yusuke@yuyuhakusho.com")
                .build();
    }
}
