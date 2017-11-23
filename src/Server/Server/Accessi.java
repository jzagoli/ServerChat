package Server;

import java.util.ArrayList;

public class  Accessi{
    private ArrayList<Utente> listautenti;

    public Accessi() {
        listautenti = new ArrayList<>();
    }

    public ArrayList<Utente> getListautenti() {
        return listautenti;
    }

    public void addUtente (Utente u){
        listautenti.add(u);
    }

    public void removeUtente (Utente u){
        listautenti.remove(u);
    }
}

