package metiers;

public class Dessert {
    private Arret unArret;
    private Ligne uneLigne;

    public Arret getUnArret() {
        return unArret;
    }

    public void setUnArret(Arret unArret) {
        this.unArret = unArret;
    }

    public Ligne getUneLigne() {
        return uneLigne;
    }

    public void setUneLigne(Ligne uneLigne) {
        this.uneLigne = uneLigne;
    }

    public Dessert(Arret unArret, Ligne uneLigne) {
        this.unArret = unArret;
        this.uneLigne = uneLigne;

        unArret.addLigneDesservie(uneLigne);
        uneLigne.addArretDesservi(unArret);
    }
}
