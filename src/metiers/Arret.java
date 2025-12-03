package metiers;

import java.util.ArrayList;
import java.util.List;

public class Arret {
    private int idArret;
    private String nom;
    private double latitude;
    private double longitude;

    private List<Borne> listeBornes = new ArrayList<>();
    private List<Ligne> listeLignesDesservies = new ArrayList<>();

    public int getIdArret() {
        return idArret;
    }

    public void setIdArret(int idArret) {
        this.idArret = idArret;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Borne> getListeBornes() {
        return listeBornes;
    }

    public void setListeBornes(List<Borne> listeBornes) {
        this.listeBornes = listeBornes;
    }

    public List<Ligne> getListeLignesDesservies() {
        return listeLignesDesservies;
    }

    public void setListeLignesDesservies(List<Ligne> listeLignesDesservies) {
        this.listeLignesDesservies = listeLignesDesservies;
    }

    public void addBorne(Borne borne) {
        this.listeBornes.add(borne);
    }

    public void addLigneDesservie(Ligne uneLigne) {
        this.listeLignesDesservies.add(uneLigne);
        if (!uneLigne.estDesservi(this)) {
            uneLigne.addArretDesservi(this);
        }
    }

   /* public boolean estDesservi(Ligne uneLigne) {
        boolean ligneDesservi = false;
        int i = 0;

        while (i<this.listeLignesDesservies.size() || !ligneDesservi) {
            if (uneLigne.getIdLigne() == this.listeLignesDesservies.get(i).getIdLigne()) {
                ligneDesservi = true;
            }
            i++;
        }
        return ligneDesservi;
    }*/

    public boolean estDesservi(Ligne uneLigne) {
        return this.listeLignesDesservies
                .stream()
                .anyMatch(ligne -> ligne.getIdLigne() == uneLigne.getIdLigne());
    }

    public Arret(int idArret, String nom, double latitude, double longitude) {
        this.idArret = idArret;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }
        // sans id car deja autoincrement + pour le insert mieux pour jdbc
      public Arret(String nom, double latitude, double longitude) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
