package dev.luzifer.chat;

import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatController {
    
    private static final UUID uuid = UUID.randomUUID();
    
    private static Socket socket;
    private static Consumer<String> callback;
    
    public static void connect(String host, int port) {
        
        if(socket == null) {
            
            try {
                socket = new Socket(host, port);
                socket.getOutputStream().write(uuid.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            startMessageReceiver();
        } else {
            System.out.println("Already connected!");
        }
    }
    
    public static void disconnect() {
        
        if(socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Not connected!");
        }
    }
    
    public static void onMessage(Consumer<String> callback) {
        ChatController.callback = callback;
    }
    
    public static void chat(String message) {
    
        if(socket != null) {
            try {
                socket.getOutputStream().write((uuid.toString() + ": " + message).getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Not connected!");
        }
    }
    
    public static boolean isConnected() {
        return socket != null;
    }
    
    private static void startMessageReceiver() {
        Thread receiver = new Thread(() -> {
            while(socket != null && socket.isConnected()) {
                try {
                    byte[] buffer = new byte[1024];
                    int read = socket.getInputStream().read(buffer);
                    
                    if(read > 0) {
                        String message = new String(buffer, 0, read);
                        if(callback != null) {
                            callback.accept(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.setDaemon(true);
        receiver.start();
    }
    
}
