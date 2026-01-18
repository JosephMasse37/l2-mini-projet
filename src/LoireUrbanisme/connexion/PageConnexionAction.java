package LoireUrbanisme.connexion;

import metiers.Utilisateur;
import passerelle.Connexion;
import passerelle.DAOException;
import passerelle.UtilisateurDAO;

public class PageConnexionAction {
    private static UtilisateurDAO utilisateurDAO = new UtilisateurDAO(Connexion.getConnexion());

    public static Utilisateur seConnecter(String username, char[] pass) throws DAOException {
        StringBuilder password = new StringBuilder();

        for (char c : pass) {
            password.append(c);
        }

        return utilisateurDAO.getUnUtilisateur(username, password.toString(), false);
    }
}
