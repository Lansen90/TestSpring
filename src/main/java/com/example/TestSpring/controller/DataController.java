package com.example.TestSpring.controller;

import com.example.TestSpring.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Autowired
    private DataStorageService storageService;

    @PostMapping("/api/data")
    public ResponseEntity<Void> saveData(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        storageService.saveId(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/api/data")
    public ResponseEntity<List<String>> getAllData() {
        List<String> data = storageService.getAllIds();
        return ResponseEntity.ok(data);
    }

}