package Server;

import java.net.Socket;

public class Connessione implements Runnable {
    private Socket socket;

    public Connessione (Socket s){
        this.socket = s;
    }

    @Override
    public void run() {

    }
}
