package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    private static final AnimeMapper ANIME_MAPPER = AnimeMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        var animes = Anime.getAnimes();
        List<AnimeGetResponse> animeGetResponseList = ANIME_MAPPER.toAnimeGetResponseList(animes);
        if (name == null) return ResponseEntity.status(HttpStatus.OK).body(animeGetResponseList);

        List<AnimeGetResponse> list = animeGetResponseList.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        Anime animefounded = Anime.getAnimes()
                .stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst().orElse(null);
        AnimeGetResponse animeGetResponse = ANIME_MAPPER.toAnimeGetResponse(animefounded);
        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponse);
    }

    @PostMapping
    public AnimeGetResponse save(@RequestBody AnimePostRequest animePostRequest) {
        Anime anime = ANIME_MAPPER.toAnime(animePostRequest);
        Anime.getAnimes().add(anime);
        AnimeGetResponse animeGetResponse = ANIME_MAPPER.toAnimeGetResponse(anime);
        return animeGetResponse;
    }

}
