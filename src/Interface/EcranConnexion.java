package Interface;

import LoireUrbanisme.connexion.PageConnexion;
import passerelle.Connexion;
import com.formdev.flatlaf.FlatDarkLaf;
import passerelle.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class EcranConnexion extends JFrame {

    private JPanel zoneContenu;

    private Connection connexion = Connexion.getConnexion();
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connexion);

    public EcranConnexion() throws DAOException {

        setTitle("LoireUrbanisme - Connexion - Système de Gestion des Transports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(new PageConnexion(this), BorderLayout.CENTER);
    }

    public static void main(String[] args) throws DAOException {
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Échec au chargement du thème");
        }

        EcranConnexion fenetreMain = new EcranConnexion();

        // Lancement
        SwingUtilities.invokeLater(() -> {
            fenetreMain.setVisible(true);
        });
    }
}
