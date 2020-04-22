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
    private Date sign_time;
    private Date delete_time;

    public User() {
    }

    public User(int id, String card, String name, int sex, String email, String phone, Date sign_time, Date delete_time) {
        this.id = id;
        this.card = card;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.sign_time = sign_time;
        this.delete_time = delete_time;
    }

    public User(String card, String name, int sex, String email, String phone, Date sign_time, Date delete_time) {
        this.card = card;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.sign_time = sign_time;
        this.delete_time = delete_time;
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

    public Date getSign_time() {
        return sign_time;
    }

    public void setSign_time(Date sign_time) {
        this.sign_time = sign_time;
    }

    public Date getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Date delete_time) {
        this.delete_time = delete_time;
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
                ", sign_time=" + sign_time +
                ", delete_time=" + delete_time +
                '}';
    }
}
