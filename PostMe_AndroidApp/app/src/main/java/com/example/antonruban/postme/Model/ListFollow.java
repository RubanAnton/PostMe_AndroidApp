package com.example.antonruban.postme.Model;


public class ListFollow {
    public String getLoginFollowList() {
        return loginFollowList;
    }

    public void setLoginFollowList(String loginFollowList) {
        this.loginFollowList = loginFollowList;
    }

    public ListFollow(String loginFollowList) {

        this.loginFollowList = loginFollowList;
    }

    private String loginFollowList;
}