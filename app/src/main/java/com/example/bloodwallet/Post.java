package com.example.bloodwallet;

public class Post {

    public String summary;
    public String content;
    public int donated_num;
    public int goal_num;
    public String title;
    public String post_id;
    public String user_id;
    public String timestamp;

    public Post(){}
    public Post(String summary, String content, int donated_num, int goal_num, String title, String post_id, String user_id, String timestamp){
        this.summary = summary;
        this.content = content;
        this.donated_num = donated_num;
        this.goal_num = goal_num;
        this.title = title;
        this.post_id = post_id;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }


}
