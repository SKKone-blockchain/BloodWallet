package com.example.bloodwallet;

public class Comment {

    public String timestamp;
    public String content;
    public String userID;

    public Comment () {}
    public Comment(String timestamp, String content, String userID){
        this.timestamp = timestamp;
        this.content = content;
        this.userID = userID;
    }
}
