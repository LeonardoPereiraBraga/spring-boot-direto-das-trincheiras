package academy.devdojo.service;


import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Service
public class AnimeService {

    private AnimeHardCodedRepository repository;

    public List<Anime> findAll(String name){
        return name== null ? repository.findAll() : repository.findByName(name);
    }
    public Anime findByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Anime not found"));
    }
    public Anime save(Anime anime){
        return repository.save(anime);
    }
    public void delete(Long id){
        Anime animeFounded = findByIdOrThrow(id);
        repository.delete(animeFounded);
    }
    public void update(Anime animeToUpdate){
        assertAnimeExists(animeToUpdate.getId());
        repository.update(animeToUpdate);
    }
    public void assertAnimeExists(Long id){
        findByIdOrThrow(id);
    }


}
