package com.example.bloodwallet;

public class Owner {

    public String hospital_code;
    public String owner_id;
    public String user_id;

    public Owner (){}
    public Owner(String hospital_code, String owner_id, String user_id){
        this.hospital_code = hospital_code;
        this.owner_id = owner_id;
        this.user_id = user_id;
    }
}
