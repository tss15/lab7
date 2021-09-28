package com.itmo.Exceptions;

public class NoSuchLabWorkException extends Exception {
    /**
     * only used with findById command
     * @param id id of the labwork that was not found
     */
    public NoSuchLabWorkException(long id){
        System.out.println("LabWork with id " + id + " not found.");
    }

}
