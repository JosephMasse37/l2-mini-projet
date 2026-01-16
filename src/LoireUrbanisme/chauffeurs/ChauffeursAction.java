package LoireUrbanisme.chauffeurs;

import metiers.Chauffeur;
import metiers.Utilisateur;
import passerelle.ChauffeurDAO;
import passerelle.Connexion;
import passerelle.DAOException;
import passerelle.UtilisateurDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChauffeursAction {
    private static ChauffeurDAO chauffeurDAO = new ChauffeurDAO(Connexion.getConnexion());
    private static UtilisateurDAO utilisateurDAO = new UtilisateurDAO(Connexion.getConnexion());

    public static void editChauffeur(int idChauffeur, JPanel parentPanel) throws DAOException {
        Chauffeur c = chauffeurDAO.find(idChauffeur);

        if (c == null) {
            JOptionPane.showMessageDialog(parentPanel,
                    "Chauffeur introuvable.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupérer tous les utilisateurs pour le menu déroulant
        List<Utilisateur> utilisateurs = utilisateurDAO.findAll();

        JComboBox<Utilisateur> utilisateurCombo = new JComboBox<>(utilisateurs.toArray(new Utilisateur[0]));
        utilisateurs.stream()
                .filter(u -> u.getUsername().equals(c.getUtilisateur().getUsername()))
                .findFirst()
                .ifPresent(utilisateurCombo::setSelectedItem);

        JTextField prenomField = new JTextField(c.getUtilisateur().getPrenom(), 15);
        JTextField nomField = new JTextField(c.getUtilisateur().getNom(), 15);

        // Synchroniser les champs prénom/nom avec le choix du combo
        utilisateurCombo.addActionListener(e -> {
            Utilisateur selectedUser = (Utilisateur) utilisateurCombo.getSelectedItem();
            if (selectedUser != null) {
                prenomField.setText(selectedUser.getPrenom());
                nomField.setText(selectedUser.getNom());
            }
        });

        JCheckBox formationTramBox = new JCheckBox("Formation Tram");
        formationTramBox.setSelected(c.isFormation_tram());

        // Construire le panneau pour la boîte de dialogue
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("ID (non modifiable) :"));
        panel.add(new JLabel(String.valueOf(c.getIdChauffeur())));

        panel.add(new JLabel("Utilisateur :"));
        panel.add(utilisateurCombo);

        panel.add(new JLabel("Prénom :"));
        panel.add(prenomField);

        panel.add(new JLabel("Nom :"));
        panel.add(nomField);

        panel.add(new JLabel("Formation Tram :"));
        panel.add(formationTramBox);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Modifier le chauffeur",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Récupérer les nouvelles valeurs
            Utilisateur selectedUser = (Utilisateur) utilisateurCombo.getSelectedItem();
            String prenom = prenomField.getText().trim();
            String nom = nomField.getText().trim();
            boolean formationTram = formationTramBox.isSelected();

            // Appliquer les modifications
            c.setUtilisateur(selectedUser);
            c.getUtilisateur().setPrenom(prenom);
            c.getUtilisateur().setNom(nom);
            c.setFormation_tram(formationTram);

            // Sauvegarder en base
            chauffeurDAO.update(c);
            utilisateurDAO.update(c.getUtilisateur());

            JOptionPane.showMessageDialog(parentPanel,
                    "Le chauffeur a été modifié avec succès.",
                    "Modification réussie",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void deleteChauffeur(int idChauffeur, JPanel parentPanel) throws DAOException {
        Chauffeur c = chauffeurDAO.find(idChauffeur);
        boolean result = chauffeurDAO.delete(c);
        // Affiche un message de confirmation
        if (result) {
            JOptionPane.showMessageDialog(
                    parentPanel,
                    "Le chauffeur a été supprimé avec succès.",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    parentPanel,
                    "Le chauffeur n'a pas pu être supprimé.",
                    "Suppression échouée",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
