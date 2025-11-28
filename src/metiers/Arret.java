package metiers;

import java.util.ArrayList;
import java.util.List;

public class Arret {
    private int idArret;
    private String nom;
    private double latitude;
    private double longitude;

    private List<Borne> listeBornes = new ArrayList<>();

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

    public void addBorne(Borne borne) {
        this.listeBornes.add(borne);
    }
}
