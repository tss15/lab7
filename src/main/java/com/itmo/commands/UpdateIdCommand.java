package com.itmo.commands;

import com.itmo.Exceptions.NotYourPropertyException;
import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.collection.LabWork;
import com.itmo.utils.FieldsScanner;

/**
 * The type Update id command.
 */
public class UpdateIdCommand extends Command{

    /**
     * Instantiates a new Command.
     */
    public UpdateIdCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }
    private LabWork dr;

    @Override
    public void clientInsertion() {
        FieldsScanner fieldsScanner = FieldsScanner.getInstance();
        dr = fieldsScanner.scanLabWork();
    }

    /**
     * updates the labwork at the specified id. implemented like this: first removes the element,
     * then creates a new one and assigns it the id of the past.
     * @return
     */
    @Override
    public String execute(Application application, User user) {
        try{
            long id = Long.parseLong(args[0].trim());
            LabWork prev = application.getCollection().findById(id);
            if(prev!=null){
                application.getCollection().remove(prev, user);
                dr.setId(id);
                application.getCollection().add(dr);
                return "LabWork added successfully!";
            }else{
                return "LabWork with id " + id + " not found in collection";
            }
        }catch (NumberFormatException e){
            return "ID - is a number";
        }catch (NotYourPropertyException e){
            return "this labwork belongs to " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "update the value of the collection element whose id is equal to the given";
    }
}