package com.example.bloodwallet.ui;

public class Userkeywords {
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String userID;

    public Userkeywords() {}

    public Userkeywords(String userID, String keyword1,String keyword2, String keyword3) {
        this.userID = userID;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
    }

    public String getKeyword1() {
        return keyword1;
    }
    public String getKeyword2() {
        return keyword2;
    }
    public String getKeyword3() {
        return keyword3;
    }
    public String getUserID() { return userID; }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }
    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }
    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }
}