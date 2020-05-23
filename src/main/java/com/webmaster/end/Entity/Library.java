package com.webmaster.end.Entity;

public class Library {
    private int id;
    private String library;
    private String layer;

    public Library() {
    }

    public Library(int id, String library, String layer) {
        this.id = id;
        this.library = library;
        this.layer = layer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", library='" + library + '\'' +
                ", layer='" + layer + '\'' +
                '}';
    }
}
