package com.example.bloodwallet;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String postID;
    public String title;
    public String story;
    public String writer;
    public Integer donatedNum;
    public Integer targetNum;
    public String summary;
//    public ArrayList<Comment> comments;

    public Post(){

    }

    public Post(String postID, String title, String story, String writer, Integer donatedNum, Integer targetNum, String summary){
        this.postID = postID;
        this.title = title;
        this.story = story;
        this.writer = writer;
        this.donatedNum = donatedNum;
        this.targetNum = targetNum;
        this.summary = summary;
    }



    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("postID", postID);
        result.put("title", title);
        result.put("story", story);
        result.put("writer", writer);
        result.put("donatedNum", donatedNum);
        result.put("targetNum", targetNum);
        result.put("summary", summary);
        return result;
    }
}
