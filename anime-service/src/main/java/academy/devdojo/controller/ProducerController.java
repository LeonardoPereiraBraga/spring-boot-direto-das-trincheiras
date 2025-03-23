package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
    private static final ProducerMapper PRODUCER_MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public List<Producer> listAll(@RequestParam(required = false) String name) {
        var producers = Producer.getProducers();
        if (name == null) return producers;

        return producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst().orElse(null);
    }

    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        Producer producer = PRODUCER_MAPPER.toProducer(producerPostRequest);
        Producer.getProducers().add(producer);
        ProducerGetResponse producerGetResponse = PRODUCER_MAPPER.toProducerGetResponse(producer);
        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);
    }

}
