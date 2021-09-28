package com.itmo.server;

import com.itmo.client.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@AllArgsConstructor @Setter @Getter
public class Response implements Serializable {
    private String answer;
    private User user;
    public Response(String answer) {
        this.answer = answer;
    }
}