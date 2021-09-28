package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.collection.LabWork;

import java.util.Set;

/**
 * The type Filter starts with name command.
 */
public class FilterStartsWithNameCommand extends Command {

    /**
     * Instantiates a new Command.
     */
    public FilterStartsWithNameCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }

    @Override
    public String execute(Application application, User user) {
        Set<LabWork> res =
                application.getCollection().filterStartsWithName(args[0]);
        if(res.size()!=0){
            StringBuilder builder = new StringBuilder("Items in the collection whose names begin" +
                    "with string " + args[0] + ": " + res.size() +"\n");
            res.stream().map(LabWork::getName).forEach(d ->builder.append(d).append("\n"));
            return builder.toString();
        }else{
            return "LabWorks with names starting with " + args[0] + " don't exist.";
        }
    }
    @Override
    public String getDescription() {
        return "display elements whose name field value begins with a given substring";
    }
}
