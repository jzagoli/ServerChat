package Server;

import java.io.*;
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
            BufferedWriter scrittore = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (run){
                String packet = lettore.readLine();
                String cod = packet.substring(0,1);
                String value = packet.substring(2);
                switch (cod) {
                    case "00":
                        login(value,scrittore);
                        break;
                    case "10":
                        logout(scrittore);
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

    private void login (String value, BufferedWriter scrittore) throws IOException {
        Utente ut = new Utente(socket.getRemoteSocketAddress().toString(),value);
        acc.addUtente(ut);
        //risposta
        String name = acc.getUtenteByIp(socket.getRemoteSocketAddress().toString()).getNomeUt();
        scrittore.write("01"+name);
    }

    private void logout (BufferedWriter scrittore) throws IOException {
        String ip = socket.getRemoteSocketAddress().toString();
        Utente toLogout = acc.getUtenteByIp(ip);
        acc.removeUtente(toLogout);
        //risposta
        scrittore.write("10");
    }

    private void ban(String value){
        Utente u1=acc.getUtenteByName(value);
        //u1.addBan();
    }

    public void stop (){
        this.run = false;
    }
}
