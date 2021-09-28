package com.itmo.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    private String name;
    private String hashPass;
    private long id;
    private double red, green, blue;




    public User(String name, String hashPass){
        this.name = name;
        this.hashPass = hashPass;
    }

    public User(String name){
        this.name = name;
    }





}