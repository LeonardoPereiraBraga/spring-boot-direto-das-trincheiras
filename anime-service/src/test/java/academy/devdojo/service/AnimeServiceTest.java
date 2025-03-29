package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AnimeServiceTest {
    @InjectMocks
    AnimeService service;

    @Mock
    AnimeHardCodedRepository repository;

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
    @DisplayName("findAll returns all animes when null is given")
    void findAll_ReturnsAllAnimes_WhenNullIsGiven(){
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        List<Anime> allAnimes = service.findAll(null);
        Assertions.assertThat(allAnimes).isNotNull().hasSize(animeList.size());
    }
    @Test
    @DisplayName("findAll returns list with found object when name exists")
    void findByName_ReturnsAnime_WhenNameIsFound(){
        Anime anime = animeList.getFirst();
        List<Anime> expectedAnimesFound = List.of(anime);
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimesFound);
        var producersFound = service.findAll(anime.getName());
        Assertions.assertThat(producersFound).containsAll(expectedAnimesFound);
    }
    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Anime animeToFound = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToFound.getId())).thenReturn(Optional.of(animeToFound));
        Anime animeFounded = service.findByIdOrThrow(animeToFound.getId());
        Assertions.assertThat(animeFounded).isEqualTo(animeToFound);
    }
    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound(){
        Anime animeToFound = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToFound.getId())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.findByIdOrThrow(animeToFound.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }
    @Test
    @DisplayName("save creates a producer")
    void save_CreatesAnime_WhenSuccessful(){
        Anime animeToSave = animeList.getFirst();
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);
        Anime animeSaved = service.save(animeToSave);
        Assertions.assertThat(animeSaved).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }
    @Test
    @DisplayName("update updates a anime")
    void update_UpdatesAnime_WhenSuccessful() {
        Anime animeToUpdate = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }
    @Test
    @DisplayName("update throws ResponseStatusException a anime")
    void update_ThrowsResponseStatusException_WhenNotFound() {
        Anime animeToUpdate = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.update(animeToUpdate)).isInstanceOf(ResponseStatusException.class);
    }
    @Test
    @DisplayName("assertAnimeExists throws ResponseStatusException a anime")
    void assertAnimeExists_ThrowsResponseStatusException_WhenNotFound() {
        Anime animeToUpdate = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.assertAnimeExists(animeToUpdate.getId())).isInstanceOf(ResponseStatusException.class);
    }
    @Test
    @DisplayName("delete removes a Anime")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);
        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }
    @Test
    @DisplayName("delete Throws ResponseStatusException When Anime is not found")
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound (){

        Anime animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException().isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

}
