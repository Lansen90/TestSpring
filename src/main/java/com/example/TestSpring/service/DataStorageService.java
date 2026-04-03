package com.example.TestSpring.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.ArrayList;

@Service
public class DataStorageService {

    private final ConcurrentLinkedQueue<String> storage = new ConcurrentLinkedQueue<>();

    public void saveId(String id) {

        if (id != null) {
            storage.offer(id);

        }

    }

    public List<String> getAllIds() {
        return new ArrayList<>(storage);
    }




}