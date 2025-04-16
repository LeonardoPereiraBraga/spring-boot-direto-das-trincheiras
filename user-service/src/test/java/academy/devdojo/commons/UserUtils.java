package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        var toyohisa = User.builder()
                .id(1L)
                .firstName("Toyohisa")
                .lastName("Shimazu")
                .email("toyohisa@drifters.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
        var ichigo = User.builder()
                .id(2L)
                .firstName("Ichigo")
                .lastName("Kurosaki")
                .email("ichigo@bleach.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
        var ash = User.builder()
                .id(3L)
                .firstName("Ash")
                .lastName("Ketchum")
                .email("ash@pokemon.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();

        return new ArrayList<>(List.of(toyohisa, ichigo, ash));
    }

    public User newUserToSave() {
        return User.builder()
                .firstName("Yusuke")
                .lastName("Urameshi")
                .email("yusuke@yuyuhakusho.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
    }

    public User newUserSaved() {
        return User.builder()
                .id(99L)
                .firstName("Yusuke")
                .lastName("Urameshi")
                .email("yusuke@yuyuhakusho.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
    }
}
