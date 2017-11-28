package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Listener  {

    private static Boolean run = true;
    private static ServerSocket serverSocket;
    private static Accessi acc;
    private static HashMap<String, Socket> socketmap = new HashMap<>();

    public static void main(String[] args) throws  IOException{

        System.out.println("Server Chat Partito");

        serverSocket = new ServerSocket(4242);
        acc = new Accessi();
        while (run){
            Socket socket = serverSocket.accept();
            socketmap.put(socket.getRemoteSocketAddress().toString(), socket);
            Thread connessione = new Thread(new Connessione(socket, acc, socketmap));
            connessione.start();
            System.out.println("Connesso:" + socket.getRemoteSocketAddress().toString());
        }
    }

    public void stop () throws IOException {
        this.run = false;
        serverSocket.close();
        for (Socket s : socketmap.values()) {
            s.close();
        }
    }
}
