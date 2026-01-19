package LoireUrbanisme.ReseauTrafic;

import metiers.Ligne;
import passerelle.Connexion;
import passerelle.DAOException;
import passerelle.LigneDAO;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ReseauTraficAction {
    private final static LigneDAO ligneDAO = new LigneDAO(Connexion.getConnexion());

    public static void editLigne(Ligne l, JPanel parentPanel) throws DAOException {
        //Libellé
        JTextField nomLigne = new JTextField(l.getLibelle(), 15);

        // Durée
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(180);
        formatter.setAllowsInvalid(false);

        JFormattedTextField duree = new JFormattedTextField(formatter);
        duree.setValue(l.getDuree());

        // Couleur
        JTextField hexField = new JTextField(6);
        hexField.setEditable(false);

        JColorChooser colorChooser = new JColorChooser();

        JPanel preview = new JPanel();
        preview.setPreferredSize(new Dimension(40, 40));
        preview.setBackground(new Color(Integer.parseInt(l.getCouleur(), 16)));
        preview.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Couleur → preview
        colorChooser.getSelectionModel().addChangeListener(e -> {
            preview.setBackground(colorChooser.getColor());
        });

        // Clic sur le carré → ouvrir le picker
        preview.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Color selected = JColorChooser.showDialog(
                        parentPanel,
                        "Choisir une couleur",
                        preview.getBackground()
                );
                if (selected != null) {
                    colorChooser.setColor(selected);
                    preview.setBackground(selected);
                }
            }
        });

        // Écouteur : couleur → hex
        colorChooser.getSelectionModel().addChangeListener(e -> {
            Color c = colorChooser.getColor();
            String hex = String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
            hexField.setText(hex);
        });

        // Construire le panneau pour la boîte de dialogue
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Ligne de "+ l.getTypeLigne().getLibelle() + " modifiée :"));
        panel.add(new JLabel("Ligne " + l.getLibelle()));

        panel.add(new JLabel("Durée (en minutes) :"));
        panel.add(duree);

        panel.add(new JLabel("Couleur :"));
        panel.add(preview);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Modifier la ligne",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Récupérer les nouvelles valeurs
            int newDuree = (Integer) duree.getValue();
            String couleurHex = hexField.getText();

            // Appliquer les modifications
            l.setDuree(newDuree);
            l.setCouleur(couleurHex);

            // Sauvegarder en base
            boolean r1 = ligneDAO.update(l);

            if (r1) {
                JOptionPane.showMessageDialog(parentPanel,
                        "La ligne a été modifiée avec succès.",
                        "Modification réussie",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentPanel,
                        "La ligne n'a pas pu être modifiée.",
                        "Modification échouée",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
