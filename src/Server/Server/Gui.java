package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Gui {
    private JPanel JPanel;
    private JLabel Titolo;
    private JTextArea Utenti;
    private JButton Stop;
    private JButton Start;
    private JTextArea Connessi;

    public Gui(Listener listener) {
        Thread t = new Thread(listener);
        Start.addActionListener(actionEvent -> {
            t.start();
        });
        Stop.addActionListener(actionEvent -> {
            try {
                listener.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Gui");
        Listener listener = new Listener();
        Thread rc = new Thread(new RefreshConnessi(listener));
        Thread ru = new Thread(new RefreshUtenti(listener));
        rc.start();
        ru.start();
        frame.setContentPane(new Gui(listener).JPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(570, 700);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private static class RefreshUtenti implements Runnable {

        Listener l;
        Accessi a;

        public RefreshUtenti(Listener l) {
            this.l = l;
            a = l.getAcc();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2);
                String s = "";
                for (Utente u : a.getListautenti()) {
                    s += u.getNomeUt() + "\n";
                }
                //Utenti.setText(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RefreshConnessi implements Runnable {

        Listener l;
        HashMap<String, Socket> map;

        public RefreshConnessi(Listener l) {
            this.l = l;
            map = l.getSocketmap();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2);
                String s = "";
                for (Socket socket : map.values()) {
                    s += socket.getRemoteSocketAddress().toString() + "\n";
                }
                //Utenti.setText(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
