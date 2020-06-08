package com.example.bloodwallet;

public class StoryListItem {
    public String title;
    public String content;
    public String summary;
    public int donatedNum;
    public int goalNum;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public int getPercent() {
        double percent = (double) donatedNum / goalNum * 100;
        return (int)percent;
    }
}
