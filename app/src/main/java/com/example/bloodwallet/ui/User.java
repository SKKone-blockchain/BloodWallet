package com.example.bloodwallet.ui;

public class User {
        private String name;
//        private String keyword1;
//        private String keyword2;
//        private String keyword3;
        private String birthdate;
        private String id;
        private String pw;
        private String phone_number;
//        private String public_key;
        private String sex;
        public User() {}
//String keyword1, String keyword2, String keyword3,
    //,String public_key
    //,String phone_number
        public User(String ID, String name , String PW , String birthdate, String sex,String phone_number){
            this.name=name;
            this.id=ID;
           /* this.keyword1=keyword1;
            this.keyword2=keyword2;
            this.keyword3=keyword3;*/
            this.birthdate=birthdate;
            this.pw=PW;
            this.phone_number=phone_number;
            //this.public_key=public_key;
            this.sex=sex;
        }

        public String getName() {
            return this.name;
        }
//        public String getKeyword1() {
//            return keyword1;
//        }
//        public String getKeyword2() {
//            return keyword2;
//        }
//        public String getKeyword3() {
//            return keyword3;
//        }
        public String getBirthdate() {
            return birthdate;
        }
        public String getID() {
            return this.id;
        }
        public String getPW() {
            return this.pw;
        }
        public String getPhone_number() {
            return phone_number;
        }
//        public String getPublic_key() {
//            return public_key;
//        }
        public String getSex() {
            return this.sex;
        }

        public void setName(String userName) {
            this.name = userName;
        }
//        public void setKeyword1(String keyword1) {
//            this.keyword1 = keyword1;
//        }
//        public void setKeyword2(String keyword2) {
//            this.keyword2 = keyword2;
//        }
//        public void setKeyword3(String keyword3) {
//            this.keyword3 = keyword3;
//        }
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
//        public void setPublic_key(String public_key) {
//            this.public_key=public_key;
//        }
        public void setSex(String sex) {
            this.sex=sex;
        }

}
