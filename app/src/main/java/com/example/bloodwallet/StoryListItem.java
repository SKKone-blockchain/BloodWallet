package com.example.bloodwallet;

public class StoryListItem implements Comparable<StoryListItem> {
    public String postID;
    public String writer;
    public String title;
    public String content;
    public String summary;
    public String uploadTime;
    public int donatedNum;
    public int goalNum;
    public Double score;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getScore(){
        return String.valueOf(score);
    }

    public int getPercent() {
        double percent = (double) donatedNum / goalNum * 100;
        return (int)percent;
    }

    @Override
    public int compareTo(StoryListItem storyListItem) {
        return this.score.compareTo(storyListItem.score);
    }
}
