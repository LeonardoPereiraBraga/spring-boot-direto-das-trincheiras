package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.commons.FileUtils;
import academy.devdojo.user_service.commons.ProfileUtils;
import academy.devdojo.user_service.config.TestcontainersConfiguration;
import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.repository.ProfileRepository;
import academy.devdojo.user_service.request.ProfilePostResponse;
import academy.devdojo.user_service.response.ProfileGetResponse;
import net.javacrumbs.jsonunit.assertj.JsonAssertion;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileControllerIt {
    private static final String URL = "/v1/profiles";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProfileUtils profileUtils;
    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //antes do teste executa esse sql
    @Sql(value = "/sql/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>(){}; //Tipo retornado
        ResponseEntity<List<ProfileGetResponse>> responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();
        responseEntity.getBody()
                .forEach(profileGetResponse ->
                        Assertions.assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }
    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    void findAll_ReturnsEmptyList_WhenIsNotFound() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>(){}; //Tipo retornado
        ResponseEntity<List<ProfileGetResponse>> responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("POST v1/profiles creates an profile")
    void save_CreatesProfile_WhenSuccessful() throws Exception{
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> profileJson = new HttpEntity<>(request,httpHeaders);

        ResponseEntity<ProfilePostResponse> responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, profileJson, ProfilePostResponse.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }
    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        var response = fileUtils.readResourceFile("profile/%s".formatted(responseFile));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> profileJson = new HttpEntity<>(request,httpHeaders);
        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, profileJson, String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity).isNotNull();
        System.out.println(responseEntity.getBody());
        JsonAssertions.assertThatJson(responseEntity.getBody()).whenIgnoringPaths("timestamp").isEqualTo(response);

    }

    private static Stream<Arguments> postProfileBadRequestSource(){


        return Stream.of(
                Arguments.of("post-request-profile-blank-fields-400.json", "post-response-profile-blank-fields-400.json"),
                Arguments.of("post-request-profile-empty-fields-400.json", "post-response-profile-empty-fields-400.json")
        );
    }
}
