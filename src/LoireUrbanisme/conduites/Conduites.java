package LoireUrbanisme.conduites;

import metiers.Chauffeur;
import metiers.ConduitSur;
import metiers.Ligne;
import metiers.Vehicule;
import passerelle.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Conduites extends JPanel {
    private Font customFont;
    private JTable tableau;
    private TableRowSorter<TableModel> sorter;

    // DAOs
    private final LigneDAO ligneDAO = new LigneDAO(Connexion.getConnexion());
    private final VehiculeDAO vehiculeDAO = new VehiculeDAO(Connexion.getConnexion());
    private final ChauffeurDAO chauffeurDAO = new ChauffeurDAO(Connexion.getConnexion());

    // Filtrage
    private JComboBox<Ligne> lignesCombo;
    private JComboBox<Vehicule> vehiculesCombo;
    private JComboBox<Chauffeur> chauffeursCombo;

    private final Color violet = new Color(165, 55, 255);

    public Conduites() {
        init();
    }

    private void init() {
        chargerPolice();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);

        placeElements();
    }

    private void placeElements() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setOpaque(false);

        topPanel.add(titre(), BorderLayout.NORTH);

        // Création des combos
        topPanel.add(filtresPanel(), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableau(), BorderLayout.CENTER);
        add(addButton(), BorderLayout.SOUTH);

        // Configurer les filtres
        setupFiltres();
    }

    private JPanel filtresPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        // Ligne
        List<Ligne> lignes = null;
        try { lignes = ligneDAO.findAll(); } catch (DAOException e) { e.printStackTrace(); }
        lignes.add(0, null); // "Toutes les lignes"
        lignesCombo = new JComboBox<>(lignes.toArray(new Ligne[0]));
        lignesCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                String text = (value == null) ? "Toutes les lignes" : value.toString();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        lignesCombo.setPreferredSize(new Dimension(180, 30));
        lignesCombo.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        // Véhicule
        List<Vehicule> vehicules = null;
        try { vehicules = vehiculeDAO.findAll(); } catch (DAOException e) { e.printStackTrace(); }
        vehicules.add(0, null);
        vehiculesCombo = new JComboBox<>(vehicules.toArray(new Vehicule[0]));
        vehiculesCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                String text = (value == null) ? "Tous les véhicules" : value.toString();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        vehiculesCombo.setPreferredSize(new Dimension(180, 30));
        vehiculesCombo.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        // Chauffeur
        List<Chauffeur> chauffeurs = null;
        try { chauffeurs = chauffeurDAO.findAll(); } catch (DAOException e) { e.printStackTrace(); }
        chauffeurs.add(0, null);
        chauffeursCombo = new JComboBox<>(chauffeurs.toArray(new Chauffeur[0]));
        chauffeursCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                String text = (value == null) ? "Tous les chauffeurs" : value.toString();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        chauffeursCombo.setPreferredSize(new Dimension(180, 30));
        chauffeursCombo.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        panel.add(lignesCombo);
        panel.add(vehiculesCombo);
        panel.add(chauffeursCombo);

        return panel;
    }

    private void setupFiltres() {
        // Créer le sorter
        sorter = new TableRowSorter<>(tableau.getModel());
        tableau.setRowSorter(sorter);

        lignesCombo.addActionListener(e -> {
            Ligne selectedLigne = (Ligne) lignesCombo.getSelectedItem();
            Integer idLigne = (selectedLigne == null) ? null : selectedLigne.getIdLigne();

            if (idLigne != null) {
                vehiculesCombo.setSelectedItem(null);
                chauffeursCombo.setSelectedItem(null);
            }
            applyFiltre(idLigne, 6);
        });

        vehiculesCombo.addActionListener(e -> {
            Vehicule selectedVehicule = (Vehicule) vehiculesCombo.getSelectedItem();
            Integer numVehicule = (selectedVehicule == null) ? null : selectedVehicule.getNumVehicule();

            if (numVehicule != null) {
                lignesCombo.setSelectedItem(null);
                chauffeursCombo.setSelectedItem(null);
            }
            applyFiltre(numVehicule, 7);
        });

        chauffeursCombo.addActionListener(e -> {
            Chauffeur selectedChauffeur = (Chauffeur) chauffeursCombo.getSelectedItem();
            Integer idChauffeur = (selectedChauffeur == null) ? null : selectedChauffeur.getIdChauffeur();

            if (idChauffeur != null) {
                lignesCombo.setSelectedItem(null);
                vehiculesCombo.setSelectedItem(null);
            }
            applyFiltre(idChauffeur, 8);
        });
    }

    private void applyFiltre(Integer valeur, int colIndex) {
        if (valeur == null) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                    int cellValue = (int) entry.getValue(colIndex);
                    return cellValue == valeur;
                }
            });
        }
    }

    private JComponent tableau() {
        String[] colonnes = {"Ligne", "Vehicule", "Chauffeur", "Date & Heure Départ ligne",
                "Nombres de validations", "Actions", "idLigne", "numVehicule",
                "idChauffeur", "LocalDateTimeConduite"};
        Object[][] donnees = null;

        try {
            ConduitSurDAO dao = new ConduitSurDAO(Connexion.getConnexion());
            List<ConduitSur> conduites = dao.findAll();
            donnees = new Object[conduites.size()][10];

            for (int i = 0; i < conduites.size(); i++) {
                ConduitSur c = conduites.get(i);
                donnees[i][0] = c.getUneLigne().getLibelle();
                donnees[i][1] = c.getUnVehicule().getNumVehicule();
                donnees[i][2] = c.getLeChauffeur().getUtilisateur().getPrenom() + " " +
                        c.getLeChauffeur().getUtilisateur().getNom();
                donnees[i][3] = c.getDateHeureConduite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss"));
                donnees[i][4] = c.getNbValidation();
                donnees[i][5] = ""; // Actions boutons

                // IDs
                donnees[i][6] = c.getUneLigne().getIdLigne();
                donnees[i][7] = c.getUnVehicule().getNumVehicule();
                donnees[i][8] = c.getLeChauffeur().getIdChauffeur();
                donnees[i][9] = c.getDateHeureConduite();
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

        // Cacher les colonnes ID
        for (int i = 6; i < tableau.getColumnCount(); i++) {
            tableau.getColumnModel().getColumn(i).setMinWidth(0);
            tableau.getColumnModel().getColumn(i).setMaxWidth(0);
        }

        // Désactiver sélection
        tableau.setRowSelectionAllowed(false);
        tableau.setColumnSelectionAllowed(false);

        // Stylisation
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
                    setHorizontalAlignment(SwingConstants.CENTER);
                    c.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));
                    return c;
                }
            });
        }

        tableau.setShowGrid(true);
        tableau.setGridColor(Color.LIGHT_GRAY);
        tableau.getTableHeader().setBackground(new Color(30, 30, 30));
        tableau.getTableHeader().setForeground(Color.WHITE);
        tableau.getTableHeader().setFont(customFont.deriveFont(Font.BOLD, 18f));

        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.CENTER); setVerticalAlignment(SwingConstants.CENTER); }
        });

        tableau.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tableau.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(tableau, this));

        return new JScrollPane(tableau);
    }

    private JComponent titre() {
        JLabel titre = new JLabel("CONDUITES - TABLEAU INFORMATIONNEL");
        titre.setFont(new Font(customFont.getFontName(), Font.BOLD, 26));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        return titre;
    }

    private Component addButton() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton addButton = new JButton("Ajouter une conduite") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(violet);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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
            try { AjouterConduiteDialog.show(this, tableau); }
            catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE); }
        });

        return bottomPanel;
    }

    private void chargerPolice() {
        try {
            File fontFile = new File("resources/LoireUrbanisme.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) { System.out.println("Erreur police : " + e.getMessage()); }
    }

    public void refreshTable() {
        removeAll();
        placeElements();
        revalidate();
        repaint();
    }
}
