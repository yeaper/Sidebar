package com.yyp.sidebar.model;

/**
 * Created by yyp on 2017/8/23.
 */
public class User {
    private String avatar;
    private String name;

    public User(String avatar, String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
