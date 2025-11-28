package metiers;

public class Abonnement {
    private int idAbonnement;
    private String formule;
    private LocalDate dateDebut;
    private double prix;

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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
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
}
