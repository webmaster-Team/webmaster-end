package com.webmaster.end.Entity;

public class BookType {
    private int id;
    private String typeid;
    private String title;

    public BookType() {
    }

    public BookType(int id, String typeid, String title) {
        this.id = id;
        this.typeid = typeid;
        this.title = title;
    }

    @Override
    public String toString() {
        return "BookType{" +
                "id=" + id +
                ", typeid='" + typeid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
