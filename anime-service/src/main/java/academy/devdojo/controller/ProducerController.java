package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.service.ProducerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {
    @Qualifier("producerMapper")
    private final ProducerMapper MAPPER;
    private final ProducerService producerService;
    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = producerService.findAll(name);
        var producerGetResponse = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        Producer producer = producerService.findByIdOrThrow(id);
        ProducerGetResponse producerGetResponse = MAPPER.toProducerGetResponse(producer);
        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);
        var producer = MAPPER.toProducer(producerPostRequest);
        Producer producerSaved = producerService.save(producer); 
        var response = MAPPER.toProducerPostResponse(producerSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);

        producerService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        log.debug("Request to update producer {}", request);


        var producerToUpdate = MAPPER.toProducer(request);
        producerService.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }


}
