package com.webmaster.end.Entity;

import java.util.Date;

public class User {
    private int id;
    private String card;
    private String name;
    //1为男，0为女
    private int sex;
    private String email;
    private String phone;
    private String cover;
    private String signTime;
    private String deleteTime;

    public User() {
    }

    public User(int id, String card, String name, int sex, String email, String phone, String cover, String signTime, String deleteTime) {
        this.id = id;
        this.card = card;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.cover = cover;
        this.signTime = signTime;
        this.deleteTime = deleteTime;
    }

    public User(String card, String name, int sex, String email, String phone, String cover, String signTime) {
        this.card = card;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.cover = cover;
        this.signTime = signTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String sign_time) {
        this.signTime = sign_time;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String delete_time) {
        this.deleteTime = deleteTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", card='" + card + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", cover='" + cover + '\'' +
                ", signTime='" + signTime + '\'' +
                ", deleteTime='" + deleteTime + '\'' +
                '}';
    }
}
