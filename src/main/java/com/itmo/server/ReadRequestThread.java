package com.itmo.server;

import ch.qos.logback.classic.Logger;
import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.commands.Command;
import com.itmo.commands.ExitCommand;
import com.itmo.utils.SerializationManager;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

//	For multithreaded reading of requests

@AllArgsConstructor
public class ReadRequestThread extends Thread {
    private SocketChannel channel;
    private Application application;
    private User user;


    private final int DEFAULT_BUFFER_SIZE = 4096;
    private final ByteBuffer b = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    public final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ReadRequestThread.class);
    @Override
    public void run() {

        while(true){
            Command command = null;
            while(command==null) command = getCommandFromClient(channel);
            new Thread(new RequestExecutorThread(command, channel, application, command.getUser())).start();
            if(command instanceof ExitCommand) break;
        }
    }

    private Command getCommandFromClient(SocketChannel channel) {
        try {
            if(checkOneByte(channel)) {
                Command command;
                int got;
                while (b.position() == 1) {
                    got = channel.read(b);
                    if (got != 0) log.info("bytes received:" + got);
                }
                if (b.remaining() != 0) {
                    command = SerializationManager.readObject(Arrays.copyOfRange(b.array(),
                            1, b.array().length - 1));
                    log.info("Received command: " + command.toString());
                    b.clear();
                    return command;
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            log.error("Error while serialization");
            return null;
        }catch (StreamCorruptedException e ){ // when trying to deserialize an object that was not fully passed
            System.out.println(e.getLocalizedMessage());
            return null;
        } catch(IOException e) {
            return null;
        }
    }

    private boolean checkOneByte(SocketChannel channel) throws IOException {
        int bytesFromClient = 0;
        if(channel!=null) bytesFromClient = channel.read(b);
        return (bytesFromClient==1);
    }
}
