package com.loancare.lakeview.retrofitt;

/**
 * Created by 886016 on 3/13/2018.
 */

public class Userinput {

    private String username;
    private String password;
    private String resourcename;
    private String log;

    public Userinput(String username, String password, String android, String s) {
        this.username = username;
        this.password = password;
        this.resourcename = android;
        this.log = s;


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResourcename() {
        return resourcename;
    }

    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
