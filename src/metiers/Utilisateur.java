package metiers;

public class Utilisateur {
    private String username;
    private String password;
    private String prenom;
    private String nom;
    // pr FK
    private Chauffeur chauffeur;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;

        if (chauffeur.getUtilisateur() != this || chauffeur.getUtilisateur() == null) {
            chauffeur.setUtilisateur(this);
        }
    }

    public Utilisateur(String username, String password, String prenom, String nom) {
        this.nom = nom;
        this.username = username;
        this.password = password;
        this.prenom = prenom;
    }

    public Utilisateur(String username, String password, String prenom, String nom, Chauffeur chauffeur) {
        this.username = username;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
        this.chauffeur = chauffeur;
    }

    @Override
    public String toString() {
        return "USERNAME " + username + " | Identité : " + prenom + " " + nom;
    }
}
