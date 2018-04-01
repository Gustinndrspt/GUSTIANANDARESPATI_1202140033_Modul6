package com.example.gusti_r7i22ff.gustianandarespati_1202140033_modul6.model;

public class Post {
    String id;
    private String userID;
    private String username;
    private String titlePost;
    private String post;
    private String imagePost;
    public Long timestamp;



    public Post() {
    }

    public Post(String id, String userID, String username, String mImagePost, String titlePost, String post, Long timestamp) {
        this.id = id;
        this.username = username;
        this.imagePost = mImagePost;
        this.titlePost = titlePost;
        this.post = post;
        this.userID = userID;
        this.timestamp=timestamp;

    }

    public String getImagePost() {
        return imagePost;
    }

    public String getUserID() {
        return userID;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitlePost() {
        return titlePost;
    }

    public String getPost() {
        return post;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
