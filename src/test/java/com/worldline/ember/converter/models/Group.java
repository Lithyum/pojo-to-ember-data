package com.worldline.ember.converter.models;

import java.util.List;

public class Group {

    private String name;
    private Integer maxUsers;
    private List<User> users;
    private User[] managers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User[] getManagers() {
        return managers;
    }

    public void setManagers(User[] managers) {
        this.managers = managers;
    }
}
