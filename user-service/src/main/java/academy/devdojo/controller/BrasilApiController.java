package academy.devdojo.controller;

import academy.devdojo.config.BrasilApiConfigurationProperties;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;
    @GetMapping()
    public ResponseEntity<Void> csrfToken() {
        return ResponseEntity.noContent().build();
    }
}
