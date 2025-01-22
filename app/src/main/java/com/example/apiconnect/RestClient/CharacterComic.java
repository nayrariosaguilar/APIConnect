package com.example.apiconnect.RestClient;

public class CharacterComic {
    int id;
    Thumbnail thumbnail;
    String name;
    String description;

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CharacterComic(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
