package metiers;

import java.util.ArrayList;
import java.util.List;

public class TypeLigne {
    private int idTypeLigne;
    private String libelle;
    private List<Ligne> listeLignes = new ArrayList<>();

    public int getIdTypeLigne() {
        return idTypeLigne;
    }

    public void setIdTypeLigne(int idTypeLigne) {
        this.idTypeLigne = idTypeLigne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Ligne> getListeLignes() {
        return listeLignes;
    }

    public void setListeLignes(List<Ligne> listeLignes) {
        this.listeLignes = listeLignes;
    }

    public void addLigne(Ligne ligne){
        listeLignes.add(ligne);
    }

    public TypeLigne(int idTypeLigne, String libelle) {
        this.idTypeLigne = idTypeLigne;
        this.libelle = libelle;
    }
}
