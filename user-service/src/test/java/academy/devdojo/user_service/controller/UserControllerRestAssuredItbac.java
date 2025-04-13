package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.commons.FileUtils;
import academy.devdojo.user_service.commons.UserUtils;
import academy.devdojo.user_service.config.TestcontainersConfiguration;
import academy.devdojo.user_service.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerRestAssuredItbac {
    private static final String URL = "/v1/users";

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUrl(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //antes do teste executa esse sql
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");
        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .and(users -> {
                    users.node("[0].id").asNumber().isPositive();
                    users.node("[1].id").asNumber().isPositive();
                    users.node("[2].id").asNumber().isPositive();
                });
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id").isEqualTo(expectedResponse);


    }
    @Test
    @DisplayName("GET v1/users?firstName=Toyohisa returns all users when successful")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //antes do teste executa esse sql
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsAnime_WhenNameIsGiven() throws Exception {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-toyohisa-first-name-200.json");
        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", "Toyohisa")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id").isEqualTo(expectedResponse);

    }
    @Test
    @DisplayName("GET v1/users?firstName=x returns empty list when first name is not found")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //antes do teste executa esse sql
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() throws Exception {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", "x")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id").isEqualTo(expectedResponse);



    }
    @Test
    @DisplayName("GET v1/users/1 returns all users when successful")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //antes do teste executa esse sql
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnsAnime_WhenIdIsGiven() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var users = repository.findByFirstNameIgnoreCase("Toyohisa");
        Assertions.assertThat(users).hasSize(1);

        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", users.getFirst().getId())
                .get(URL+"/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id").isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("GET v1/users/99 throws ResponseException When User not Exists")
    void findById_ThrowsResponseException_WhenIdNotExists() throws Exception {
        String expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-404.json");
        Long id = 99L;
        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp").isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("POST v1/users creates a user")
    void save_CreatesTheUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");
        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id").isEqualTo(expectedResponse);
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id").isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("DELETE v1/users/1 creates a user")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteById_DeletesUser_WhenSuccessful() throws Exception {
        var id = repository.findAll().getFirst().getId();

        RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL+"/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }
    @Test
    @DisplayName("DELETE v1/users throws a ResponseException when User is not found")
    void deleteById_ThrowsResponseException_WhenUserIsNotFound() throws Exception {
        var expectedResponse = fileUtils.readResourceFile("user/delete-user-by-id-404.json");
        Long id = 999L;
        String response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp").isEqualTo(expectedResponse);
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp").isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("UPDATE v1/users updates a user")
    @Sql(value = "/sql/user/init_one_user2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //antes do teste executa esse sql
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_updatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        var users = repository.findByFirstNameIgnoreCase("Yusuke");

        String replace = request.replace("1", users.getFirst().getId().toString());

        RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(replace)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }

    @Test
    @DisplayName("UPDATE v1/users throws a ResponseException when User is not found")
    void update_ThrowsResponseException_WhenUserIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var expectedResponse = fileUtils.readResourceFile("user/put-user-by-id-404.json");
        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().response().body().asString();
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp").isEqualTo(expectedResponse);

    }


//    }
//    @ParameterizedTest
//    @MethodSource("putUserBadRequestSource")
//    @DisplayName("UPDATE v1/users returns BadRequest when is given empty attributes")
//    void update_ReturnsBadRequest_WhenIsGivenEmptyAttributes(String fileName, List<String> errors) throws Exception {
//        String request = fileUtils.readResourceFile("user/%s".formatted(fileName));
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        Exception resolvedException = mvcResult.getResolvedException();
//        Assertions.assertThat(resolvedException).isNotNull();
//        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
//
//    }
//    @ParameterizedTest
//    @MethodSource("postUserBadRequestSource")
//    @DisplayName("POST v1/users returns bad request when fields are empty")
//    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
//        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));
//
//        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                        .post("/v1/users")
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andReturn();
//
//        var resolvedException = mvcResult.getResolvedException();
//
//        Assertions.assertThat(resolvedException).isNotNull();
//
//        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
//    }
    //Metodo para parametrizar o teste
    private static Stream<Arguments> postUserBadRequestSource(){
        var firstNameRequiredError = "The field 'firstName' is required";
        var lastNameRequiredError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";
        var emailInvalidError = "The e-mail is not valid";

        var allErrors = List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError);
        var emailError = Collections.singletonList(emailInvalidError);

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", allErrors),
                Arguments.of("post-request-user-blank-fields-400.json", allErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailError)
        );
    }
    private static Stream<Arguments> putUserBadRequestSource(){
        var firstNameRequiredError = "The field 'firstName' is required";
        var lastNameRequiredError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";
        var emailInvalidError = "The e-mail is not valid";
        var idRequiredError = "the field 'id' cannot be null";

        var allErrors = List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError, idRequiredError);
        var emailError = Collections.singletonList(emailInvalidError);

        return Stream.of(
                Arguments.of("post-request-user-blank-fields-400.json", allErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailError),
                Arguments.of("put-request-user-empty-fields-400.json", allErrors)
        );
    }

}
