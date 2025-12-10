package tests;

import metiers.Utilisateur;
import passerelle.UtilisateurDAO;

public class TestUtilisateurDAO {
    public static void main(String[] args) {
        Utilisateur u1 = UtilisateurDAO.getUnUtilisateur("meline.asefatulu","etudiant007");
        System.out.println(u1);
    }
}
