package academy.devdojo.user_service.controller;

import academy.devdojo.user_service.commons.FileUtils;
import academy.devdojo.user_service.commons.ProfileUtils;
import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.mapper.ProfileMapperImpl;
import academy.devdojo.user_service.repository.ProfileRepository;
import academy.devdojo.user_service.repository.UserProfileRepository;
import academy.devdojo.user_service.repository.UserRepository;
import academy.devdojo.user_service.service.ProfileService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ProfileController.class)
@Import({ProfileRepository.class, ProfileMapperImpl.class, FileUtils.class, ProfileUtils.class, ProfileService.class, UserProfileRepository.class})
class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfileRepository repository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserProfileRepository userProfileRepository;
    private List<Profile> profileList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);
        var response = fileUtils.readResourceFile("profile/get-profiles-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    void findAll_ReturnsEmptyList_WhenNothingIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("profile/get-profiles-empty-list-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/profiles creates an profile")
    void save_CreatesProfile_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-profile-201.json");
        var profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileSaved);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/profiles")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/profiles")
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

    private static Stream<Arguments> postProfileBadRequestSource(){
        var NameRequiredError = "The field 'name' is required";
        var DescriptionRequiredError = "The field 'description' is required";

        var allErrors = List.of(NameRequiredError,DescriptionRequiredError);

        return Stream.of(
                Arguments.of("post-request-profile-blank-fields-400.json", allErrors),
                Arguments.of("post-request-profile-empty-fields-400.json", allErrors)
        );
    }

}