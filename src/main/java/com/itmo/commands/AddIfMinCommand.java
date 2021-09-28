package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.collection.LabWork;
import com.itmo.utils.FieldsScanner;

import java.util.Date;

/**
 * The type Add if min command.
 */
public class AddIfMinCommand extends Command {
    private LabWork dr;
    /**
     * Instantiates a new Command.
     */
    public AddIfMinCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public void clientInsertion() {
        FieldsScanner helper = FieldsScanner.getInstance();
        dr = helper.scanLabWork();
    }

    @Override
    public String execute(Application application, User user) {
        dr.setCreationDate(new Date());
        dr.setOwnerName(user.getName());
        if(application.getCollection().isMin(dr)){
            application.manager.insertLabWork(dr);
            application.syncWithDB();
            return application.getCollection().addIfMin(dr);
        }else return "Not added since not min";
    }

    @Override
    public String getDescription() {
        return "add a new item to a collection if its value is less than the smallest item in this collection";
    }
}
