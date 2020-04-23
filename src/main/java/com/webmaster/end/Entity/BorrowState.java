package com.webmaster.end.Entity;
/**
 *
 * @Description: 书籍借阅状态的封装类，包含状态码、提示消息、书籍对象、借阅信息对象
 * @Author: Daniel
 * @Date: 2020/4/23 2:15 下午
 */
public class BorrowState {

    private int state;
    private Rental rental;
    private Book book;
    private String msg;

    public BorrowState(){}

    public BorrowState(int state, Rental rental, Book book, String msg){
        setState(state); setBook(book); setRental(rental); setMsg(msg);
    }

    public BorrowState(int state, String msg){
        setMsg(msg); setRental(null); setBook(null); setState(state);
    }

    public void setState(int state){
        this.state = state;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setRental(Rental rental){
        this.rental = rental;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public Rental getRental() {
        return rental;
    }

    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

}
