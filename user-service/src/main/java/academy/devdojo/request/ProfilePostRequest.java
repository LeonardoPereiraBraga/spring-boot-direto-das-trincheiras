package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProfilePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
    @NotBlank(message = "The field 'description' is required")
    private String description;
}
