package com.example.bloodwallet;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Keyword {
    public String userID;
    public String kw1;
    public String kw2;
    public String kw3;

    public Keyword(){

    }

    public Keyword(String userID, String kw1, String kw2, String kw3){
        this.userID = userID;
        this.kw1 = kw1;
        this.kw2 = kw2;
        this.kw3 = kw3;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", userID);
        result.put("kw1", kw1);
        result.put("kw2", kw2);
        result.put("kw3", kw3);

        return result;
    }
}
