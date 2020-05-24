package com.webmaster.end.Entity;

public class Rental {
    private int id;
    private int bookId;
    private int userId;
    private String borrowTime;
    private String returnTime="0";
    private int duration;
    private int isReborrow=0;

    public Rental() {
    }

    public Rental(int id, int bookId, int userId, String borrowTime, String returnTime, int duration, int isReborrow) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
        this.duration = duration;
        this.isReborrow = isReborrow;
    }

    public Rental(int bookId, int userId, String borrowTime, String returnTime, int duration, int isReborrow) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
        this.duration = duration;
        this.isReborrow = isReborrow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIsReborrow() {
        return isReborrow;
    }

    public void setIsReborrow(int isReborrow) {
        this.isReborrow = isReborrow;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", borrowTime=" + borrowTime +
                ", returnTime=" + returnTime +
                ", duration=" + duration +
                ", isReborrow=" + isReborrow +
                '}';
    }
}
