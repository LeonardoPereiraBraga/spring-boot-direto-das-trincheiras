package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.commons.FileUtils;
import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.mapper.UserMapperImpl;
import academy.devdojo.user_service.repository.ProfileRepository;
import academy.devdojo.user_service.repository.UserRepository;
import academy.devdojo.user_service.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(UserController.class)
@Import({UserService.class, UserMapperImpl.class, FileUtils.class, UserRepository.class,ProfileRepository.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    FileUtils fileUtils;


    @MockBean
    private UserRepository repository;
    @MockBean
    private ProfileRepository profileRepository;


    private final List<User> userList = new ArrayList<>();

    @BeforeEach
    void init(){
        User user1 = User.builder().id(1L).firstName("Toyohisa").lastName("Shimazu").email("toyohisa@drifters.com").build();
        User user2 = User.builder().id(2L).firstName("Ichigo").lastName("Kurosaki").email("ichigo@bleach.com").build();
        User user3 = User.builder().id(3L).firstName("Ash").lastName("Ketchum").email("ash@pokemon.com").build();
        userList.addAll(List.of(user1,user2,user3));
    }

    @Test
    @DisplayName("GET v1/users returns all users when successful")
    void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        String response = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/users?firstName=Toyohisa returns all users when successful")
    void findAll_ReturnsAnime_WhenNameIsGiven() throws Exception {
        String firstName = "Toyohisa";
        User toyohisa = userList.stream().filter(user -> user.getFirstName().equals(firstName)).findFirst().orElse(null);
        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.singletonList(toyohisa));
        String response = fileUtils.readResourceFile("user/get-user-toyohisa-first-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("firstName",firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/users?firstName=x returns empty list when first name is not found")
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
        var firstName = "x";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/users/1 returns all users when successful")
    void findById_ReturnsAnime_WhenIdIsGiven() throws Exception {
        Long id = 1L;
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);
        String response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/999 throws ResponseException When User not Exists")
    void findById_ThrowsResponseException_WhenIdNotExists() throws Exception {
        Long id = 999L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @DisplayName("POST v1/users creates a user")
    void save_CreatesTheUser_WhenSuccessful() throws Exception {
        User userToSave = User.builder().id(99L).firstName("Yusuke").lastName("Urameshi").email("yusuke@yuyuhakusho.com").build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);
        String request = fileUtils.readResourceFile("user/post-request-user-200.json");
        String response = fileUtils.readResourceFile("user/post-response-user-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                        .content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/users creates a user")
    void deleteById_DeletesUser_WhenSuccessful() throws Exception {
        Long id = 1L;
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    @DisplayName("DELETE v1/users throws a ResponseException when User is not found")
    void deleteById_ThrowsResponseException_WhenUserIsNotFound() throws Exception {
        Long id = 999L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));
    }

    @Test
    @DisplayName("UPDATE v1/users updates a user")
    void update_updatesUser_WhenSuccessful() throws Exception {
        Long id = 1L;
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        String request = fileUtils.readResourceFile("user/put-request-user-200.json");
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("UPDATE v1/users throws a ResponseException when User is not found")
    void update_ThrowsResponseException_WhenUserIsNotFound() throws Exception {
        String request = fileUtils.readResourceFile("user/put-request-user-404.json");
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not found"));

    }
    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("UPDATE v1/users returns BadRequest when is given empty attributes")
    void update_ReturnsBadRequest_WhenIsGivenEmptyAttributes(String fileName, List<String> errors) throws Exception {
        String request = fileUtils.readResourceFile("user/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andReturn();
        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }
    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty")
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }
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