package com.fanok.mdpu24v1;

import java.util.Date;

public class Message {
    private String name, text, photo;
    private Date date;

    public Message(String name, String text, String photo, long date) {
        this.name = name;
        this.text = text;
        this.photo = photo;
        this.date = new Date(date);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = new Date(date);
    }
}
