package academy.devdojo.user_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@With
@NamedEntityGraph(name = "UserProfile.fullDetails",
        attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("profile")})
public class UserProfile {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Profile profile;
}
