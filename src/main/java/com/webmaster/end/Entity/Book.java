package com.webmaster.end.Entity;

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
    private String library;
    private String layer;
    private String origin;
    private String entail;
    //表示剩余的树的数量
    private int state;
    private String entryTime;
    private String deleteTime="0";

    public Book() {
    }

    public Book(int id, String name, String author, String ISBN, String publisher, int price, String version, String typeId, String summary, String cover, String library, String layer, String origin, String entail, int state, String entryTime, String deleteTime) {
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
        this.library = library;
        this.layer = layer;
        this.origin = origin;
        this.entail = entail;
        this.state = state;
        this.entryTime = entryTime;
        this.deleteTime = deleteTime;
    }

    public Book(String name, String author, String ISBN, String publisher, int price, String version, String typeId, String summary, String cover, String library, String layer, String origin, String entail, int state, String entryTime) {
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.price = price;
        this.version = version;
        this.typeId = typeId;
        this.summary = summary;
        this.cover = cover;
        this.library = library;
        this.layer = layer;
        this.origin = origin;
        this.entail = entail;
        this.state = state;
        this.entryTime = entryTime;
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
                ", library='" + library + '\'' +
                ", layer='" + layer + '\'' +
                ", origin='" + origin + '\'' +
                ", entail='" + entail + '\'' +
                ", state=" + state +
                ", entryTime='" + entryTime + '\'' +
                ", deleteTime='" + deleteTime + '\'' +
                '}';
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getEntail() {
        return entail;
    }

    public void setEntail(String entail) {
        this.entail = entail;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }
}
