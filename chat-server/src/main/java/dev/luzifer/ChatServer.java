package dev.luzifer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    
    private static final Set<Socket> CLIENTS = new HashSet<>();
    
    private static final int PORT = 8000;
    
    public void start() {
    
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Thread clientHandler = new Thread(() -> {
            while(true) {
                for(Socket client : CLIENTS) {
                    try {
                        if(client.getInputStream().available() > 0) {
                            byte[] buffer = new byte[client.getInputStream().available()];
                            client.getInputStream().read(buffer);
                            
                            String message = new String(buffer);
                            System.out.println("Received message: " + message);
                            
                            for(Socket otherClient : CLIENTS) {
                                otherClient.getOutputStream().write(message.getBytes());
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        clientHandler.setDaemon(true);
        clientHandler.start();
        
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                CLIENTS.add(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
