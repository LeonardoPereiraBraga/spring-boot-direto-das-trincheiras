package academy.devdojo.service;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;

    private final List<Producer> producerList = new ArrayList<>();

    @BeforeEach
    void init(){
        var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        var madhouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();
        producerList.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }

    @Test
    @DisplayName("findAll returns a list with all producers") //Nome do teste
    void findAll_ReturnsAllProducers_WhenSucessesfull(){
        BDDMockito.when(repository.findAll()).thenReturn(producerList);
        var producers = service.findAll(null);
        Assertions.assertThat(producers).isNotNull().hasSize(producers.size());
    }
    @Test
    @DisplayName("findAll returns list with foud object when name exists") //Nome do teste
    void findByName_ReturnsProducer_WhenNameIsFound(){
        Producer producer = producerList.getFirst();
        List<Producer> expectedProducersFound = List.of(producer);
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(expectedProducersFound);
        var producersFound = service.findAll(producer.getName());
        Assertions.assertThat(producersFound).containsAll(expectedProducersFound);
    }
    @Test
    @DisplayName("findAll returns a empty list when name is not found") //Nome do teste
    void findByName_ReturnsEmptyList_WhenNameIsNotFound(){
        String nameWhichDontExists = "notfound";
        BDDMockito.when(repository.findByName(nameWhichDontExists)).thenReturn(emptyList());
        var producers = service.findAll(nameWhichDontExists);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("findById returns a producer with given id") //Nome do teste
    void findById_ReturnsProducerById_WhenSucessesfull(){
        Producer expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));
        Producer producers = service.findByIdOrThrow(expectedProducer.getId());
        Assertions.assertThat(producers).isEqualTo(expectedProducer);
    }
    @Test
    @DisplayName("findById throws ResponseStatusException when producer is not found") //Nome do teste
    void findById_ThrowsResponseStatusException_WhenProducerIsNotFound(){
        Producer expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findByIdOrThrow(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }
    @Test
    @DisplayName("save creates a producer") //Nome do teste
    void save_CreatesProducer_WhenSuccessful(){
        var producerToSave = Producer.builder().id(99L).name("Mappa").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var savedProducer = service.save(producerToSave);
        Assertions.assertThat(savedProducer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    }
    @Test
    @DisplayName("delete removes a producer") //Nome do teste
    void delete_RemovesProducer_WhenSuccessful(){
        Producer producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);
        Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }
    @Test
    @DisplayName("delete Throws ResponseStatusException When Producer is not found") //Nome do teste
    void delete_ThrowsResponseStatusException_WhenProducerIsNotFound (){

        Producer producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.delete(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }
    @Test
    @DisplayName("update updates a prodcuer") //Nome do teste
    void update_UpdatesProducer_WhenSuccessful(){
        Producer producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("ani");
        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));
    }
    @Test
    @DisplayName("update Throws ResponseStatusException When Producer is not found") //Nome do teste
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound(){
        Producer producerToUpdate = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }


}