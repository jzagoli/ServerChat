package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connessione implements Runnable {
    private Socket socket;
    private Boolean run = true;

    public Connessione (Socket s){
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader lettore = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (run){
                String packet = lettore.readLine();
                String cod = packet.substring(0,1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop (){
        this.run = false;
    }
}
