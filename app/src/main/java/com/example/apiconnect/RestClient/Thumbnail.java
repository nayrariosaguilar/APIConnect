package com.example.apiconnect.RestClient;

public class Thumbnail {
    String path;
    String extension;

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }

    public Thumbnail(String extension,String path) {
        this.extension = extension;
        this.path = path;
    }
    public String getFullUrl() {
        return path + "." + extension;
    }
}
