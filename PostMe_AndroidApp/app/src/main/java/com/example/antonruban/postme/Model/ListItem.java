package com.example.antonruban.postme.Model;

public class ListItem {

    private String loginHead;
    private String postDesc;

    public String getLoginHead() {
        return loginHead;
    }
    public void setLoginHead(String loginHead) {
        this.loginHead = loginHead;
    }

    public ListItem(String loginHead, String postDesc) {
        this.loginHead = loginHead;
        this.postDesc = postDesc;
    }

    public String getPostDesc() {return postDesc;}
    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
}
