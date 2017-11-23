package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener  {

    static Boolean  run = true;
    static ServerSocket serverSocket;
    static Accessi acc;

    public static void main(String[] args) throws  IOException{
        serverSocket = new ServerSocket(4242);
        while (run){
            Socket socket = serverSocket.accept();
            Thread connessione = new Thread(new Connessione(socket,acc));
            connessione.start();
        }
    }

    public void stop () throws IOException {
        this.run = false;
        serverSocket.close();
    }
}
