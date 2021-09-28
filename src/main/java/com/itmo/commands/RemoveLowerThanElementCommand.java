package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.collection.LabWork;
import com.itmo.utils.FieldsScanner;

/**
 * The type Remove lower element command.
 */
public class RemoveLowerThanElementCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public RemoveLowerThanElementCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    private LabWork dr;
    @Override
    public void clientInsertion() {
        FieldsScanner helper = FieldsScanner.getInstance();
        dr = helper.scanLabWork();
    }

    @Override
    public String execute(Application application, User user) {
        return application.getCollection().removeLower(dr, user);
    }

    @Override
    public String getDescription() {
        return "remove all elements from the collection that are less than the given one";
    }
}
