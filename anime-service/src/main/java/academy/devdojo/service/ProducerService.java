package academy.devdojo.service;


import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProducerService {

    private final ProducerHardCodedRepository repository;

    public List<Producer> findAll(String name){
        return name== null ? repository.findAll() : repository.findByName(name);
    }
    public Producer findByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producer not found"));
    }
    public Producer save(Producer producer){
        return repository.save(producer);
    }
    public void delete(Long id){
        Producer producerFounded = findByIdOrThrow(id);
        repository.delete(producerFounded);
    }
    public void update(Producer producerToUpdate){
        Producer producer = findByIdOrThrow(producerToUpdate.getId());
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        repository.update(producerToUpdate);
    }


}
