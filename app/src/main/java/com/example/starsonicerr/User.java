package com.example.starsonicerr;

public class User {
    private String userid;
    private String username;

    public User(String userid, String username, String userdate, String userday, String usercategory) {
        this.userid = userid;
        this.username = username;
        this.userdate = userdate;
        this.userday = userday;
        this.usercategory = usercategory;
    }

    private String userdate;

    private String userday;

    private String usercategory;

    public User() {

    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserdate() {
        return userdate;
    }

    public void setUserdate(String userdate) {
        this.userdate = userdate;
    }

    public String getUserday() { return userday;}

    public String getUsercategory() { return usercategory; }

    public void setUsercategory(String usercategory) {
        this.usercategory = usercategory;
    }



}