package com.webmaster.end.Entity;

import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String createTime;
    private String compeleteTime="0";
    //0为在准备，1为代取阅，2为已完成，3为未取书失败，4为取消订阅
    private int state;
    private String qrcode="0";
    private List<Book> books;

    public Order() {
    }

    public Order(int id, int userId, String createTime, String compeleteTime, int state, String qrcode, List<Book> books) {
        this.id = id;
        this.userId = userId;
        this.createTime = createTime;
        this.compeleteTime = compeleteTime;
        this.state = state;
        this.qrcode = qrcode;
        this.books = books;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", createTime='" + createTime + '\'' +
                ", compeleteTime='" + compeleteTime + '\'' +
                ", state=" + state +
                ", qrcode='" + qrcode + '\'' +
                ", books=" + books +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompeleteTime() {
        return compeleteTime;
    }

    public void setCompeleteTime(String compeleteTime) {
        this.compeleteTime = compeleteTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
