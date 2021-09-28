package com.itmo.server;

import com.itmo.client.User;
import lombok.Getter;

import java.util.HashSet;

public class ActiveUsersHandler {
    @Getter
    private HashSet<User> activeUsers = new HashSet<>();
    private static ActiveUsersHandler handler=null;

    public static ActiveUsersHandler getInstance(){
        if(handler==null) return new ActiveUsersHandler();
        else return handler;
    }
    private ActiveUsersHandler(){
    }

    public boolean containsUserName(String name) {
        return activeUsers.stream().anyMatch(user -> user.getName().equals(name));
    }

    //add the user to active
    public boolean addUser(User user) {
        if (activeUsers.contains(user)) return false;
        activeUsers.add(user);
        return true;
    }
    //removing a user from active
    public void removeUser(User user) {
        activeUsers.remove(user);
    }
}