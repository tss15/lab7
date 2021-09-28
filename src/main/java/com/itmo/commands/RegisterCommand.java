package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.server.ReadRequestThread;
import com.itmo.utils.FieldsScanner;
import com.itmo.utils.PassEncoder;
import com.itmo.utils.SimplePasswordGenerator;

public class RegisterCommand extends Command {
    private String login = null;
    private String pass = null;
    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public void clientInsertion() {
        login = FieldsScanner.getInstance().scanStringNotEmpty("login for registration");
        System.out.println("Your future login: " + login + ". Do you need a password?");
        pass = registerPassword();
    }

    @Override
    public String execute(Application application, User user) {
        if(!application.manager.containsUserName(login)){
            pass = new PassEncoder().getHash(pass, null);
            user.setName(login);
            user.setHashPass(pass);
            application.manager.insertUser(user);
            return "Registration successful. Your login: " + user.getName();
        }else return "User with this login is already registered.";
    }


    private String registerPassword() {
        FieldsScanner fs = FieldsScanner.getInstance();
        boolean yes = fs.scanYN();
        if(yes){
            String passw = fs.scanStringNotEmpty("password or enter generate to auto-generate it");
            passw = passw.trim().equals("generate") ?
                    new SimplePasswordGenerator(true, true, true, false ).generate(10,10)
                    : passw;
            System.out.println("Your future password: " + passw);
            return passw;
        }else{
            return "";
        }
    }
}
