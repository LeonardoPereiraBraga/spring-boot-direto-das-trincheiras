package academy.devdojo.service;


import academy.devdojo.domain.Anime;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnimeService {

    private final AnimeRepository repository;

    public List<Anime> findAll(String name){
        return name== null ? repository.findAll() : repository.findByName(name);
    }
    public Page<Anime> findAllPaginated(Pageable pageable){
        return repository.findAll(pageable);
    }
    public Anime findByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Anime not found"));
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
        repository.save(animeToUpdate);
    }
    public void assertAnimeExists(Long id){
        findByIdOrThrow(id);
    }


}
