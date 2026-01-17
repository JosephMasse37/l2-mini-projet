package LoireUrbanisme.conduites;

import LoireUrbanisme.chauffeurs.Chauffeurs;
import metiers.*;
import passerelle.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AjouterConduiteDialog {
    public static void show(JPanel parentPanel, JTable table) throws Exception {
        ConduitSurDAO conduitSurDAO = new ConduitSurDAO(Connexion.getConnexion());
        LigneDAO ligneDAO = new LigneDAO(Connexion.getConnexion());
        VehiculeDAO vehiculeDAO = new VehiculeDAO(Connexion.getConnexion());
        ChauffeurDAO chauffeurDAO = new ChauffeurDAO(Connexion.getConnexion());

        List<Ligne> lignes = ligneDAO.findAll();
        JComboBox<Ligne> lignesCombo = new JComboBox<>(lignes.toArray(new Ligne[0]));

        List<Vehicule> vehiculesInit = vehiculeDAO.findAll();
        JComboBox<Vehicule> vehiculesCombo = new JComboBox<>(vehiculesInit.toArray(new Vehicule[0]));
        vehiculesCombo.setEnabled(false);

        lignesCombo.addActionListener(e -> {
            try {
                Ligne ligneSelectionnee = (Ligne) lignesCombo.getSelectedItem();
                vehiculesCombo.setEnabled(true);

                if (ligneSelectionnee == null) {
                    return;
                }

                DefaultComboBoxModel<Vehicule> model;

                if (ligneSelectionnee.getTypeLigne().getIdTypeLigne() == 1) {

                    List<Bus> vehicules = vehiculeDAO.getBus();
                    model = new DefaultComboBoxModel<>(
                            vehicules.toArray(new Vehicule[0])
                    );

                } else if (ligneSelectionnee.getTypeLigne().getIdTypeLigne() == 2) {

                    List<Tram> vehicules = vehiculeDAO.getTram();
                    model = new DefaultComboBoxModel<>(
                            vehicules.toArray(new Vehicule[0])
                    );

                } else {

                    List<Vehicule> vehicules = vehiculeDAO.findAll();
                    model = new DefaultComboBoxModel<>(
                            vehicules.toArray(new Vehicule[0])
                    );
                }

                // Mise à jour du combo existant
                vehiculesCombo.setModel(model);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        List<Chauffeur> chauffeurs = chauffeurDAO.findAll();
        JComboBox<Chauffeur> chauffeursCombo = new JComboBox<>(chauffeurs.toArray(new Chauffeur[0]));

        Date now = new Date();
        SpinnerDateModel dateModel = new SpinnerDateModel(
                now,           // valeur initiale
                now,                 // date min
                null,                 // date max
                Calendar.MINUTE       // incrément
        );

        JSpinner dateHeureSpinner = new JSpinner(dateModel);

        // Format visible pour l'utilisateur
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateHeureSpinner, "dd/MM/yyyy HH:mm");
        dateHeureSpinner.setEditor(editor);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Ligne :"));
        panel.add(lignesCombo);
        panel.add(new JLabel("Véhicule :"));
        panel.add(vehiculesCombo);
        panel.add(new JLabel("Chauffeur :"));
        panel.add(chauffeursCombo);
        panel.add(new JLabel("Date & Heure de début de conduite :"));
        panel.add(dateHeureSpinner);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Ajouter une conduite",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (!vehiculesCombo.isEnabled()) {
                JOptionPane.showMessageDialog(
                        parentPanel,
                        "Veuillez sélectionner une ligne.",
                        "Formulaire incomplet",
                        JOptionPane.WARNING_MESSAGE
                );

                AjouterConduiteDialog.show(parentPanel, table);
                return;
            }

            Ligne selectedLigne = (Ligne) lignesCombo.getSelectedItem();
            Vehicule selectedVehicule = (Vehicule) vehiculesCombo.getSelectedItem();
            Chauffeur selectedChauffeur = (Chauffeur) chauffeursCombo.getSelectedItem();

            Date dateHeure = (Date) dateHeureSpinner.getValue();
            LocalDateTime dateTimeConduite = dateHeure.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Créer le nouveau chauffeur
            ConduitSur nouvelleConduite = new ConduitSur(selectedChauffeur, selectedLigne, selectedVehicule, dateTimeConduite);

            try {
                ConduitSur createdConduitSur = conduitSurDAO.create(nouvelleConduite);

                JOptionPane.showMessageDialog(
                        parentPanel,
                        "La conduite a été ajouté avec succès.",
                        "Ajout réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception e){
                JOptionPane.showMessageDialog(
                        parentPanel,
                        "La conduite n'a pas pu être ajouté.\n" + e.getCause() + ".",
                        "Ajout échouée",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            // Rafraîchir le tableau
            ((Conduites) parentPanel).refreshTable();
        }
    }
}