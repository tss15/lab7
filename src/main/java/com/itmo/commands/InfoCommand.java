package com.itmo.commands;


import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * The type Info command.
 */
public class InfoCommand extends Command {
    /**
     * Instantiates a new Command.
     *
     */
    public InfoCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }




    @Override
    public String getDescription() {
        return "print collection information to standard output " +
                "(type, date of initialization, number of elements, etc.)";
    }

    @Override
    public String execute(Application application, User user) {
            return application.getCollection().getCollectionInfo();
    }
}
