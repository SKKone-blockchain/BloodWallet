package com.example.bloodwallet.ui;

public class User {
        private String name;
        private String birthdate;
        private String id;
        private String pw;
        private String phone_number;
        private String public_key;
        private String sex;
        private String email;
        public User() {}

        public User(String email, String ID, String name , String PW , String birthdate, String sex,String phone_number, String public_key){
            this.name=name;
            this.id=ID;
            this.email = email;
            this.birthdate=birthdate;
            this.pw=PW;
            this.phone_number=phone_number;
            this.public_key= public_key;
            this.sex=sex;
        }

        public String getName() {
            return this.name;
        }
        public String getBirthdate() {
            return birthdate;
        }
        public String getID() {
            return this.id;
        }
        public String getEmail() { return this.email; }
        public String getPW() {
            return this.pw;
        }
        public String getPhone_number() {
            return phone_number;
        }
        public String getPublic_key() {
            return public_key;
        }
        public String getSex() {
            return this.sex;
        }

        public void setName(String userName) {
            this.name = userName;
        }
        public void setBirthdate(String birthdate) {
            this.birthdate=birthdate;
        }
        public void setID(String ID) {
            this.id=ID;
        }
        public void setPW(String PW) {
            this.pw=PW;
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
