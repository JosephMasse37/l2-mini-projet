package LoireUrbanisme.borne;

import LoireUrbanisme.conduites.Conduites;
import metiers.*;
import passerelle.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AjouterBorneDialog {
    public static void show(BornePanel parentPanel) throws Exception {
        BorneDAO borneDAO = new BorneDAO(Connexion.getConnexion());
        ArretDAO arretDAO = new ArretDAO(Connexion.getConnexion());

        List<Arret> arrets = arretDAO.findAll();
        JComboBox<Arret> arretsCombo = new JComboBox<>(arrets.toArray(new Arret[0]));

        arretsCombo.addActionListener(e -> {
            try {
                Arret arretSelectionne = (Arret) arretsCombo.getSelectedItem();

                if (arretSelectionne == null) {
                    return;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Arrêt :"));
        panel.add(arretsCombo);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Ajouter une borne",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Arret selectedArret = (Arret) arretsCombo.getSelectedItem();

            // Créer le nouveau chauffeur
            Borne nouvelleBorne = new Borne(0, 0, selectedArret);

            try {
                Borne borneCree = borneDAO.create(nouvelleBorne);

                parentPanel.refreshPanel();

                JOptionPane.showMessageDialog(
                        parentPanel,
                        "La borne a été ajouté avec succès.",
                        "Ajout réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception e){
                JOptionPane.showMessageDialog(
                        parentPanel,
                        "La borne n'a pas pu être ajouté.\n" + e.getCause() + ".",
                        "Ajout échouée",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
}