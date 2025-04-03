package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
public class ProducerPutRequest {
    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "The field 'name' is required")
    private String name;

}
