package metiers;

public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private int age;
    private Abonnement unAbonnement;

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Abonnement getUnAbonnement() {
        return unAbonnement;
    }

    public void setUnAbonnement(Abonnement unAbonnement) {
        this.unAbonnement = unAbonnement;
        unAbonnement.addClient(this);
    }

    public Client(int idClient, String nom, String prenom, int age) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age; 
    }

    public Client(int idClient, String nom, String prenom, int age, Abonnement unAbonnement) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.unAbonnement = unAbonnement;
    }

    @Override
    public String toString() {
        return "Client [idClient=" + idClient + ", nom=" + nom + ", prenom=" + prenom + ", age=" + age
                + ", unAbonnement=" + unAbonnement + "]";
    }
}
