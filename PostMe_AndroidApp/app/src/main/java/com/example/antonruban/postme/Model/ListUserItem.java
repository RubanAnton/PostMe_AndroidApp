package com.example.antonruban.postme.Model;

/**
 * Created by antonruban on 18.05.17.
 */

public class ListUserItem {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ListUserItem(String login) {

        this.login = login;
    }
}
