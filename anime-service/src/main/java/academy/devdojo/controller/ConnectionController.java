package academy.devdojo.controller;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private final Connection connectionMySql;

    @GetMapping
    public ResponseEntity<Connection> getConnections(){
        return ResponseEntity.ok(connectionMySql);

    }
}
