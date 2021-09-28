package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.collection.LabWork;
import com.itmo.utils.FieldsScanner;

import java.util.Date;

/**
 * The type Add element command.
 */
public class AddElementCommand extends Command {

    private LabWork dr=null;

    /**
     * Instantiates a new Command.
     */
    public AddElementCommand(String[] args) {
    }

    @Override
    public void clientInsertion() {
        FieldsScanner fieldsScanner = FieldsScanner.getInstance();
        this.dr = fieldsScanner.scanLabWork();
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }


    @Override
    public String execute(Application application, User user){
        dr.setCreationDate(new Date());
        dr.setOwnerName(user.getName());
        application.manager.insertLabWork(dr);
        application.syncWithDB();
        return "Labwork added successfully!";
    }

    /**
     *
     * @return command description
     */
    @Override
    public String getDescription() {
        return "add new item to collection";
    }
}
