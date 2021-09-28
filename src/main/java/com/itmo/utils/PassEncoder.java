package com.itmo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



@Getter
public class PassEncoder {
    private String password;

    public String getHash(String pass, String pepper) {
        password = pass;
        if(pass == null) return null;
        try {
            if(pepper!=null) pass = pepper + pass;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] data = messageDigest.digest(pass.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for(byte b : data) hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}