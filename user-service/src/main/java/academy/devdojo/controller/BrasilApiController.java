package academy.devdojo.controller;

import academy.devdojo.config.BrasilApiConfigurationProperties;
import academy.devdojo.response.CepGetResponse;
import academy.devdojo.service.BrasilApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {
    private final BrasilApiService service;
    @GetMapping("/{cep}")
    public ResponseEntity<CepGetResponse> csrfToken(@PathVariable String cep) {
        CepGetResponse cepGetResponse = service.findCep(cep);
        return ResponseEntity.ok(cepGetResponse);
    }
}
