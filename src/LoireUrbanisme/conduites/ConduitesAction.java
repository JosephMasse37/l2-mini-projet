package LoireUrbanisme.conduites;

import metiers.*;
import passerelle.*;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConduitesAction {
    private final static ConduitSurDAO conduitSurDAO = new ConduitSurDAO(Connexion.getConnexion());
    private final static LigneDAO ligneDAO = new LigneDAO(Connexion.getConnexion());
    private final static VehiculeDAO vehiculeDAO = new VehiculeDAO(Connexion.getConnexion());
    private final static ChauffeurDAO chauffeurDAO = new ChauffeurDAO(Connexion.getConnexion());

    public static void editConduite(int idLigne, int numVehicule, int idChauffeur, LocalDateTime dateHeureConduite, JPanel parentPanel) throws DAOException {
        Ligne l = ligneDAO.find(idLigne);
        Vehicule v = vehiculeDAO.find(numVehicule);
        Chauffeur c = chauffeurDAO.find(idChauffeur);

        ConduitSur cs = conduitSurDAO.find(idChauffeur, idLigne, numVehicule, dateHeureConduite);

        if (cs == null) {
            JOptionPane.showMessageDialog(parentPanel,
                    "Conduite introuvable.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(9999);
        formatter.setAllowsInvalid(false);

        JFormattedTextField intField = new JFormattedTextField(formatter);

        // Construire le panneau pour la boîte de dialogue
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Conduite :"));
        panel.add(new JLabel("Ligne " + l.getLibelle() +
                " | Véhicule " + v.getNumVehicule() +
                " | Chauffeur " + c.getUtilisateur().getPrenom() + " " + c.getUtilisateur().getNom() +
                " | Daté au " + cs.getDateHeureConduite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss"))
        ));

        panel.add(new JLabel("Nombre de validations :"));
        panel.add(intField);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Modifier la conduite",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Récupérer les nouvelles valeurs
            int nbValidations = (Integer) intField.getValue();

            // Appliquer les modifications
            cs.setNbValidation(nbValidations);

            // Sauvegarder en base
            boolean r1 = conduitSurDAO.update(cs);

            if (r1) {
                JOptionPane.showMessageDialog(parentPanel,
                        "Le chauffeur a été modifié avec succès.",
                        "Modification réussie",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentPanel,
                        "Le chauffeur n'a pas pu être modifié.",
                        "Modification échouée",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void deleteConduite(int idLigne, int numVehicule, int idChauffeur, LocalDateTime dateHeureConduite, JPanel parentPanel) throws DAOException {
        Ligne l = ligneDAO.find(idLigne);
        Vehicule v = vehiculeDAO.find(numVehicule);
        Chauffeur c = chauffeurDAO.find(idChauffeur);

        ConduitSur cs = conduitSurDAO.find(idChauffeur, idLigne, numVehicule, dateHeureConduite);

        boolean result = conduitSurDAO.delete(cs);
        // Affiche un message de confirmation
        if (result) {
            JOptionPane.showMessageDialog(
                    parentPanel,
                    "La conduite a été supprimé avec succès.",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    parentPanel,
                    "La conduite n'a pas pu être supprimé.",
                    "Suppression échouée",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
