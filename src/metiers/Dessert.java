package metiers;

public class Dessert {
    private Arret unArret;
    private Ligne uneLigne;
    private int ordre;

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

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public Dessert(Arret unArret, Ligne uneLigne, int ordre) {
        this.unArret = unArret;
        this.uneLigne = uneLigne;
        this.ordre = ordre;

        unArret.addLigneDesservie(uneLigne);
        uneLigne.addArretDesservi(unArret);
    }

    @Override
    public String toString() {
        return "Arrêt : " + unArret.getNom() + " desservi par la Ligne " + uneLigne.getLibelle() + " est en " + ordre + "è.";
    }
}
