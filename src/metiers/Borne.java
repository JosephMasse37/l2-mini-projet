package metiers;

public class Borne {
    private int idBorne;
    private int nbVoyageVendu;
    private int nbVentesTickets;

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

    public int getNbVentesTickets() {
        return nbVentesTickets;
    }

    public void setNbVentesTickets(int NbVentesTickets) {
        this.nbVentesTickets = NbVentesTickets;
    }

    public Arret getArret() {
        return arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
        arret.addBorne(this);
    }

    public Borne(){}

    public Borne(int idBorne,int nbVoyageVendu,int NbVentesTickets){
        this.idBorne=idBorne;
        this.nbVoyageVendu=nbVoyageVendu;
        this.nbVentesTickets=NbVentesTickets;
    }

    public Borne(int nbVoyageVendu,int NbVentesTickets){
        this.nbVoyageVendu=nbVoyageVendu;
        this.nbVentesTickets=NbVentesTickets;
    }



}
