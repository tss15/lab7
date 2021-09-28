package com.itmo.server;

import ch.qos.logback.classic.Logger;
import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.utils.SerializationManager;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

    @AllArgsConstructor
public class GiveResponseTask implements Runnable {
    private SocketChannel channel;
    private Application application;
    private User client;
    private String ans;

    public final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(GiveResponseTask.class);
    @Override
    public void run() {
        sendResponse(ans, channel);
    }

    //•	Module for sending responses to the client.
    private void sendResponse(String commandResult, SocketChannel channel){
        Response response = new Response(commandResult);
        try {
            byte[] ans = SerializationManager.writeObject(response);
            log.info("response serialized");
            ByteBuffer buffer = ByteBuffer.wrap(ans);
            int given = channel.write(buffer);
            log.info("sended response: " + given + " bytes");
        } catch (IOException e) {
            log.error("Error while serialization response");
        }
    }
}
