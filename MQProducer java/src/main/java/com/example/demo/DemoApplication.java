package com.example.demo;

import com.example.demo.messaging.ItemProducer;
import com.example.demo.models.Item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ItemProducer.initialize("tcp://127.0.0.1:61616", "items");
		Item i = new Item("Some Item");

		if (ItemProducer.inError()) {
			System.out.println(true);
		} else {
			ItemProducer.addItem(i);
			System.out.println("Item added to queue");
			if (ItemProducer.inError()) {
				System.out.println(true);
			}
		}
		SpringApplication.run(DemoApplication.class, args);
	}
}

