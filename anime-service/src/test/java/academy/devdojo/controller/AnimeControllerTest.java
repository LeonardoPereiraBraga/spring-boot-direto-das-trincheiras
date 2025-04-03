package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimeMapperImpl;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.service.AnimeService;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AnimeController.class)
@Import({AnimeData.class, AnimeHardCodedRepository.class, AnimeService.class, AnimeMapperImpl.class, FileUtils.class})
class AnimeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    AnimeData animeData;

    @SpyBean
    AnimeHardCodedRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    private final List<Anime> animeList = new ArrayList<>();


    @BeforeEach
    void init(){
        {
            var mappa = Anime.builder().id(1L).name("Mappa").build();
            var kyotoAnimation = Anime.builder().id(2L).name("Kyoto Animation").build();
            var madhouse = Anime.builder().id(3L).name("Madhouse").build();
            animeList.addAll(List.of(mappa, kyotoAnimation, madhouse));
        }
    }

    @Test
    @DisplayName("GET v1/animes returns all animes when null is given")
    void findAll_ReturnsAllAnimes_WhenNullIsGiven() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/animes?name=Mappa returns list with found object when name exists")
    void findByName_ReturnsAnime_WhenNameIsFound() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-Mappa-name-200.json");
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        String name = "Mappa";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/animes?name= returns empty list when name is not found")
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        String name = "naoExiste";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/animes/1 returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-by-Id-200.json");
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }
    @Test
    @DisplayName("GET v1/animes/99 throws NotFound when anime is not found")
    void findById_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception{
        String response = fileUtils.readResourceFile("anime/get-anime-by-Id-404.json");
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Long id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("POST v1/animes creates a anime")
    void save_CreatesAnime_WhenSuccessful() throws Exception{
        Anime animeTosave = Anime.builder().id(777L).name("Toei Animation").build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeTosave);
        var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes")
                        .content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("PUT v1/animes updates a anime")
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
    @Test
    @DisplayName("PUT v1/animes throws NotFound a anime")
    void update_ThrowsNotFound_WhenNotFound() throws Exception  {
        String response = fileUtils.readResourceFile("anime/get-anime-by-Id-404.json");
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("DELETE v1/animes removes a Anime")
    void delete_RemovesAnime_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }
    @Test
    @DisplayName("DELETE v1/animes/999 Throws NotFound When Anime is not found")
    void delete_ThrowsNotFound_WhenAnimeIsNotFound () throws Exception {
        String response = fileUtils.readResourceFile("anime/get-anime-by-Id-404.json");
        Long idNotExists = 999L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", idNotExists))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("PUT v1/animes updates a anime")
    void update_ReturnsBadRequest_WhenInvalidFields(String fileName, List<String> errors) throws Exception {
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }
    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes creates a anime")
    void save_ReturnsBadRequest_WhenInvalidFields(String fileName, List<String> errors) throws Exception{
        Anime animeTosave = Anime.builder().id(777L).name("Toei Animation").build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeTosave);
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes")
                        .content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }
    private static Stream<Arguments> putAnimeBadRequestSource(){
        var nameRequiredError = "The field 'name' is required";
        var idRequiredError = "the field 'id' cannot be null";
        var allErros = List.of(nameRequiredError,idRequiredError);


        return Stream.of(
                Arguments.of("put-request-anime-blank-fields-400.json", allErros),
                Arguments.of("put-request-anime-empty-fields-400.json", allErros)
        );
    }
    private static Stream<Arguments> postAnimeBadRequestSource(){
        var nameRequiredError = "The field 'name' is required";
        var allErros = List.of(nameRequiredError);


        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json", allErros),
                Arguments.of("post-request-anime-blank-fields-400.json", allErros)
        );
    }



}