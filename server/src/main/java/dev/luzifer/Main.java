package dev.luzifer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    private static final List<Socket> clients = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
    
        ServerSocket serverSocket = new ServerSocket(1337);
        
        Thread messageReceiveThread = new Thread(() -> {
            String inLine;
            while (true) {
                for (Socket client : clients) {
                    try {
                        if (client.getInputStream().available() > 0) {
                            
                            byte[] bytes = new byte[client.getInputStream().available()];
                            client.getInputStream().read(bytes);
                            
                            String message = new String(bytes);
                            System.out.println("Received message: " + message);
                            
                            for (Socket client1 : clients)
                                client1.getOutputStream().write(message.getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        messageReceiveThread.setDaemon(true);
        messageReceiveThread.start();
        
        while (true) {
            Socket client = serverSocket.accept();
            clients.add(client);
        }
    }
}
