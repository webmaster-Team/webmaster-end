package com.webmaster.end.Entity;


public class User {
    public static int STUDENT_NUMBER=15;
    public static int TEACHER_NUMBER=30;

    private int id;
    private String card;
    private String name;
    //1为男，0为女
    private int sex=1;
    private String email="无";
    private String phone="0";
    private String cover="http://123.56.3.135:8081/image/default.png";
    private String signTime;
    private String deleteTime="0";
    private int identity=0;

    public User() {
    }

    public User(int id, String card, String name, int sex, String email, String phone, String cover, String signTime, String deleteTime,int identity) {
        this.id = id;
        this.card = card;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.cover = cover;
        this.signTime = signTime;
        this.deleteTime = deleteTime;
        this.identity=identity;
    }

    public User(String card, String name, int sex, String email, String phone, String cover, String signTime,int identity) {
        this.card = card;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.cover = cover;
        this.signTime = signTime;
        this.identity=identity;
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

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
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
                ", identity=" + identity +
                '}';
    }
}
