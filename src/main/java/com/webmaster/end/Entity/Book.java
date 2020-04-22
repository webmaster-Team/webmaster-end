package com.webmaster.end.Entity;

import java.util.Date;

public class Book {
    private int id;
    private String name;
    private String author;
    private String ISBN;
    private String publisher;
    private int price;
    private String version;
    private String typeId;
    private String summary;
    private String cover;
    private int state;
    private Date entry_time;
    private Date delete_time;

    public Book() {
    }

    public Book(int id, String name, String author, String ISBN, String publisher, int price, String version, String typeId, String summary, String cover, int state, Date entry_time, Date delete_time) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.price = price;
        this.version = version;
        this.typeId = typeId;
        this.summary = summary;
        this.cover = cover;
        this.state = state;
        this.entry_time = entry_time;
        this.delete_time = delete_time;
    }

    public Book(String name, String author, String ISBN, String publisher, int price, String version, String typeId, String summary, String cover, int state, Date entry_time, Date delete_time) {
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.price = price;
        this.version = version;
        this.typeId = typeId;
        this.summary = summary;
        this.cover = cover;
        this.state = state;
        this.entry_time = entry_time;
        this.delete_time = delete_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(Date entry_time) {
        this.entry_time = entry_time;
    }

    public Date getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Date delete_time) {
        this.delete_time = delete_time;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", version='" + version + '\'' +
                ", typeId='" + typeId + '\'' +
                ", summary='" + summary + '\'' +
                ", cover='" + cover + '\'' +
                ", state=" + state +
                ", entry_time=" + entry_time +
                ", delete_time=" + delete_time +
                '}';
    }
}
