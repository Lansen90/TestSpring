
package com.example.TestSpring.controller;

import com.example.TestSpring.service.DataService;
import com.example.TestSpring.service.KafkaConsumerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    private final DataService dataService;
    private final KafkaConsumerService kafkaConsumerService;

    public DataController(DataService dataService, KafkaConsumerService kafkaConsumerService) {
        this.dataService = dataService;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("/data")
    public ResponseEntity<Void> saveData(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        dataService.addId(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/data")
    public ResponseEntity<List<String>> getData() {
        List<String> messages = kafkaConsumerService.readAndCommitNewMessages();
        return ResponseEntity.ok(messages);
    }
}