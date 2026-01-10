package metiers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ligne {
    private int idLigne;
    private String libelle;
    private TypeLigne typeLigne;
    private Arret arretDepart;
    private Arret arretArrive;
    private List<Arret> arretsDesservis = new ArrayList<>();
    private int duree;

    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getDuree() {
        return duree;
    }


    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public TypeLigne getTypeLigne() {
        return typeLigne;
    }

    public void setTypeLigne(TypeLigne typeLigne) {
        this.typeLigne = typeLigne;
        typeLigne.addLigne(this);
    }

    public Arret getArretDepart() {
        return arretDepart;
    }

    public void setArretDepart(Arret arretDepart) {
        this.arretDepart = arretDepart;
        addArretDesservi(arretDepart);
    }

    public Arret getArretArrive() {
        return arretArrive;
    }

    public void setArretArrive(Arret arretArrive) {
        this.arretArrive = arretArrive;
        addArretDesservi(arretArrive);
    }

    public List<Arret> getArretsDesservis() {
        return arretsDesservis;
    }

    public void setArretsDesservis(List<Arret> arretsDesservis) {
        this.arretsDesservis = arretsDesservis;
    }

    public void addArretDesservi(Arret unArret) {
        this.arretsDesservis.add(unArret);
        if (!unArret.estDesservi(this)) {
            unArret.addLigneDesservie(this);
        }
    }

   /* public boolean estDesservi(Arret unArret) {
        boolean arretDesservi = false;
        int i = 0;

        while (i<this.arretsDesservis.size() || !arretDesservi) {
            if (unArret.getIdArret() == this.arretsDesservis.get(i).getIdArret()) {
                arretDesservi = true;
            }
            i++;
        }
        return arretDesservi;
    }
     J AI CHANGE CAR PLUS LISIBLE ET PAS MANIP D INDEX DONC MOINS D ERREURS POSSIBLE*/

    public boolean estDesservi(Arret unArret) {

        if (unArret == null) return false;

        return this.arretsDesservis.stream()
                .anyMatch(a -> a.getIdArret() == unArret.getIdArret());
    }

    public Ligne(int idLigne, String libelle, TypeLigne typeLigne) {
        this.libelle = libelle;
        this.typeLigne = typeLigne;
        this.idLigne = idLigne;
    }

    public Ligne(int idLigne, String libelle, TypeLigne typeLigne, Arret arretDepart, Arret arretArrive, int duree,List<Arret> arretsDesservis) {
        this.idLigne = idLigne;
        this.libelle = libelle;
        this.typeLigne = typeLigne;
        this.arretDepart = arretDepart;
        this.arretArrive = arretArrive;
        this.duree=duree;
        this.arretsDesservis = arretsDesservis;
    }

//pr insert
    public Ligne( String libelle, TypeLigne typeLigne, Arret arretDepart, Arret arretArrive,int duree) {
        this.libelle = libelle;
        this.typeLigne = typeLigne;
        this.arretDepart = arretDepart;
        this.arretArrive = arretArrive;
        this.duree=duree;

    }

    //pr find
    public Ligne(int idLigne, String libelle, TypeLigne typeLigne, Arret arretDepart, Arret arretArrive,int duree) {
        this.idLigne=idLigne;
        this.libelle = libelle;
        this.typeLigne = typeLigne;
        this.arretDepart = arretDepart;
        this.arretArrive = arretArrive;
        this.duree=duree;

    }
    public void setDuree(int duree) {
        this.duree = duree;
    }
}

