package LoireUrbanisme.borne;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatCard extends JPanel {

    public StatCard(String titre, JLabel valeur) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(40, 40, 40));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setForeground(Color.LIGHT_GRAY);
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        valeur.setFont(new Font("Segoe UI", Font.BOLD, 34));
        valeur.setHorizontalAlignment(SwingConstants.CENTER);

        add(lblTitre, BorderLayout.NORTH);
        add(valeur, BorderLayout.CENTER);
    }
}
