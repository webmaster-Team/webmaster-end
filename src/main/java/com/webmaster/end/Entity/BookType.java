package com.webmaster.end.Entity;

public class BookType {
    private int id;
    private String type;
    private String title;

    public BookType() {
    }

    public BookType(int id, String type, String title) {
        this.id = id;
        this.type = type;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BookType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
