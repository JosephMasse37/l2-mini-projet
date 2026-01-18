package LoireUrbanisme.chauffeurs;

import metiers.Chauffeur;
import metiers.Utilisateur;
import passerelle.ChauffeurDAO;
import passerelle.Connexion;
import passerelle.UtilisateurDAO;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class AjouterChauffeurDialog {
    public static void show(JPanel parentPanel, JTable table) throws Exception {
        ChauffeurDAO chauffeurDAO = new ChauffeurDAO(Connexion.getConnexion());
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(Connexion.getConnexion());

        List<Utilisateur> utilisateurs = utilisateurDAO.getUtilisateursNonChauffeur();
        JComboBox<Utilisateur> utilisateurCombo = new JComboBox<>(utilisateurs.toArray(new Utilisateur[0]));

        JCheckBox formationTramBox = new JCheckBox("Formation Tram");

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Utilisateur :"));
        panel.add(utilisateurCombo);
        panel.add(new JLabel("Formation Tram :"));
        panel.add(formationTramBox);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Ajouter un chauffeur",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Utilisateur selectedUser = (Utilisateur) utilisateurCombo.getSelectedItem();
            boolean formationTram = formationTramBox.isSelected();

            // Créer le nouveau chauffeur
            Chauffeur nouveau = new Chauffeur(formationTram, selectedUser);

            Chauffeur createdChauffeur = chauffeurDAO.create(nouveau);

            if (createdChauffeur.getIdChauffeur() != 0) {
                JOptionPane.showMessageDialog(
                        parentPanel,
                        "Le chauffeur a été ajouté avec succès.",
                        "Ajout réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        parentPanel,
                        "Le chauffeur n'a pas pu être ajouté.",
                        "Ajout échouée",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            // Rafraîchir le tableau
            ((Chauffeurs) parentPanel).refreshTable();
        }
    }
}