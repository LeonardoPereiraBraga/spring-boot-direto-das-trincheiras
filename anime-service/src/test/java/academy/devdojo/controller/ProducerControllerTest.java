package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.SecurityConfig;
import academy.devdojo.domain.Anime;
import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapperImpl;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerRepository;
import academy.devdojo.service.ProducerService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProducerController.class)
@Import({ProducerMapperImpl.class, ProducerService.class, ProducerRepository.class, ProducerData.class, FileUtils.class})
@WithMockUser
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @MockBean
    private ProducerRepository repository;

    @Autowired
    private FileUtils fileUtils;

    private final List<Producer> producerList = new ArrayList<>();


    @BeforeEach
    void init(){
        //Colocando uma data fixa pra nao quebrar o teste
        String dateTime = "2024-08-06T10:36:59.441524";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);
        var mappa = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(localDateTime).build();
        var madhouse = Producer.builder().id(3L).name("Madhouse").createdAt(localDateTime).build();
        producerList.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }

    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns list with foud object when name exists")
    void findAll_ReturnsFoundProducerInList_WhenNameIsFound() throws Exception {
        var name = "Ufotable";
        Producer producerFounded = producerList.stream().filter(producer -> producer.getName().equals(name)).toList().getFirst();
        BDDMockito.when(repository.findByName(name)).thenReturn(List.of(producerFounded));
        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/producers returns empty list when name is not found")
    void findAll_ReturnsAllProducers_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", "x"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/producers/1 returns a producer with given id") //Nome do teste
    void findById_ReturnsProducerById_WhenSucessesfull() throws Exception{
        Long id = 1L;
        Producer producerFounded = producerList.stream().filter(producer -> producer.getId().equals(id)).toList().getFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(producerFounded));
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/producers/1 throws NotFound 404 when producer is not found") //Nome do teste
    void findById_ThrowsNotFound_WhenProducerIsNotFound() throws Exception{
        String response = fileUtils.readResourceFile("producer/get-producer-by-Id-404.json");
        var id = 999L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer") //Nome do teste
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
                        .content(request)
                        .header("x-api-key", "qualquercoisa")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }
    @Test
    @DisplayName("PUT v1/producers updates a producer") //Nome do teste
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        Long id = 1L;
        Producer producerFounded = producerList.stream().filter(producer -> producer.getId().equals(id)).toList().getFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(producerFounded));
        var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    @DisplayName("PUT v1/producers Throws NotFound When Producer is not found") //Nome do teste
    void update_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        String response = fileUtils.readResourceFile("producer/get-producer-by-Id-404.json");
        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("DELETE v1/producers/1 removes a producer")
    void delete_RemovesProducer_WhenSuccessful() throws Exception {
        Long id = 1L;
        Producer producerFounded = producerList.stream().filter(producer -> producer.getId().equals(id)).toList().getFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(producerFounded));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    @DisplayName("DELETE v1/producers/999 Throws NotFound When Producer is not found") //Nome do teste
    void delete_ThrowsNotFound_WhenProducerIsNotFound () throws Exception {
        String response = fileUtils.readResourceFile("producer/get-producer-by-Id-404.json");
        Long id = 999L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @ParameterizedTest
    @MethodSource("putProducerBadRequestSource")
    @DisplayName("PUT v1/producers updates a producer") //Nome do teste
    void update_ReturnsBadRequest_WhenInvalidFields(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }
    @ParameterizedTest
    @MethodSource("postProducerBadRequestSource")
    @DisplayName("POST v1/producers creates a producer") //Nome do teste
    void save_ReturnsBadRequest_WhenInvalidFIelds(String fileName, List<String> errors) throws Exception {
        var producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
                        .content(request)
                        .header("x-api-key", "qualquercoisa")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }

    private static Stream<Arguments> putProducerBadRequestSource(){
        var nameRequiredError = "The field 'name' is required";
        var idRequiredError = "the field 'id' cannot be null";
        var allErros = List.of(nameRequiredError,idRequiredError);


        return Stream.of(
                Arguments.of("put-request-producer-blank-fields-400.json", allErros),
                Arguments.of("put-request-producer-empty-fields-400.json", allErros)
        );
    }
    private static Stream<Arguments> postProducerBadRequestSource(){
        var nameRequiredError = "The field 'name' is required";
        var allErros = List.of(nameRequiredError);


        return Stream.of(
                Arguments.of("post-request-producer-blank-fields-400.json", allErros),
                Arguments.of("post-request-producer-empty-fields-400.json", allErros)
        );
    }





}