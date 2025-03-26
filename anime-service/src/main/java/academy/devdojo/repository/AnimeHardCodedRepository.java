package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        var mappa = Anime.builder().id(1L).name("Mappa").build();
        var kyotoAnimation = Anime.builder().id(2L).name("Kyoto Animation").build();
        var madhouse = Anime.builder().id(3L).name("Madhouse").build();
        ANIMES.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }


    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id){
        return ANIMES
                .stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public List<Anime> findByName(String name){
        return ANIMES.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }
    public Anime save(Anime anime){
        ANIMES.add(anime);
        return anime;
    }
    public void delete(Anime anime){
        ANIMES.remove(anime);

    }
    public void update(Anime anime){
        delete(anime);
        save(anime);

    }
}
