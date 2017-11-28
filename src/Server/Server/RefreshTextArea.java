package Server;

import javax.swing.*;
import java.net.Socket;
import java.util.HashMap;

public class RefreshTextArea {
    private JTextArea Utenti, Connessi;

    public RefreshTextArea(JTextArea utenti, JTextArea connessi) {
        Utenti = utenti;
        Connessi = connessi;
    }

    public void vai(Listener listener) {
        Thread rc = new Thread(new RefreshConnessi(listener));
        Thread ru = new Thread(new RefreshUtenti(listener));
        rc.start();
        ru.start();
    }

    private class RefreshUtenti implements Runnable {

        Listener l;
        Accessi a;

        public RefreshUtenti(Listener l) {
            this.l = l;
            a = l.getAcc();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);
                    String s = "";
                    for (Utente u : a.getListautenti()) {
                        s += u.getNomeUt() + "---->" + u.getIp() + "\n";
                    }
                    Utenti.setText(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RefreshConnessi implements Runnable {

        Listener l;
        HashMap<String, Socket> map;

        public RefreshConnessi(Listener l) {
            this.l = l;
            map = l.getSocketmap();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);
                    String s = "";
                    for (Socket socket : map.values()) {
                        s += socket.getRemoteSocketAddress().toString() + "\n";
                    }
                    Connessi.setText(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
