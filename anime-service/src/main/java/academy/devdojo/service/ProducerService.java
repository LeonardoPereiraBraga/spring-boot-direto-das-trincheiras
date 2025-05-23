package academy.devdojo.service;


import academy.devdojo.domain.Producer;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProducerService {

    private final ProducerRepository repository;

    public List<Producer> findAll(String name){
        return name== null ? repository.findAll() : repository.findByName(name);
    }
    public Producer findByIdOrThrow(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Producer not found"));
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
        repository.save(producerToUpdate);
    }


}
