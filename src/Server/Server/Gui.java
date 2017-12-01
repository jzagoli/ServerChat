package Server;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.awt.Color.gray;

public class Gui {
    private JPanel JPanel;
    private JLabel Titolo;
    private JTextArea Utenti;
    private JButton Stop;
    private JButton Start;
    private JTextArea Connessi;
    private JScrollPane Console;
    private JTextArea Consoletext;
    private JLabel labelLoggati;
    private JLabel labelConnessi;

    public Gui(Listener listener) throws FileNotFoundException {
        Utenti.setBorder(BorderFactory.createLineBorder(gray));
        Connessi.setBorder(BorderFactory.createLineBorder(gray));
        RefreshTextArea refreshTextArea = new RefreshTextArea(Utenti, Connessi, Consoletext);
        refreshTextArea.vai(listener);
        Stop.setEnabled(false);
        //la cosa dell'array di thread me l'ha fatta intellij sennò non andava
        final Thread[] t = new Thread[1];
        Start.addActionListener(actionEvent -> {
            t[0] = new Thread(listener);
            t[0].start();
            Start.setEnabled(false);
            Stop.setEnabled(true);}
            );
        Stop.addActionListener(actionEvent -> {
            try {
                listener.stop();
                t[0].join();
                Start.setEnabled(true);
                Stop.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Gui");
        Listener listener = new Listener();
        frame.setContentPane(new Gui(listener).JPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(580, 850);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
