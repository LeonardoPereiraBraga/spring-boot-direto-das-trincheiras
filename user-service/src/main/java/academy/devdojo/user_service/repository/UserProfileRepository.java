package academy.devdojo.user_service.repository;

import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    //query mais performatica
    @Override
    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
    List<UserProfile> findAll();

    @Query("SELECT up.user from UserProfile up where up.profile.id = ?1")
    List<User> findAllUsersByProfileId(Long id);
}
