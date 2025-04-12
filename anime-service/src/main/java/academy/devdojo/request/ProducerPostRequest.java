package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
