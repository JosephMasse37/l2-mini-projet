package LoireUrbanisme.vehicule;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// IMPORTS RÉELS
import metiers.Vehicule;
import metiers.Bus;
import metiers.Tram;
import passerelle.VehiculeDAO;
import passerelle.Connexion;
import passerelle.DAOException;

public class PanelVehicules extends JPanel {

    // --- ATTRIBUTS ---
    private JTable table;
    private DefaultTableModel tableModel;
    
    // La liste source (chargée depuis la BDD)
    private List<Vehicule> listeCompleteVehicules; 
    
    // DAO
    private VehiculeDAO vehiculeDAO;

    // --- CONSTRUCTEUR ---
    public PanelVehicules(VehiculeDAO vehiculeDAO) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.decode("#1E1E1E")); // Fond Noir Charbon

        // 1. Initialisation du DAO
        this.vehiculeDAO = vehiculeDAO;

        // 2. Chargement des données
        chargerDonnees();

        // 3. Création des composants
        JPanel panelTop = createFilterPanel();
        JPanel panelCenter = createTablePanel();
        JPanel panelBottom = createBottomPanel();

        // 4. Assemblage
        this.add(panelTop, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
    }

    // --- 1. BARRE DE FILTRES (HAUT) ---
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        panel.setOpaque(false);

        // Boutons de filtre
        panel.add(createFilterButton("Tous", true));
        panel.add(createFilterButton("Bus", false));
        panel.add(createFilterButton("Tram", false)); // Attention: "Tram" dans votre BDD, pas "Tramway"
        
        // Indicateur "En service"
        JPanel statusPanel = new RoundedPanel(Color.decode("#2C2C2C"), 20);
        // On compte tout le monde comme "En service" vu qu'on a pas l'info
        JLabel lblStatus = new JLabel("Total Parc : " + listeCompleteVehicules.size());
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBorder(new EmptyBorder(5, 15, 5, 15));
        statusPanel.add(lblStatus);
        
        panel.add(Box.createHorizontalStrut(20)); 
        panel.add(statusPanel);

        return panel;
    }

    private JToggleButton createFilterButton(String text, boolean selected) {
        JToggleButton btn = new JToggleButton(text);
        btn.setSelected(selected);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.BLACK);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        // Action au clic : on filtre la liste locale
        btn.addActionListener(e -> {
            // Désélectionner les autres boutons (Bricolage visuel rapide)
            for (Component c : btn.getParent().getComponents()) {
                if (c instanceof JToggleButton && c != btn) ((JToggleButton) c).setSelected(false);
            }
            btn.setSelected(true);
            filtrerTableau(text);
        });
        
        return btn;
    }

    // --- 2. LE TABLEAU (CENTRE) ---
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Colonnes : Statut (0), Modèle (1), Ligne (2), Maintenance (3), Edit (4)
        String[] colonnes = {"Statut", "Modèle / Marque", "Ligne", "Dernière Maintenance", ""};

        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Lecture seule
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(50); // Lignes bien hautes
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setGridColor(Color.decode("#1E1E1E")); // Couleur inter-lignes

        // Couleurs Sombres
        table.setBackground(Color.decode("#2D2D2D"));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(Color.decode("#404040"));
        table.setSelectionForeground(Color.WHITE);

        // --- RENDERERS SPÉCIFIQUES ---
        
        // 1. Point de Statut
        table.getColumnModel().getColumn(0).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(0).setMaxWidth(60);

        // 2. Crayon Modifier
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(4).setMaxWidth(50);

        // 3. Texte Centré pour le reste
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(Color.decode("#2D2D2D"));
        centerRenderer.setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // En-tête
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#1E1E1E"));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBorder(null);

        // Remplissage initial
        filtrerTableau("Tous");

        // Clic sur le crayon
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (col == 4 && row >= 0) {
                    modifierVehiculeAction(row);
                }
            }
        });

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Conteneur arrondi visuel
        RoundedPanel container = new RoundedPanel(Color.decode("#2D2D2D"), 20);
        container.setLayout(new BorderLayout());
        container.add(header, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        panel.add(container, BorderLayout.CENTER);
        return panel;
    }

    // --- 3. BARRE DU BAS (BOUTON AJOUTER) ---
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        panel.setOpaque(false);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(Color.WHITE);
        btnAjouter.setForeground(Color.BLACK);
        btnAjouter.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAjouter.addActionListener(e -> ajouterVehiculeAction());

        panel.add(btnAjouter);
        return panel;
    }

    // --- LOGIQUE MÉTIER ---

    private void chargerDonnees() {
        try {
            // Appel réel à la BDD
            listeCompleteVehicules = vehiculeDAO.findAll();
        } catch (DAOException e) {
            listeCompleteVehicules = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Erreur chargement véhicules : " + e.getMessage());
        }
    }

    private void filtrerTableau(String typeFiltre) {
        List<Vehicule> vehiculesAffiches;

        if (typeFiltre.equals("Tous")) {
            vehiculesAffiches = listeCompleteVehicules;
        } else {
            // Filtrage Java sur le Type (Bus ou Tram)
            vehiculesAffiches = listeCompleteVehicules.stream()
                .filter(v -> {
                    if (v.getTypevehicule() == null) return false;
                    return v.getTypevehicule().getLibelle().equalsIgnoreCase(typeFiltre);
                })
                .collect(Collectors.toList());
        }
        rafraichirTableau(vehiculesAffiches);
    }

    private void rafraichirTableau(List<Vehicule> donnees) {
        tableModel.setRowCount(0); // On vide
        for (Vehicule v : donnees) {
            
            // --- SIMULATION DES DONNÉES MANQUANTES ---
            String statutSimule = "En service"; // Pas d'attribut 'etat' dans Vehicule.java
            String ligneSimulee = "-";          // Pas d'attribut 'ligne' dans Vehicule.java
            
            // On affiche Marque + Modèle dans la même colonne
            String modeleComplet = v.getMarque() + " " + v.getModele();
            
            // Formatage date maintenance
            String dateMaint = (v.getDateHeureDerniereMaintenance() != null) 
                    ? v.getDateHeureDerniereMaintenance().toLocalDate().toString() 
                    : "Aucune";

            tableModel.addRow(new Object[]{
                statutSimule,   // Col 0 : Statut (Rond)
                modeleComplet,  // Col 1 : Modèle
                ligneSimulee,   // Col 2 : Ligne
                dateMaint,      // Col 3 : Date
                "✎"             // Col 4 : Icone
            });
        }
    }

    // --- ACTIONS ---

    private void ajouterVehiculeAction() {
        JOptionPane.showMessageDialog(this, "Fonctionnalité 'Ajouter Véhicule' à implémenter.\n" +
                "Il faudra créer un formulaire pour saisir Marque, Modèle, Dates...");
    }

    private void modifierVehiculeAction(int row) {
        // Récupération (attention, il faudrait récupérer l'ID caché idéalement)
        String modele = (String) table.getValueAt(row, 1);
        JOptionPane.showMessageDialog(this, "Modification de : " + modele + "\n(Logique à venir)");
    }

    // --- RENDERERS & DESIGN ---

    // 1. Renderer rond coloré pour le statut
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            label.setText(""); // On cache le texte
            
            // Logique couleur (Simulée pour l'instant car tout est "En service")
            String etat = (String) value;
            Color c = Color.GREEN; 
            if ("En panne".equals(etat)) c = Color.RED;
            
            label.setIcon(new DotIcon(c));
            return label;
        }
    }

    // Icône ronde simple
    class DotIcon implements Icon {
        private Color color;
        public DotIcon(Color c) { this.color = c; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x, y, 10, 10); // Rond de 10px
        }
        public int getIconWidth() { return 10; }
        public int getIconHeight() { return 10; }
    }

    // 2. Renderer Crayon
    class ActionCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            l.setText("✎"); 
            l.setFont(new Font("SansSerif", Font.PLAIN, 18));
            l.setForeground(Color.LIGHT_GRAY);
            l.setHorizontalAlignment(JLabel.CENTER);
            return l;
        }
    }

    // 3. Panneau à bords arrondis
    static class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 20;
        public RoundedPanel(Color bgColor, int radius) {
            this.backgroundColor = bgColor;
            this.cornerRadius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }
}