package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * The type Print descending command.
 */
public class PrintDescendingCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public PrintDescendingCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(Application application, User user) {
        return application.getCollection().printDescending();
    }

    @Override
    public String getDescription() {
        return "display the elements of the collection in descending order";
    }
}
