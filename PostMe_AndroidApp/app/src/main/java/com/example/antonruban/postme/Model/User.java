package com.example.antonruban.postme.Model;

/**
 * Created by antonruban on 25.05.17.
 */

public class User {
    private String email;
    private String username;
    private String password;


    public User (String username, String password){
        this.username = username;
        this.password = password;
    }

    public void setEmail (String name){
        this.email = name;
    }

    public String getEmail (){
        return email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password =password;
    }

    public String getPassword(){
        return password;
    }

}
