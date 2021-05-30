package com.example.asyncgame;

public class Chat {

    private String datetime;
    private String author;
    private String content;

    public Chat(String datetime, String author, String content) {
        this.datetime = datetime;
        this.author = author;
        this.content = content;
    }

    public Chat() {}


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
