package metiers;

import java.util.ArrayList;
import java.util.List;

public class Abonnement {
    private int idAbonnement;
    private String formule;
    private double prix;
    private String duree;

    private List<Client> listeClients = new ArrayList<>();

    public int getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<Client> getListeClients() {
        return listeClients;
    }

    public void setListeClients(List<Client> listeClients) {
        this.listeClients = listeClients;
    }

    public void addClient(Client client) {
        this.listeClients.add(client);
    }

    public Abonnement(int idAbonnement, String formule, double prix, String duree, List<Client> listeClients) {
        this.idAbonnement = idAbonnement;
        this.formule = formule;
        this.duree = duree;;
        this.prix = prix;
        this.listeClients = listeClients;
    }

    public Abonnement(int idAbonnement, String formule, double prix, String duree) {
        this.idAbonnement = idAbonnement;
        this.formule = formule;
        this.duree = duree;
        this.prix = prix;
        this.listeClients = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Abonnement [idAbonnement=" + idAbonnement + ", formule=" + formule + ", prix=" + prix + ", duree="
                + duree + ", listeClients=" + listeClients + "]";
    }
}
