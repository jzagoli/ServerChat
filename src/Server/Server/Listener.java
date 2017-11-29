package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Listener implements Runnable {

    private static Boolean run = true;
    private static ServerSocket serverSocket;
    private static Accessi acc = new Accessi();
    private static HashMap<String, Socket> socketmap = new HashMap<>();

    Listener() {
    }

    static Accessi getAcc() {
        return acc;
    }

    static HashMap<String, Socket> getSocketmap() {
        return socketmap;
    }

    public void run() {
        System.out.println("Server Chat Partito");
        try {
            serverSocket = new ServerSocket(4242);
            while (run) {
                Socket socket = serverSocket.accept();
                socketmap.put(socket.getRemoteSocketAddress().toString(), socket);
                Thread connessione = new Thread(new Connessione(socket, acc, socketmap));
                connessione.start();
                System.out.println("Connesso:" + socket.getRemoteSocketAddress().toString());
            }
        } catch (IOException e) {
            System.out.println("Server Chat Fermato");
        }
    }

    void stop() throws IOException {
        run = false;
        for (Socket s : socketmap.values()) {
            s.close();
        }
        serverSocket.close();
    }
}
