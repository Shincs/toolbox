package dev.luzifer.chat;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// Absolutely unsecured, just for demonstration purposes
public class ChatController {
    
    private static final UUID uuid = UUID.randomUUID();
    
    private static Socket socket;
    private static Consumer<String> callback;
    
    private static volatile boolean isAlive = false;
    
    public static CompletableFuture<Boolean> connect(String host, int port) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                
                socket = new Socket(host, port);
                isAlive = true;
                
                startMessageReceiver();
                startHeartBeat();
                
                return true;
            } catch (IOException e) {
                return false;
            }
        });
    }
    
    public static void disconnect() {
        if(isConnected()) {
            try {
                socket.close();
                socket = null;
            } catch (Exception ignored) {
                // we dont care
            }
        }
    }
    
    public static void onMessage(Consumer<String> callback) {
        ChatController.callback = callback;
    }
    
    public static void chat(String message) {
        if(isConnected()) {
            try {
                String shortenedUUID = uuid.toString().substring(0, 5);
                socket.getOutputStream().write((shortenedUUID + ": " + message).getBytes());
            } catch (Exception ignored) {
                isAlive = false;
            }
        }
    }
    
    public static boolean isInitialized() {
        return socket != null;
    }
    
    public static boolean isConnected() {
        return isAlive;
    }
    
    private static void startMessageReceiver() {
        Thread receiver = new Thread(() -> {
            while(isAlive) {
                try {
                    if(socket.getInputStream().available() > 0) {
                        byte[] buffer = new byte[socket.getInputStream().available()];
                        socket.getInputStream().read(buffer);
                        
                        String message = new String(buffer);
                        System.out.println("Received message: " + message);
                        
                        if(callback != null)
                            callback.accept(message);
                    }
                } catch (IOException ignored) {
                    isAlive = false;
                }
            }
        });
        receiver.start();
    }
    
    private static void startHeartBeat() {
        Thread heartBeat = new Thread(() -> {
            while(isAlive) {
                try {
                    socket.getOutputStream().write(0);
                    Thread.sleep(1000);
                } catch (Exception ignored) {
                    isAlive = false;
                }
            }
        });
        heartBeat.start();
    }
    
}
