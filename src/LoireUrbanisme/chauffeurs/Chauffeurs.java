package LoireUrbanisme.chauffeurs;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import LoireUrbanisme.menu.Menu;
import metiers.Chauffeur;
import passerelle.ChauffeurDAO;
import passerelle.Connexion;
import passerelle.DAOException;

public class Chauffeurs extends JPanel {

    private Font customFont;
    private Color violet = new Color(165, 55, 255);
    private JTable tableau;

    private Menu menu;

    private boolean filtreFormationTram = false;

    public Chauffeurs(Menu menu) {
        this.menu = menu;

        init();
    }

    private void init() {
        chargerPolice();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //marg int
        setOpaque(false);

        placeElements();
    }

    private void placeElements() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.setOpaque(false);

        topPanel.add(titre(), BorderLayout.NORTH);
        topPanel.add(filtreFormationTramButton(), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableau(), BorderLayout.CENTER);
        add(addButton(), BorderLayout.SOUTH);
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

    private JComponent filtreFormationTramButton() {
        JButton filtreBtn = new JButton("Filtrer Formation Tram");

        filtreBtn.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        // Remplir le fond avec couleur
        if (filtreFormationTram) {
            filtreBtn.setBackground(violet); // Violet
        } else {
            filtreBtn.setBackground(getBackground()); // Fond de base
        }

        filtreBtn.setPreferredSize(new Dimension(160, 40));
        filtreBtn.setBorder(BorderFactory.createLineBorder(violet, 1, true));

        // Supprimer le fond de base
        filtreBtn.setFocusPainted(false);
        filtreBtn.setOpaque(true);

        // Action du bouton
        filtreBtn.addActionListener(e -> {
            filtreFormationTram = !filtreFormationTram; // bascule l'état
            refreshTable(); // rafraîchit le tableau pour appliquer le filtre
        });

        // Mise en page à gauche du bouton
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.add(filtreBtn);

        return panel;
    }

    private JComponent tableau() {
        String[] colonnes = {"ID", "Utilisateur", "Prénom", "Nom", "Formation Tram", "Actions"};

        Object[][] donnees = null;

        try {
            Connection connexion = Connexion.getConnexion();
            ChauffeurDAO dao = new ChauffeurDAO(connexion);
            List<Chauffeur> chauffeurs;

            if (filtreFormationTram) {
                chauffeurs = dao.getChauffeursAvecFormationTram();
            } else {
                chauffeurs = dao.findAll();
            }


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


        for (int i = 0; i <= 3; i++) {
            tableau.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14)); // applique la police directement
                    setHorizontalAlignment(SwingConstants.CENTER);      // centre le texte
                    return c;
                }
            });
        }

        tableau.setShowGrid(true);
        tableau.setGridColor(Color.LIGHT_GRAY);

        tableau.getTableHeader().setBackground(new Color(30, 30, 30));
        tableau.getTableHeader().setForeground(Color.WHITE);
        tableau.getTableHeader().setFont(customFont.deriveFont(Font.BOLD, 18f));

        // Centrer le texte dans toutes les colonnes Object directement
        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER); // centre horizontalement
                setVerticalAlignment(SwingConstants.CENTER);   // centre verticalement si besoin
            }
        });

        // Ajouter les boutons
        tableau.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tableau.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(tableau, this, menu));

        return new JScrollPane(tableau);
    }

    private Component addButton() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false); // garde le fond sombre
        JButton addButton = new JButton("Ajouter un chauffeur") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Remplir le fond avec couleur
                g2.setColor(violet); // violet
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 20 px d’arrondi

                // Dessiner le texte centré
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);

        addButton.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        bottomPanel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                AjouterChauffeurDialog.show(this, tableau);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'ajout du chauffeur",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return bottomPanel;
    }

    public void refreshTable() {
        removeAll();

        placeElements();

        revalidate();
        repaint();
    }

}
