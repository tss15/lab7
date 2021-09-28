package com.itmo.commands;

import com.itmo.Exceptions.NoSuchCommandException;
import com.itmo.Exceptions.WrongArgumentsNumberException;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Command template
 * calls commands
 */
public class CommandsInvoker {
    // checking commands for correct input is also here.

    private static HashMap<String, Command> registeredCommands = new HashMap<>();


    private static CommandsInvoker instance;

    /**
     * Get instance commands invoker.
     *
     * @return the commands invoker
     */
    public static CommandsInvoker getInstance(){
        if (instance == null) {
            instance = new CommandsInvoker();
        }
        return instance;
    }
    private CommandsInvoker(){
    }

    public String printHelp(){
        //to sort by key (alphabet)
        Map<String, Command> treeMap = new TreeMap<>(registeredCommands);
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Command> entry : treeMap.entrySet()){
            builder.append(entry.getKey()).append(" : ").append(entry.getValue().getDescription()).append("\n");
        }
        return builder.toString();
    }

    /**
     * registers the command, i.e. matches a string to the command itself
     *
     * @param commandName the line on which the command will be called
     * @param command     command itself
     */
    public void register(String commandName, Command command){
        registeredCommands.put(commandName, command);
    }

    public void register(Map<String, Command> commandMap){
        registeredCommands.putAll(commandMap);
    }

    public Command validateCommand(String commandName, String[] arguments) throws NoSuchCommandException,
            WrongArgumentsNumberException {
        if(registeredCommands.containsKey(commandName)){
            Command command = registeredCommands.get(commandName);
            int requiredArgs = command.getNumberOfRequiredArgs();
            if(requiredArgs == arguments.length){
                command.setArgs(arguments);
                return registeredCommands.get(commandName);
            }else{
                throw new WrongArgumentsNumberException(requiredArgs, arguments.length);
            }
        }else {
            throw new NoSuchCommandException(commandName);
        }
    }
    /**
     * used, for example, in the help command
     *
     * @return map of registered commands
     */
    public HashMap<String, Command> getMapOfRegisteredCommands(){
        return registeredCommands;
    }
}