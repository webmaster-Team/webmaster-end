package com.webmaster.end.Entity;

public class Rental {
    private int id;
    private int bookId;
    private int userId;
    private long borrowTime;
    private long returnTime;
    private long duration;
    private int isReborrow;

    public Rental() {
    }

    public Rental(int id, int bookId, int userId, long borrowTime, long returnTime, long duration, int isReborrow) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
        this.duration = duration;
        this.isReborrow = isReborrow;
    }

    public Rental(int bookId, int userId, long borrowTime, long returnTime, long duration, int isReborrow) {
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

    public long getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(long borrowTime) {
        this.borrowTime = borrowTime;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
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
