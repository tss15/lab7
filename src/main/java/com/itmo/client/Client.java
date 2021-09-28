package com.itmo.client;
import com.itmo.Exceptions.NoSuchCommandException;
import com.itmo.Exceptions.WrongArgumentsNumberException;
import com.itmo.utils.SerializationManager;
import com.itmo.commands.*;
import com.itmo.server.Response;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static CommandsInvoker invoker;
    private static Socket socket;
    public static final int BUFFER_SIZE = 4096;
    private static boolean notExit = true;
    private static final BufferedReader systemIn
            = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private static final Scanner scanner = new Scanner(System.in);
    private final String host;
    private final int port;
    private boolean once = true;
    private static User user;
    public void run() {
        registerCommands();
        connect();
        while (notExit) {
            handshake();
        }
        scanner.close();
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
    public Client(String host, int port){
        this.host = host;
        this.port = port;
        user = new User("unregistered", "");
    }

    public static void sendOneByte() throws IOException {
        byte[] b = new byte[1];
        b[0] = (byte) 127;
        socket.getOutputStream().write(b);
    }

    public void handshake(){
        if(socket==null) {
            System.out.println("socket==null");
            return;
        }
        try{
            Command command = scanCommandFromConsole();
            command.setUser(user);
            sendOneByte();
            sendCommand(command);
            getAnswer();
            if(command instanceof ExitCommand) {
                notExit = false;
                System.exit(0);
            }
        } catch (IOException e) {
            if(notExit){
                System.out.println("Lost connection");
                connect();
            }
        }catch (ClassNotFoundException e){
            System.out.println("Error during synchronization");
        }
    }

    private Command scanCommandFromConsole() throws IOException {
        Command command = null;
        String line;
        do{
            if((line = systemIn.readLine()) != null){
                command = getCommandFromString(line);
            }
        }while(command==null);
        command.clientInsertion();
        return command;
    }

    public static void getAnswer() throws IOException, ClassNotFoundException {
        byte[] buff = new byte[BUFFER_SIZE];
        int got = socket.getInputStream().read(buff);
        if(got>0){
            Response r = SerializationManager.readObject(buff);
            String ans = r.getAnswer();
            if(ans.startsWith("Hello, ")){
                user.setName(ans.trim().substring(7));
            }
            System.out.println(ans);
        }
    }


    public static void sendCommand(Command command) throws IOException {
        byte[] serializedCommand = SerializationManager.writeObject(command);

        socket.getOutputStream().write(serializedCommand);
    }

    public static Command getCommandFromString(String command){
        String[] splitted = command.split(" ");
        String commandName = splitted[0];
        String[] arguments = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arguments, 0, splitted.length - 1);
        try {
            return invoker.validateCommand(commandName, arguments);
        } catch (WrongArgumentsNumberException | NoSuchCommandException e) {
            return null;
        }
    }

    private void connect()  {
        System.out.println("Trying to establish a connection with the server");
        while (true) {
            try {
                InetAddress addr = InetAddress.getByName(host);
                socket = new Socket(addr, port);
                System.out.println("Connected: " + socket);
                once=true;
                return;
            } catch (UnknownHostException e) {
                if(once){
                    once=false;
                    System.out.println("Wrong host specified");
                }
            } catch (IOException e) {
                if(once){
                    once=false;
                    System.out.println("Server disconnected");
                }
            }
        }
    }

    private void registerCommands(){
        invoker = CommandsInvoker.getInstance();
        invoker.register("info", new InfoCommand( null));
        invoker.register("help", new HelpCommand(null));
        invoker.register("exit", new ExitCommand(null));
        invoker.register("clear", new ClearCommand(null));
        invoker.register("remove_by_id", new RemoveByIdCommand(null));
        invoker.register("add", new AddElementCommand(null));
        invoker.register("show", new ShowCommand(null));
        invoker.register("add", new AddElementCommand(null));
        invoker.register("update", new UpdateIdCommand(null));
        invoker.register("filter_starts_with_name", new FilterStartsWithNameCommand(null));
        invoker.register("add_if_max", new AddIfMaxCommand(null));
        invoker.register("add_if_min", new AddIfMinCommand(null));
        invoker.register("remove_lower", new RemoveLowerThanElementCommand(null));
        invoker.register("print_field_ascending_minimalPoint", new PrintFieldAscendingMinimalPointCommand(null));
        invoker.register("print_descending", new PrintDescendingCommand(null));
        //invoker.register("save", new SaveCommand(mainReceiver, null));
        invoker.register("execute_script", new ExecuteScriptCommand(null));
        invoker.register("login", new LoginCommand());
        invoker.register("register", new RegisterCommand());
    }

    public static Socket getSocket() {
        return socket;
    }
}