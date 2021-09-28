package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * The type Exit command.
 */
public class ExitCommand extends Command {


    /**
     * Instantiates a new Command.
     *
     * @param args
     */
    public ExitCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }


    @Override
    public String getDescription() {
        return "close program";
    }

    @Override
    public String execute(Application application, User user) {
        application.activeUsers.removeUser(user);
        return "Bye-Bye";
    }
}
