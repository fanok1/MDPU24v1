package com.fanok.mdpu24v1;

public class Article {
    private String image;
    private String urlArticle;
    private String title;
    private String date;

    public Article(String image, String urlArticle, String title, String date) {
        this.image = image;
        this.urlArticle = urlArticle;
        this.title = title;
        this.date = date;
    }

    public Article() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    public void setUrlArticle(String urlArticle) {
        this.urlArticle = urlArticle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
