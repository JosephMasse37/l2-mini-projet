package metiers;

public class Chauffeur {
    private int idChauffeur ;
    private boolean formation_tram;

    private Utilisateur utilisateur;

    public int getIdChauffeur() {
        return idChauffeur;
    }

    public void setIdChauffeur(int idChauffeur) {
        this.idChauffeur = idChauffeur;
    }

    public boolean isFormation_tram() {
        return formation_tram;
    }

    public void setFormation_tram(boolean formation_tram) {
        this.formation_tram = formation_tram;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }



    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (utilisateur.getChauffeur() != this || utilisateur.getChauffeur() == null) {
            utilisateur.setChauffeur(this);
        }
    }

    public Chauffeur(int idChauffeur, boolean formation_tram, Utilisateur utilisateur) {
        this.idChauffeur = idChauffeur;
        this.formation_tram = formation_tram;
        this.utilisateur = utilisateur;
    }

    //pr create dao
    public Chauffeur( boolean formation_tram, Utilisateur utilisateur) {
        this.formation_tram = formation_tram;
        this.utilisateur = utilisateur;
    }
}
