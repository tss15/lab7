package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.utils.FieldsScanner;
import com.itmo.utils.PassEncoder;

public class LoginCommand extends Command{
    private String login = null;
    private String pass = null;
    private final String  PEPPER = "1@#$&^%$)3";

    @Override
    public void clientInsertion() {
        login = FieldsScanner.getInstance().scanStringNotEmpty("login").trim();
        pass = FieldsScanner.getInstance().scanLine("password (no password - Enter)").trim();
    }

    @Override
    public String execute(Application application, User user) {
        User u = null;
        if(!application.activeUsers.containsUserName(login)){
            String hashPassword = new PassEncoder().getHash(pass, null);
            u = new User(login, hashPassword);
            if(application.manager.containsUser(u)){
                application.activeUsers.removeUser(user);
                user.setName(login);
                user.setHashPass(hashPassword);
                application.activeUsers.addUser(user);
                return "Hello, " + user.getName();
            }else{
                return "User with such username and password is not registered.";
            }
        }else{
            return "Such a user is already on the server.";
        }
    }

    @Override
    public String getDescription() {
        return "user authentication, required to access commands";
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }
}
