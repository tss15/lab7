package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * The type Print field ascending minimalPoint command.
 */
public class PrintFieldAscendingMinimalPointCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public PrintFieldAscendingMinimalPointCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(Application application, User user) {
        return application.getCollection().printFieldAscendingMinimalPoint();
    }

    @Override
    public String getDescription() {
        return "display the values of the minimalPoint field in ascending order";
    }
}
