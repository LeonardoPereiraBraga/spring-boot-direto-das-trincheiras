package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animeList = new ArrayList<>();

    {
        var mappa = Anime.builder().id(1L).name("Mappa").build();
        var kyotoAnimation = Anime.builder().id(2L).name("Kyoto Animation").build();
        var madhouse = Anime.builder().id(3L).name("Madhouse").build();
        animeList.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }

    public List<Anime> getAnimeList() {
        return animeList;
    }
}
