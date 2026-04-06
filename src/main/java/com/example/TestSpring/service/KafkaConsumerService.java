package com.example.TestSpring.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
public class KafkaConsumerService {

    private KafkaConsumer<String, String> consumer;
    private final Object lock = new Object();

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.topic}")
    private String topic;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public List<String> readAndCommitNewMessages() {
        synchronized (lock) {
            List<String> messages = new ArrayList<>();

            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));

                for (ConsumerRecord<String, String> record : records) {
                    messages.add(record.value());
                }

                if (!messages.isEmpty()) {
                    consumer.commitSync();
                    System.out.println("Commit " + messages.size() + " messages");
                }

            } catch (Exception e) {
                System.err.println("Error reading from Kafka: " + e.getMessage());
                e.printStackTrace();
                return new ArrayList<>();
            }

            return messages;
        }
    }

    @PreDestroy
    public void destroy() {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}