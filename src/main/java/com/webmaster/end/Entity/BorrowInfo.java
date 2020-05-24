package com.webmaster.end.Entity;

public class BorrowInfo {
    private int bookId;
    private int userId;
    private String username;
    private String bookname;
    private String borrowtime;
    private String returntime;
    private int duration;
    private int isReborrow;

    public BorrowInfo() {
    }

    public BorrowInfo(int bookId, int userId, String username, String bookname, String borrowtime,String returntime, int duration, int isReborrow) {
        this.bookId = bookId;
        this.userId = userId;
        this.username = username;
        this.bookname = bookname;
        this.borrowtime = borrowtime;
        this.returntime = returntime;
        this.duration = duration;
        this.isReborrow = isReborrow;
    }

    @Override
    public String toString() {
        return "BorrowInfo{" +
                "bookId=" + bookId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", bookname='" + bookname + '\'' +
                ", borrowtime='" + borrowtime + '\'' +
                ", returntime='" + returntime + '\'' +
                ", duration=" + duration +
                ", isReborrow=" + isReborrow +
                '}';
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBorrowtime() {
        return borrowtime;
    }

    public void setBorrowtime(String borrowtime) {
        this.borrowtime = borrowtime;
    }

    public String getReturntime() {
        return returntime;
    }

    public void setReturntime(String returntime) {
        this.returntime = returntime;
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
}
