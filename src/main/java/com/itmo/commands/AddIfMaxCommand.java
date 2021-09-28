package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.collection.LabWork;
import com.itmo.utils.FieldsScanner;

import java.util.Date;

/**
 * The type Add if max command.
 */
public class AddIfMaxCommand extends Command{
    private LabWork dr;

    @Override
    public void clientInsertion() {
        FieldsScanner helper = FieldsScanner.getInstance();
        dr = helper.scanLabWork();
    }

    /**
     * Instantiates a new Command.
     *
     * @param args
     */

    public AddIfMaxCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(Application application, User user) {
        if(application.getCollection().isMax(dr)){
            dr.setCreationDate(new Date());
            dr.setOwnerName(user.getName());
            application.manager.insertLabWork(dr);
            application.syncWithDB();
            return application.getCollection().addIfMax(dr);
        }else return "Not added since not max";
    }

    @Override
    public String getDescription() {
        return "add a new item to the collection if its value is greater than the value of the largest item in this collection";
    }
}
