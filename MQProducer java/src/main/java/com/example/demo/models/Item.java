package com.example.demo.models;

public class Item {
    private static int nextId = 0;

    public int id;

    public String title;

    public Item(String title) {
        this.title = title;
        id = nextId++;
    }
}
