package com.example.antonruban.postme.Model;

/**
 * Created by antonruban on 25.05.17.
 */

public class Post {
    private String username;
    private String post;


    public Post (String username, String post){
        this.username = username;
        this.post = post;
    }

    public void setPost_ (String p){
        this.post = p;
    }

    public String getPost_ (){
        return post;
    }

    public void setUsername_(String username){
        this.username = username;
    }

    public String getUsername_(){
        return username;
    }



}