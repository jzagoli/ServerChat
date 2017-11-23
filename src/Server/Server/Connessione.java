package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connessione implements Runnable {
    private Socket socket;
    private Boolean run = true;
    private Accessi acc;

    public Connessione (Socket s,Accessi accessi){
        this.socket = s;
        this.acc = accessi;
    }

    @Override
    public void run() {
        try {
            BufferedReader lettore = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (run){
                String packet = lettore.readLine();
                String cod = packet.substring(0,1);
                String value = packet.substring(2);
                switch (cod) {
                    case "00":
                        login(value);
                        break;
                    case "10":
                        logout();
                        break;
                    case "20":

                        break;
                    case "30":

                        break;
                    case "31":

                        break;
                    case "40":

                        break;
                    case "50":

                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login (String value){
        Utente ut = new Utente(socket.getRemoteSocketAddress().toString(),value);
        acc.addUtente(ut);
        //TODO risposta
    }

    private void logout (){
        String ip = socket.getRemoteSocketAddress().toString();
        Utente toLogout = acc.getUtenteByIp(ip);
        acc.removeUtente(toLogout);
        //TODO risposta
    }

    public void stop (){
        this.run = false;
    }
}
