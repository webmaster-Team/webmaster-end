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
    private String entail;
    private int state;
    private String entryTime;
    private String deleteTime;

    public static final int CAN_BORROW=1;
    public static final int HAS_BORROW=2;
    public static final int NOT_BORROW=0;
    public static final int ERROR_BORROW=-1;

    public Book() {
    }

    public Book(int id, String name, String author, String ISBN, String publisher, int price, String version, String typeId, String summary, String cover, String library, String layer, String entail, int state, String entryTime, String deleteTime) {
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
        this.entail = entail;
        this.state = state;
        this.entryTime = entryTime;
        this.deleteTime = deleteTime;
    }

    public Book(String name, String author, String ISBN, String publisher, int price, String version, String typeId, String summary, String cover,String library,String layer,String entail, int state, String entryTime) {
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.price = price;
        this.version = version;
        this.typeId = typeId;
        this.summary = summary;
        this.cover = cover;
        this.library=library;
        this.layer=layer;
        this.entail=entail;
        this.state = state;
        this.entryTime = entryTime;
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

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entry_time) {
        this.entryTime = entry_time;
    }

    public String getDelete_time() {
        return deleteTime;
    }

    public void setDeleteTime(String delete_time) {
        this.deleteTime = delete_time;
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

    public String getEntail() {
        return entail;
    }

    public void setEntail(String entail) {
        this.entail = entail;
    }

    public String getDeleteTime() {
        return deleteTime;
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
                ", entail='" + entail + '\'' +
                ", state=" + state +
                ", entryTime='" + entryTime + '\'' +
                ", deleteTime='" + deleteTime + '\'' +
                '}';
    }
}
