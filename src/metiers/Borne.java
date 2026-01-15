package metiers;

public class Borne {
    private int idBorne;
    private int nbVoyageVendu;
    private int nbVentesTickets;
    // pr FK
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

        //  arret.addBorne(this);( avant) but need to supp aussi si y a un changement sinon borne ds plus arret

        // Si on avait déjà un arrêt, on demande à cet ancien arrêt de nous retirer de sa liste
        if (this.arret != null) {
            this.arret.getListeBornes().remove(this);
        }

        // On met à jour l'attribut avec le nouvel arrêt
        this.arret = arret;

        // On s'ajoute à la liste du nouvel arrêt
        if (arret != null) {
            arret.addBorne(this);
        }
    }

    public Borne(int idBorne,int nbVoyageVendu,int NbVentesTickets, Arret arret){
        this.idBorne=idBorne;
        this.nbVoyageVendu=nbVoyageVendu;
        this.nbVentesTickets=NbVentesTickets;
        this.setArret(arret); //garantit lien direct pcq on add via set
    }

    public Borne(int nbVoyageVendu,int NbVentesTickets,Arret arret){
        this.nbVoyageVendu=nbVoyageVendu;
        this.nbVentesTickets=NbVentesTickets;
        this.setArret(arret);
    }




}
