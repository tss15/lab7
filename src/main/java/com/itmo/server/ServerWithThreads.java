package com.itmo.server;

import com.itmo.app.Application;
import com.itmo.client.User;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerWithThreads {
    private ServerSocketChannel ssc;
    private final Logger log;
    private Application application;
    private boolean serverOn = true;
    private final int port;
//    public static ExecutorService executorService = Executors.newCachedThreadPool();
//    public static ForkJoinPool pool = ForkJoinPool.commonPool();
    public static ForkJoinPool pool = new ForkJoinPool();

    public void run(Application application) {
        log.info("Server start, port: " + port);
        setupNet();
        SocketChannel clientSocketChannel = null;

        while (serverOn) {
            checkServerCommand();
            try{
                clientSocketChannel = accept();
                if(clientSocketChannel!=null){
                    User newUser = new User("unregistered", "");
                    new Thread(new ReadRequestThread(clientSocketChannel, application, newUser)).start();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        closeEverything();
        System.exit(0);
    }


    private void closeEverything() {
        try {
            if (ssc != null) ssc.close();
        } catch (IOException ignored) {
        }
        ssc = null;
    }

    public SocketChannel accept() throws IOException {
        SocketChannel channel = ssc.accept();
        if (channel != null) {
            log.info("accepted client");
            try {
                channel.configureBlocking(false);
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                return channel;
            } catch (IOException e) {
                log.error("Unable to accept channel");
            }
        }
        return null;
    }

    public void setupNet(){
        ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
            ssc.socket().bind(inetSocketAddress);
        } catch (IOException e) {
            System.out.println("The server may already be running ...");
        }
    }

    public ServerWithThreads(int port, Logger log){
        this.port = port;
        this.log = log;
    }

    private void checkServerCommand() {
        try {
            if(System.in.available()>0){
                String[] line;
                String line1;
                Scanner scanner = new Scanner(System.in);
                if ((line1 = scanner.nextLine()) != null) {
                    line = line1.trim().split(" ");
                    if (line[0].equals("exit") && line.length == 1) {
                        log.info("exiting");
                        scanner.close();
                        serverOn = false;
                    } else {
                        log.info("no such command");
                    }
                }
            }
        } catch (NoSuchElementException | IOException ignored) {
        }
    }
}