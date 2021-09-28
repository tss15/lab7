package com.itmo.commands;

import com.itmo.client.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Template Command
 */
public abstract class Command implements Executable, Serializable {
    @Getter @Setter
    private User user;
    protected String[] args;

    public void clientInsertion(){
    }

    /**
     * for commands like AddElement, AddIfMin, AddIfMax, UpdateIdCommand, the method returns 0, because
     * the element is entered line by line
     *
     * @return the number of arguments the command has
     */
    abstract public int getNumberOfRequiredArgs();


    /**
     * Instantiates a new Command.
     *
     */
    public Command(String[] args){
        this.args = args;
    }

    public Command(){}

    public void setArgs(String[] args){
        this.args = args;
    }
    /**
     * Get description string.
     *
     * @return command description
     */
    public String getDescription(){
        return " No command description has been added";
    }
}
