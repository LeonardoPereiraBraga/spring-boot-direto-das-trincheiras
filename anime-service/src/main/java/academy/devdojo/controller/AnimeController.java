package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.domain.Anime;
import academy.devdojo.domain.Anime;
import academy.devdojo.exception.DefaultErrorMessage;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.service.AnimeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {


    @Qualifier("animeMapper")
    private final AnimeMapper MAPPER;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);

        var animes = animeService.findAll(name);
        var animeGetResponse = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        Anime anime = animeService.findByIdOrThrow(id);
        AnimeGetResponse animeGetResponse = MAPPER.toAnimeGetResponse(anime);
        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest animePostRequest) {
        var anime = MAPPER.toAnime(animePostRequest);
        Anime animeSaved = animeService.save(anime);
        var response = MAPPER.toAnimePostResponse(animeSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        animeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime {}", request);


        var animeToUpdate = MAPPER.toAnime(request);
        animeService.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }



}
