package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
    public static void main(String[] args) throws IOException {
        Boolean run = true;
        ServerSocket serverSocket=new ServerSocket(4242);

        while (run){
            Socket socket = serverSocket.accept();
            Thread connessione = new Thread(new Connessione(socket));
            connessione.run();
        }
    }
}
