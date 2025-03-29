package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    AnimeHardCodedRepository repository;

    @Mock
    AnimeData animeData;
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
    @DisplayName("findAll returns a list of animes")
    void findAll_ReturnsAListofAnimes_WhenSuccessful(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        List<Anime> all = repository.findAll();
        Assertions.assertThat(all).isNotNull().hasSize(animeList.size());
    }
    @Test
    @DisplayName("findById returns a anime with given id")
    void findById_ReturnsAnime_WhenSuccessful(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Anime animeExpected = animeList.getFirst();
        Anime animeFounded = repository.findById(animeExpected.getId()).get();
        Assertions.assertThat(animeFounded).isEqualTo(animeExpected);
    }
    @Test
    @DisplayName("findByName must return a empty list when name is null")
    void findByName_ReturnsEmptyList_WhenNameIsNull(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        List<Anime> list = repository.findByName(null);
        Assertions.assertThat(list).isEmpty();
    }
    @Test
    @DisplayName("findByName returns a anime")
    void findByName_ReturnsAnime_WhenSuccessful(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Anime animeExpected = animeList.getFirst();
        List<Anime> animeFounded = repository.findByName(animeExpected.getName());
        Assertions.assertThat(animeFounded).contains(animeExpected).hasSize(1).isNotNull();
    }
    @Test
    @DisplayName("save saves a anime when successful")
    void save_createsAnime_WhenSucessful(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Anime animeToSave = Anime.builder().id(4L).name("Ufo").build();
        Anime animeSaved = repository.save(animeToSave);
        Assertions.assertThat(animeSaved).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
        Anime animeFounded = repository.findById(animeSaved.getId()).get();
        Assertions.assertThat(animeFounded).isEqualTo(animeSaved);
    }
    @Test
    @DisplayName("delete deletes a anime")
    void delete_DeletesAnime_WhenSuccessful(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Anime animeToDelete = animeList.getFirst();
        repository.delete(animeToDelete);
        List<Anime> allAnimes = repository.findAll();
        Assertions.assertThat(allAnimes).doesNotContain(animeToDelete);
    }
    @Test
    @DisplayName("updates a anime")
    void update_UpdatesAnime_WhenSuccessful(){
        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);
        Anime animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("MappaAtualizado");
        repository.update(animeToUpdate);
        Assertions.assertThat(this.animeList).contains(animeToUpdate);
        Anime animeFounded = repository.findById(animeToUpdate.getId()).get();
        Assertions.assertThat(animeFounded.getName()).isEqualTo(animeToUpdate.getName());
    }


}