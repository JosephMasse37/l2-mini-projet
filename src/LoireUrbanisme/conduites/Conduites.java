package LoireUrbanisme.conduites;

import metiers.ConduitSur;
import passerelle.ConduitSurDAO;
import passerelle.Connexion;
import passerelle.DAOException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Conduites extends JPanel {
    private Font customFont;
    private JTable tableau;

    private boolean filtreFormationTram = false;

    private final Color violet = new Color(165, 55, 255);

    public Conduites() {
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
        JLabel titre = new JLabel("CONDUITES - TABLEAU INFORMATIONNEL");
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
        String[] colonnes = {"Ligne", "Vehicule", "Chauffeur", "Date & Heure Départ ligne", "Nombres de validations", "Actions"};

        Object[][] donnees = null;

        try {
            ConduitSurDAO dao = new ConduitSurDAO(Connexion.getConnexion());
            List<ConduitSur> conduites;

            conduites = dao.findAll();


            donnees = new Object[conduites.size()][6];

            for (int i = 0; i < conduites.size(); i++) {
                ConduitSur c = conduites.get(i);
                donnees[i][0] = c.getUneLigne().getLibelle();
                donnees[i][1] = c.getUnVehicule().getNumVehicule();
                donnees[i][2] = c.getLeChauffeur().getUtilisateur().getPrenom() + " " + c.getLeChauffeur().getUtilisateur().getNom();
                donnees[i][3] = c.getDateHeureConduite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss"));
                donnees[i][4] = c.getNbValidation();
                donnees[i][5] = ""; // colonne boutons
            }

        } catch (DAOException e) {
            return new JLabel("Erreur chargement conduites");
        }

        tableau = new JTable(donnees, colonnes) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tableau.setRowHeight(40);

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
        tableau.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(tableau, this));

        return new JScrollPane(tableau);
    }

    private Component addButton() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false); // garde le fond sombre
        JButton addButton = new JButton("Ajouter une conduite") {
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
                AjouterConduiteDialog.show(this, tableau);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'ajout de la conduite",
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
