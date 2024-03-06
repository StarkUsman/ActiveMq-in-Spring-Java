package com.example.demo;

import com.example.demo.messaging.ItemProducer;
import com.example.demo.models.Item;

public class Main {
    public static void main(String[] args) {
        ItemProducer.initialize("tcp://127.0.0.1:61616", "items");
        Item item = new Item("Some Item");

        if (ItemProducer.inError()) {
            System.out.println(true);
        } else {
            ItemProducer.addItem(item);
            System.out.println("Item added to queue");
            if (ItemProducer.inError()) {
                System.out.println(true);
            }
        }
        for (var i = 0; i < 10; i++) {
            item = new Item("Item " + i);
            ItemProducer.addItem(item);
            // ItemProducer.addItem(new Item("Item " + i));
        }
    }
}
