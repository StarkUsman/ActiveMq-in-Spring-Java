package com.example.consumer;

import com.example.consumer.messaging.ItemConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        ItemConsumer.initialize("tcp://127.0.0.1:61613", "items");

        char c;
        try {
            c = (char) System.in.read();
        } catch (Exception e) {
        }

        ItemConsumer.terminate();
    }

}
