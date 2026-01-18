package LoireUrbanisme.ReseauTrafic;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class zoneContenuLigne extends JPanel {
    Font customFont;

    private void chargerPolice() {
        try {
            File fontFile = new File("resources/LoireUrbanisme.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("Erreur police : " + e.getMessage());


        }}

    public zoneContenuLigne(String nomLigne, String trajet, String statut) {
        this.setOpaque(false);
        // BorderLayout pr mieux control les 3 zones (Haut, Milieu, Bas)
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(15, 20, 12, 20));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // RANGÉE DU HAUT : NOM + STATUT
        JPanel ligneHaut = new JPanel(new BorderLayout());
        ligneHaut.setOpaque(false);

        chargerPolice();

        JLabel lblNom = new JLabel("Ligne " + nomLigne);
        lblNom.setForeground(new Color(180, 180, 180));
        lblNom.setFont(customFont.deriveFont(Font.PLAIN, 16f));

        JLabel lblStatut = new JLabel(statut.toUpperCase());
        lblStatut.setFont(customFont.deriveFont(Font.PLAIN, 12f));
        lblStatut.setForeground(statut.equalsIgnoreCase("EN SERVICE") ? new Color(144, 238, 144) : new Color(200, 50, 50));

        ligneHaut.add(lblNom, BorderLayout.WEST);
        ligneHaut.add(lblStatut, BorderLayout.EAST);

        //  CENTRE : TRAJET AVEC FLÈCHE

        JPanel ligneCentre = new JPanel(new GridBagLayout()); //bien pr pas couper noms + div 3
        ligneCentre.setOpaque(false);

        GridBagConstraints reglage = new GridBagConstraints();
        reglage.fill = GridBagConstraints.HORIZONTAL;
        reglage.gridy = 0; // ts les elem sur la mm ligne (ligne 0)

        // Nettoyage de la chaîne
        String trajetNettoye = trajet.replace("->", "-");
        String[] points = trajetNettoye.split("-");
        String depart = (points.length > 0) ? points[0].trim().toUpperCase() : ""; // un depart? sinn vide
        String arrivee = (points.length > 1) ? points[1].trim().toUpperCase() : ""; //same

        // Ville de Départ
        JLabel lblDep = new JLabel(depart);
        lblDep.setForeground(Color.WHITE);
        lblDep.setFont(customFont.deriveFont(Font.PLAIN, 12f));
        reglage.gridx = 0;
        reglage.weightx = 0; // Ne prend que la place nécessaire
        reglage.insets = new Insets(0, 0, 0, 5);
        ligneCentre.add(lblDep, reglage); //(composant, contraintes)

        // La flèche
        JLabel lblFleche = new JLabel("◦- - - - - -➔");
        lblFleche.setForeground(new Color(120, 110, 150));
        lblFleche.setHorizontalAlignment(SwingConstants.CENTER);
        reglage.gridx = 1;
        reglage.weightx = 1.0; //  flèche occupe tt l'espace central disponible
        ligneCentre.add(lblFleche, reglage);

        // Ville d'Arrivée
        JLabel lblArr = new JLabel(arrivee);
        lblArr.setForeground(Color.WHITE);
        lblArr.setFont(customFont.deriveFont(Font.PLAIN, 12f));
        reglage.gridx = 2;
        reglage.weightx = 0; // Ne prend que la place nécessaire
        reglage.insets = new Insets(0, 5, 0, 0);
        ligneCentre.add(lblArr, reglage);

        // BAS : ICÔNE CRAYON
        JButton btnEdit = new JButton("✎");
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setBackground(Color.BLACK);
        btnEdit.setFont(new Font("SansSerif", Font.PLAIN, 20));
        btnEdit.setHorizontalAlignment(SwingConstants.RIGHT);
        btnEdit.setMargin(new Insets(2, 6, 2, 6));

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnWrapper.setOpaque(false); // important si fond personnalisé
        btnWrapper.add(btnEdit);

        // le tt :
        this.add(ligneHaut, BorderLayout.NORTH);
        this.add(ligneCentre, BorderLayout.CENTER);
        this.add(btnWrapper, BorderLayout.SOUTH);
    }

}