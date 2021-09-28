package com.itmo.collection;

import java.io.Serializable;

public class Location implements Serializable {
    private int x;
    private Long y; //The field cannot be null
    private Float z; //The field cannot be null
    private String name; //The field cannot be null

    public Location(int x, Long y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "x: " + x +", y: "+ y +", z: "+ z +", locname: "+ name;
    }
}