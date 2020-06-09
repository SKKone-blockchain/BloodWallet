package com.example.bloodwallet.ui;

import java.util.HashMap;
import java.util.Map;

public class Currentuserinfo {
    public String name;
    public String keyword1;
    public String keyword2;
    public String keyword3;
    public String birthdate;
    public String ID;
    public String PW;
    public String phone_number;
    public String public_key;
    public String sex;
    public Currentuserinfo() {}

    public Currentuserinfo( String name,String ID, String PW , String keyword1, String keyword2, String keyword3, String birthdate, String sex, String public_key, String phone_number){
        this.name=name;
        this.ID=ID;
        this.keyword1=keyword1;
        this.keyword2=keyword2;
        this.keyword3=keyword3;
        this.birthdate=birthdate;
        this.PW=PW;
        this.phone_number=phone_number;
        this.public_key=public_key;
        this.sex=sex;
    }
   /* public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", ID);
        result.put("name", name);
        result.put("pw", PW);
        result.put("sex", sex);
        return result;     }*/
    public String getName() {
        return name;
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
    public String getBirthdate() {
        return birthdate;
    }
    public String getID() {
        return ID;
    }
    public String getPW() {
        return PW;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public String getPublic_key() {
        return public_key;
    }
    public String getSex() {
        return sex;
    }

    public void setName(String userName) {
        this.name = name;
    }
    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }
    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }
    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate=birthdate;
    }
    public void setID(String ID) {
        this.ID=ID;
    }
    public void setPW(String PW) {
        this.PW=PW;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number=phone_number;
    }
    public void setPublic_key(String public_key) {
        this.public_key=public_key;
    }
    public void setSex(String sex) {
        this.sex=sex;
    }

}
