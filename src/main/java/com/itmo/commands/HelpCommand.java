package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * The type Help command.
 */
public class HelpCommand extends Command {

    /**
     * Instantiates a new Command.
     */
    public HelpCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public void clientInsertion() {
        System.out.println(CommandsInvoker.getInstance().printHelp());
    }

    @Override
    public String execute(Application application, User user) {
        return "";
    }

    @Override
    public String getDescription() {
        return "display list of available commands";
    }
}
