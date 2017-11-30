package Server;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;

public class RefreshTextArea {
    private JTextArea Utenti, Connessi, Console;

    public RefreshTextArea(JTextArea utenti, JTextArea connessi, JTextArea console) throws FileNotFoundException {
        Utenti = utenti;
        Connessi = connessi;
        Console = console;
    }

    public void vai(Listener listener) {
        Thread rc = new Thread(new RefreshConnessi(listener));
        Thread ru = new Thread(new RefreshUtenti(listener));
        rc.start();
        ru.start();
        PrintStream printStream = new PrintStream(new StdoutToArea(Console));
        System.setOut(printStream);
        System.setErr(printStream);
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

    public class StdoutToArea extends OutputStream {
        private JTextArea textArea;

        public StdoutToArea(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char) b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
