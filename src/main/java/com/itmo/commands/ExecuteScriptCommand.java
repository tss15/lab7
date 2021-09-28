package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.utils.SerializationManager;
import com.itmo.client.Client;
import com.itmo.utils.FieldsScanner;

import java.io.*;
import java.util.Scanner;

/**
 * The type Execute script command.
 */
public class ExecuteScriptCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     * @param args
     */
    public ExecuteScriptCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }

    @Override
    public void clientInsertion() {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            System.out.println("Run script " + args[0]);
            while ((line = reader.readLine()) != null) {
                Command c = Client.getCommandFromString(line);
                if (c != null) {
                    FieldsScanner scanner = FieldsScanner.getInstance();
                    scanner.configureScanner(new Scanner(reader));
                    c.clientInsertion();
                    scanner.configureScanner(new Scanner(System.in));
                    byte[] serializedCommand = SerializationManager.writeObject(c);
                    Client.sendOneByte();
                    Client.getSocket().getOutputStream().write(serializedCommand);
                    Client.getAnswer();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("kaputt");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (StackOverflowError e){
            System.out.println("Stack overflow!");
        }

    }

    @Override
    public String execute(Application application, User user) {
        return "script done";
    }

    @Override
    public String getDescription() {
        return "read and execute the script from the specified file. The script contains commands in the same form in which the user enters them interactively.";
    }
}
