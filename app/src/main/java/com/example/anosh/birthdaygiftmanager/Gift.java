package com.example.anosh.birthdaygiftmanager;

/**
 * Created by Anosh on 5/11/2015.
 */
public class Gift {
    long id;
    String giftName;
    String birthdayId;
    double price;
    String status;
    String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBirthdayId() {
        return birthdayId;
    }

    public void setBirthdayId(String birthdayId) {
        this.birthdayId = birthdayId;
    }
}
