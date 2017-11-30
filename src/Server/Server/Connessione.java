package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Connessione implements Runnable {
    private Socket socket;
    private Boolean run = true;
    private Accessi acc;
    private HashMap<String, Socket> socketMap;

    Connessione(Socket s, Accessi accessi, HashMap<String, Socket> socketMap) {
        this.socket = s;
        this.acc = accessi;
        this.socketMap = socketMap;
    }

    @Override
    public void run() {
        String ip = socket.getRemoteSocketAddress().toString();
        Utente questoUt = null;
        try {
            BufferedReader lettore = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter scrittore = new PrintWriter(socket.getOutputStream(), true);
            while (run){
                String packet = lettore.readLine();
                String cod = packet.substring(0, 2);
                String value = packet.substring(2);
                switch (cod) {
                    case "00":
                        login(value,scrittore);
                        questoUt = acc.getUtenteByIp(ip);
                        break;
                    case "10":
                        logout(scrittore);
                        break;
                    case "20":
                        if (acc.isLogged(ip)) {
                            listaUtenti(scrittore);
                        } else {
                            erroreUtNonLoggato(scrittore);
                        }
                        break;
                    case "30":
                        if (acc.isLogged(ip)) {
                            ban(value);
                        } else {
                            erroreUtNonLoggato(scrittore);
                        }
                        break;
                    case "31":
                        if (acc.isLogged(ip)) {
                            unban(value);
                        } else {
                            erroreUtNonLoggato(scrittore);
                        }
                        break;
                    case "40":
                        if (acc.isLogged(ip)) {
                            mexSingolo(value, scrittore);
                        } else {
                            erroreUtNonLoggato(scrittore);
                        }
                        break;
                    case "50":
                        if (acc.isLogged(ip)) {
                            mexBroadcast(value, scrittore);
                        } else {
                            erroreUtNonLoggato(scrittore);
                        }
                        break;
                }
            }
        } catch (SocketException se) {
            if (se.getMessage().equals("Connection reset")) {
                acc.removeUtente(questoUt);
                socketMap.remove(ip);
                this.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("chiusa connessione con:" + ip);

    }

    private void login(String value, PrintWriter scrittore) throws IOException {
        if (!value.contains(";")||!value.contains("FINE")) {
            Utente ut = new Utente(socket.getRemoteSocketAddress().toString(), value);
            acc.addUtente(ut);
            //risposta cercando il nome
            String name = acc.getUtenteByIp(socket.getRemoteSocketAddress().toString()).getNomeUt();
            scrittore.write("01" + name);
            scrittore.flush();
            System.out.println("login:" + socket.getRemoteSocketAddress().toString() + "|" + value);
        } else {
            this.erroreNomeErrato(scrittore);
        }
    }

    private void logout(PrintWriter scrittore) throws IOException {
        String ip = socket.getRemoteSocketAddress().toString();
        Utente toLogout = acc.getUtenteByIp(ip);
        acc.removeUtente(toLogout);
        //risposta di logout
        if (!acc.isLogged(ip)) {
            scrittore.write("11");
            scrittore.flush();
        }
        System.out.println("logout:" + ip);
    }

    private void ban(String value) throws IOException {
        //aggiungo il mio ip nella lista di persone che l'utente da bannare non può contattare
        String mioip = socket.getRemoteSocketAddress().toString();
        acc.getUtenteByName(value).addBan(mioip);
        String nomeUtBannato = acc.getUtenteByIp(value).getNomeUt();
        String ack = "32" + nomeUtBannato + ";1";
        //invio l'ack all'utente bannato
        Socket socketUtBannato = socketMap.get(value);
        PrintWriter scrittoreUtBannato = new PrintWriter(socketUtBannato.getOutputStream(), true);
        scrittoreUtBannato.write(ack);
        scrittoreUtBannato.flush();
        System.out.println(acc.getUtenteByIp(socket.getRemoteSocketAddress().toString()).getNomeUt() + " ha bannato " + acc.getUtenteByIp(value).getNomeUt());
    }

    private void unban(String value) throws IOException {
        String mioip = socket.getRemoteSocketAddress().toString();
        acc.getUtenteByName(value).removeBan(mioip);
        String nomeUtSbannato = acc.getUtenteByIp(value).getNomeUt();
        String ack = "32" + nomeUtSbannato + ";0";
        //invio l'ack all'utente sbannato
        Socket socketUtSbannato = socketMap.get(value);
        PrintWriter scrittoreUtSbannato = new PrintWriter(socketUtSbannato.getOutputStream(), true);
        scrittoreUtSbannato.write(ack);
        scrittoreUtSbannato.flush();
        System.out.println(acc.getUtenteByIp(socket.getRemoteSocketAddress().toString()).getNomeUt() + " ha sbannato " + acc.getUtenteByIp(value).getNomeUt());
    }

    private void listaUtenti(PrintWriter scrittore) throws IOException {
        ArrayList<Utente> list = acc.getListautenti();
        for (Utente u : list) {
            scrittore.write("21" + u.getNomeUt());
            scrittore.flush();
        }
        scrittore.write("21FINE");
        scrittore.flush();
        System.out.println("inviata lista utenti a "+ socket.getRemoteSocketAddress().toString());
    }

    private void mexSingolo(String value, PrintWriter scrittore) throws IOException {
        String[] packetvalue = value.split(";");
        Utente mioUt = acc.getUtenteByIp(socket.getRemoteSocketAddress().toString());
        Utente utDaContattare = acc.getUtenteByName(packetvalue[0]);
        if (mioUt.canContact(utDaContattare)) {
            Socket socketUtDaContattare = socketMap.get(utDaContattare.getIp());
            PrintWriter scrittoreUtDaContattare = new PrintWriter(socketUtDaContattare.getOutputStream(), true);
            scrittoreUtDaContattare.write("81" + mioUt.getNomeUt() + ";" + packetvalue[1]);
            scrittoreUtDaContattare.flush();
            System.out.println("Inviato messaggio da " + mioUt.getNomeUt() + " a " + utDaContattare.getNomeUt());
        } else {
            erroreSeiBannato(scrittore);
        }
    }

    private void mexBroadcast(String value, PrintWriter scrittore) throws IOException {
        String[] packetvalue = value.split(";");
        Utente mioUt = acc.getUtenteByIp(socket.getRemoteSocketAddress().toString());
        List<Utente> listUtDaContattare = acc.getListautenti();
        for (Utente u : listUtDaContattare) {
            if (mioUt.canContact(u)) {
                Socket socketUtDaContattare = socketMap.get(u.getIp());
                PrintWriter scrittoreUtDaContattare = new PrintWriter(socketUtDaContattare.getOutputStream(), true);
                scrittoreUtDaContattare.write("82" + mioUt.getNomeUt() + ";" + packetvalue[1]);
                scrittoreUtDaContattare.flush();
                System.out.println("Inviato messaggio broadcast da " + mioUt.getNomeUt() + " a " + u.getNomeUt());
            } else {
                erroreSeiBannato(scrittore);
            }
        }
    }

    private void erroreUtNonLoggato(PrintWriter scrittore) throws IOException {
        scrittore.write("02NON LOGGATO");
        scrittore.flush();
        System.out.println("Errore ut non loggato :"+socket.getRemoteSocketAddress().toString());
    }

    private void erroreNomeErrato(PrintWriter scrittore) throws IOException {
        scrittore.write("02LOGIN ERRATO");
        scrittore.flush();
        System.out.println("Errore nome errato:"+socket.getRemoteSocketAddress().toString());
    }

    private void erroreSeiBannato(PrintWriter scrittore) throws IOException {
        scrittore.write("02BANNATO");
        scrittore.flush();
        System.out.println("Errore utente bannato:" + socket.getRemoteSocketAddress().toString());
    }

    private void stop() {
        this.run = false;
    }
}