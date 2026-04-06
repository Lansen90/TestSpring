package com.example.TestSpring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DataService {

    @Value("${kafka.consumer.topic}")
    private String topic;

    private final Queue<String> idQueue = new ConcurrentLinkedQueue<>();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final int BATCH_SIZE = 3;

    public DataService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public synchronized void addId(String id) {
        idQueue.offer(id);

        if (idQueue.size() >= BATCH_SIZE) {
            List<String> batch = new ArrayList<>();
            String item;
            while ((item = idQueue.poll()) != null) {
                batch.add(item);
            }

            for (String singleId : batch) {
                kafkaTemplate.send(topic, singleId);
            }
            System.out.println("Sent " + batch.size() + " messages to Kafka");
        }
    }
}