package LoireUrbanisme.chauffeurs;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import metiers.Chauffeur;
import passerelle.ChauffeurDAO;
import passerelle.Connexion;
import passerelle.DAOException;

public class Chauffeurs extends JPanel {

    private Font customFont;
    private JTable tableau;

    public Chauffeurs() {
        init();
    }

    private void init() {
        chargerPolice();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //marg int
        setOpaque(false);

        add(titre(), BorderLayout.NORTH);
        add(tableau(), BorderLayout.CENTER);
    }

    private void chargerPolice() {
        try {
            File fontFile = new File("resources/LoireUrbanisme.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("Erreur police : " + e.getMessage());
        }
    }

    private JComponent titre() {
        JLabel titre = new JLabel("CHAUFFEURS - TABLEAU INFORMATIONNEL");
        titre.setFont(new Font(customFont.getFontName(), Font.BOLD, 26));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        return titre;
    }

    private JComponent tableau() {
        String[] colonnes = {"ID", "Utilisateur", "Prénom", "Nom", "Formation Tram", "Actions"};

        Object[][] donnees = null;

        try {
            Connection connexion = Connexion.getConnexion();
            ChauffeurDAO dao = new ChauffeurDAO(connexion);
            List<Chauffeur> chauffeurs = dao.findAll();

            donnees = new Object[chauffeurs.size()][6];

            for (int i = 0; i < chauffeurs.size(); i++) {
                Chauffeur c = chauffeurs.get(i);
                donnees[i][0] = c.getIdChauffeur();
                donnees[i][1] = c.getUtilisateur().getUsername();
                donnees[i][2] = c.getUtilisateur().getPrenom();
                donnees[i][3] = c.getUtilisateur().getNom();
                donnees[i][4] = c.isFormation_tram() ? "✔" : "❌";
                donnees[i][5] = ""; // colonne boutons
            }

        } catch (DAOException e) {
            return new JLabel("Erreur chargement chauffeurs");
        }

        tableau = new JTable(donnees, colonnes) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tableau.setRowHeight(40);

        // Cacher la colonne ID
        tableau.getColumnModel().getColumn(0).setMinWidth(0);
        tableau.getColumnModel().getColumn(0).setMaxWidth(0);

        // Désactiver la sélection
        tableau.setRowSelectionAllowed(false);
        tableau.setColumnSelectionAllowed(false);

        // Stylisation du tableau
        tableau.setBackground(new Color(45, 45, 45));
        tableau.setForeground(Color.WHITE);
        tableau.setSelectionBackground(new Color(83, 83, 83));
        tableau.setSelectionForeground(Color.WHITE);

        tableau.setShowGrid(true);
        tableau.setGridColor(Color.LIGHT_GRAY);

        tableau.getTableHeader().setBackground(new Color(30, 30, 30));
        tableau.getTableHeader().setForeground(Color.WHITE);
        tableau.getTableHeader().setFont(customFont.deriveFont(Font.BOLD, 16f));

        // Centrer le texte dans toutes les colonnes Object directement
        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER); // centre horizontalement
                setVerticalAlignment(SwingConstants.CENTER);   // centre verticalement si besoin
            }
        });

        // Ajouter les boutons
        tableau.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tableau.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(tableau, this));

        return new JScrollPane(tableau);
    }

    public void refreshTable() {
        removeAll();
        add(titre(), BorderLayout.NORTH);
        add(tableau(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

}
