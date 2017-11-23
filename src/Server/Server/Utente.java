package Server;

import java.util.ArrayList;

public class Utente {

    private String ip;
    private String nomeUt;
    private ArrayList<String> ipBan;

    public Utente() {
        this.ip = "";
        this.nomeUt = "";
        this.ipBan = null;
    }

    public Utente(String ip, String nomeUt){
        this.ip = ip;
        this.nomeUt = nomeUt;
        this.ipBan = new ArrayList<>();
    }

    public String getIp() {
        return this.ip;
    }

    public String getNomeUt() {
        return this.nomeUt;
    }

    public ArrayList<String> getIpBan() {
        return this.ipBan;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setNomeUt(String nomeUt) {
        this.nomeUt = nomeUt;
    }

    public void setIpBan(ArrayList<String> ipBan) {
        this.ipBan = ipBan;
    }

    public void addBan(String ip){
        ipBan.add(ip);
    }

    public void removeBan(String ip){
        ipBan.remove(ip);
    }

}
