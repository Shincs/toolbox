package dev.luzifer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    
    private static final Set<Socket> CLIENTS = new HashSet<>();
    
    private static final int PORT = 8000;
    
    public void start() {
        
        System.out.println("Starting server...");
    
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            
            System.out.println("Server started on port " + PORT);
            
            while(true) {
                
                Socket client = serverSocket.accept();
                synchronized (CLIENTS) {
                    CLIENTS.add(client);
                }
                
                System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
                
                new Thread(() -> {
                    try {
                        while(true) {
                            
                            byte[] buffer = new byte[1024];
                            int read = client.getInputStream().read(buffer);
                            
                            if(read == -1) {
                                
                                synchronized (CLIENTS) {
                                    CLIENTS.remove(client);
                                }
                                
                                client.close();
                                System.out.println("Client died: " + client.getInetAddress().getHostAddress());
                                
                                break;
                            }
                            
                            if(read == 1 && buffer[0] == 0) {
                                
                                hasSentHeartbeat(client);
                                
                                for(Iterator<Socket> iterator = CLIENTS.iterator(); iterator.hasNext();) {
                                    Socket otherClient = iterator.next();
                                    
                                    if(otherClient == client)
                                        continue;
                                    
                                    if(!isAlive(otherClient)) {
    
                                        synchronized (CLIENTS) {
                                            iterator.remove();
                                        }
                                        otherClient.close();
                                        
                                        System.out.println("Client died: " + otherClient.getInetAddress().getHostAddress());
                                        continue;
                                    }
                                    
                                    otherClient.getOutputStream().write(buffer);
                                }
                                
                                continue;
                            }
                            
                            String message = new String(buffer, 0, read);
                            System.out.println(message);
                            
                            CLIENTS.forEach(c -> {
                                try {
                                    c.getOutputStream().write(message.getBytes());
                                } catch (IOException e) {
                                    synchronized (CLIENTS) {
                                        CLIENTS.remove(c);
                                    }
                                    try {
                                        c.close();
                                    } catch (IOException ignored) {}
                                }
                            });
                        }
                    } catch (IOException ignored) {}
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Server crashed.. Printing stacktrace:");
            throw new RuntimeException(e);
        }
        
    }
    
    private final Map<Socket, Timestamp> heartbeatTracker = new HashMap<>();
    
    private void hasSentHeartbeat(Socket socket) {
        heartbeatTracker.put(socket, new Timestamp(System.currentTimeMillis()));
    }
    
    private boolean isAlive(Socket socket) {
        return heartbeatTracker.get(socket).getTime() + 5000 > System.currentTimeMillis();
    }
}
