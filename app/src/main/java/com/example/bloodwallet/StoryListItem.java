package com.example.bloodwallet;

import android.graphics.drawable.Drawable;

public class StoryListItem {
    private Drawable profilePhoto;
    private String storyTitle;
    private String storyContent;
    private String uploadTime;
    private String percent;

    public void setProfilePhoto(Drawable drawable) {
        if (drawable != null) {
            profilePhoto = drawable;
        }
    }

    public Drawable getProfilePhoto() {
        return profilePhoto;
    }

    public void setStoryTitle(String title) {
        storyTitle = title;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryContent(String content) {
        storyContent = content;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setUploadTime(String time) {
        uploadTime = time;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setPercent(int percent) {
        this.percent = percent + "%";
    }

    public String getPercent() {
        return percent;
    }
}
