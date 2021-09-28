package com.itmo.client;

public class ClientMain {
    public static void main(String[] args) {
        if(args.length!=1){
            System.out.println("Specify the port in the arguments (8080)");
            System.exit(1);
        }else{
            int port = 8080;
            try{
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Not an integer");
            }
            Client client = new Client("localhost", port);
            client.run();
        }
    }
}
