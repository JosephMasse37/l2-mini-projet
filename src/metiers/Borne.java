package metiers;

public class Borne {
    private int idBorne;
    private int nbVoyageVendu;
    private int getNbVentesTickets;

    private Arret arret;

    public int getIdBorne() {
        return idBorne;
    }

    public void setIdBorne(int idBorne) {
        this.idBorne = idBorne;
    }

    public int getNbVoyageVendu() {
        return nbVoyageVendu;
    }

    public void setNbVoyageVendu(int nbVoyageVendu) {
        this.nbVoyageVendu = nbVoyageVendu;
    }

    public int getGetNbVentesTickets() {
        return getNbVentesTickets;
    }

    public void setGetNbVentesTickets(int getNbVentesTickets) {
        this.getNbVentesTickets = getNbVentesTickets;
    }

    public Arret getArret() {
        return arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
        arret.addBorne(this);
    }
}
