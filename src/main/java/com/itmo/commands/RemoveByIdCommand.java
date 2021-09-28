package com.itmo.commands;

import com.itmo.Exceptions.NotYourPropertyException;
import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * The type Remove by id command.
 */
public class RemoveByIdCommand extends Command {


    /**
     * Instantiates a new Command.
     *
     */
    public RemoveByIdCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }


    @Override
    public String execute(Application application, User user) {
        try{
            long id = Long.parseLong(args[0]);
            if(application.getCollection().removeById(id, user)){
                return "LabWork with id " + args[0] + " removed";
            }else{
                return "No such labwork with id " + args[0];
            }
        }catch (NumberFormatException e){
            return "id - this number is greater than zero";
        }catch (NotYourPropertyException e){
            return "property " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "remove an item from the collection by its id";
    }
}
