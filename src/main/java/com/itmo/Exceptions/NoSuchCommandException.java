package com.itmo.Exceptions;

public class NoSuchCommandException extends RuntimeException {
    public NoSuchCommandException(String badCommand){
        System.out.println("Such command \""+badCommand+"\" not found.");
    }
}
