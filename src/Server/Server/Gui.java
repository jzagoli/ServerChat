package Server;

import javax.swing.*;
import java.io.IOException;

public class Gui {
    private JPanel JPanel;
    private JLabel Titolo;
    private JTextArea Utenti;
    private JButton Stop;
    private JButton Start;
    private JTextArea Connessi;
    private Boolean listenerStarted = false;

    public Gui(Listener listener) {
        RefreshTextArea refreshTextArea = new RefreshTextArea(Utenti, Connessi);
        refreshTextArea.vai(listener);
        Thread t = new Thread(listener);
        Start.addActionListener(actionEvent -> {
            if (!listenerStarted) t.start();
            listenerStarted = true;
        });
        Stop.addActionListener(actionEvent -> {
            try {
                if (listenerStarted) listener.stop();
                listenerStarted = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Gui");
        Listener listener = new Listener();
        frame.setContentPane(new Gui(listener).JPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(580, 750);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}