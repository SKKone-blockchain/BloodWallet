package com.example.bloodwallet;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String post_id;
    public String title;
    public String story;
    public String user_id;
    public Integer donated_num;
    public Integer target_num;
    public String summary;
    public String timestamp;
    public String public_key;
//    public ArrayList<Comment> comments;

    public Post(){

    }

    public Post(String postID, String title, String story, String writer, Integer donatedNum, Integer targetNum, String summary, String timestamp, String public_key) {
        this.post_id = postID;
        this.title = title;
        this.story = story;
        this.user_id = writer;
        this.donated_num = donatedNum;
        this.target_num = targetNum;
        this.summary = summary;
        this.timestamp = timestamp;
        this.public_key = public_key;
    }



    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("post_id", post_id);
        result.put("title", title);
        result.put("story", story);
        result.put("user_id", user_id);
        result.put("donated_num", donated_num);
        result.put("target_num", target_num);
        result.put("summary", summary);
        result.put("timestamp", timestamp);
        return result;
    }



}
