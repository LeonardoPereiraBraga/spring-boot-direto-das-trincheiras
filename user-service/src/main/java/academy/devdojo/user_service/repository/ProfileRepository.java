package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    List<Profile> findByName(String name);
}
