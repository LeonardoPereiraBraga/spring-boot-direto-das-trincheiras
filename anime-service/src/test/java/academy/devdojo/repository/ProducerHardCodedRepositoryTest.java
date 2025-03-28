package academy.devdojo.repository;


import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest {

    @InjectMocks //Injetar dependencia
    private ProducerHardCodedRepository repository;

    //Na classe que tem o injectMocks sempre que achar producerData mocka ele
    @Mock
    private ProducerData producerData;
    private final List<Producer> producerList = new ArrayList<>();

    @BeforeEach
    void init(){
        var mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        var madhouse = Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();
        producerList.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }

    //Padrao de nome: nomeDoMetodo_ReturnsTalCoisa_QuandoSucedido/Fracasso
    @Test
    @DisplayName("findAll returns a list with all producers") //Nome do teste
    void findAll_ReturnsAllProducers_WhenSucessesfull(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotNull().hasSize(producers.size());
    }
    @Test
    @DisplayName("findById returns a producer with given id") //Nome do teste
    void findById_ReturnsProducerById_WhenSucessesfull(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var expectedProducer = producerList.getFirst();
        var producers = repository.findById(expectedProducer.getId());
        Assertions.assertThat(producers).isPresent().contains(expectedProducer);
    }
    @Test
    @DisplayName("findByName returns a empty list when name is null") //Nome do teste
    void findByName_ReturnsEmptyList_WhenNameIsNull(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("findByName returns list with foud object when name exists") //Nome do teste
    void findByName_ReturnsProducer_WhenNameIsFound(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var expectedProducer = producerList.getFirst();
        var producers = repository.findByName(expectedProducer.getName());
        Assertions.assertThat(producers).hasSize(1).contains(expectedProducer);
    }
    @Test
    @DisplayName("save creates a producer") //Nome do teste
    void save_CreatesProducer_WhenSuccessful(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producerToSave = Producer.builder().id(99L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var producer = repository.save(producerToSave);
        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
        Optional<Producer> producerSavedOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }
    @Test
    @DisplayName("delete removes a producer") //Nome do teste
    void delete_RemvesProducer_WhenSuccessful(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producerToDelete = producerList.getFirst();
        repository.delete(producerToDelete);

        Assertions.assertThat(this.producerList).isNotEmpty().doesNotContain(producerToDelete);
    }
    @Test
    @DisplayName("update update a producer") //Nome do teste
    void update_UpdatesProducer_WhenSuccessful(){
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producerToUpdate = this.producerList.getFirst();
        producerToUpdate.setName("Animeplex");
        repository.update(producerToUpdate);
        Assertions.assertThat(this.producerList).contains(producerToUpdate);
        Optional<Producer> producerUpdatedOptional = repository.findById(producerToUpdate.getId());
        Assertions.assertThat(producerUpdatedOptional).isPresent();
        Assertions.assertThat(producerUpdatedOptional.get().getName()).isEqualTo(producerToUpdate.getName());
    }

}