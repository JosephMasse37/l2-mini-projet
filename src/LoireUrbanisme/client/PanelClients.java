package LoireUrbanisme.client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// IMPORTS MÉTIERS & DAO
import metiers.Client;
import metiers.Abonnement;
import passerelle.ClientDAO;
import passerelle.AbonnementDAO;
import passerelle.Connexion;
import passerelle.DAOException;

public class PanelClients extends JPanel {

    // --- ATTRIBUTS ---
    private JTable table;
    private ClientTableModel tableModel;
    private ClientDAO clientDAO;

    // Champs d'affichage (Détails)
    private JLabel labelNom, labelPrenom, labelAge;
    private JLabel labelAbonnement, labelPrix, labelDuree;

    // Champ de recherche
    private JTextField fieldSearch;

    // Client actuellement sélectionné
    private Client clientSelectionne;

    // --- CONSTRUCTEUR ---
    public PanelClients(ClientDAO DAOReceived) {
        this.clientDAO = DAOReceived;
        this.setLayout(new BorderLayout());

        // 1. Chargement des clients
        List<Client> clients = new ArrayList<>();
        try {
            clients = clientDAO.findAll();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des clients : " + e.getMessage());
        }

        // 2. Création du modèle
        this.tableModel = new ClientTableModel(clients);

        // 3. Création des deux grandes zones
        JPanel panelLeft = createListPanel();
        JPanel panelRight = createDetailPanel();

        // 4. Assemblage avec le SplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeft, panelRight);
        splitPane.setDividerLocation(700); // Largeur de la liste
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        splitPane.setDividerSize(0);

        this.add(splitPane, BorderLayout.CENTER);
    }

    // --- ZONE GAUCHE : LISTE & RECHERCHE ---
    private JPanel createListPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#464646")); // Fond Gris Foncé
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Conteneur Violet (Carte)
        RoundedPanel cardContainer = new RoundedPanel(Color.decode("#E2C2FE"));
        cardContainer.setLayout(new BorderLayout());
        cardContainer.setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- A. BARRE DE RECHERCHE ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Rechercher un client : ");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSearch.setForeground(Color.BLACK);

        fieldSearch = new JTextField(15);
        fieldSearch.setBackground(Color.WHITE);
        fieldSearch.setForeground(Color.BLACK);
        fieldSearch.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE), BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        JButton btnSearch = new JButton("🔍");
        styleButton(btnSearch);

        // Logique de recherche (Entrée ou Clic)
        ActionListener searchAction = e -> effectuerRecherche();
        fieldSearch.addActionListener(searchAction);
        btnSearch.addActionListener(searchAction);

        searchPanel.add(lblSearch);
        searchPanel.add(fieldSearch);
        searchPanel.add(btnSearch);

        // --- B. TABLEAU (CUSTOMISÉ) ---
        // Surcharge pour dessiner la grille même vide (Grille infinie)
        table = new JTable(tableModel) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintEmptyRows(g);
            }
            // Dessine des lignes grises dans le vide
            private void paintEmptyRows(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.decode("#E2C2FE"));
                int rowHeight = getRowHeight();
                int startY = getRowCount() * rowHeight;
                while (startY < getHeight()) {
                    g2.drawLine(0, startY, getWidth(), startY);
                    startY += rowHeight;
                }
            }
        };

        table.setRowHeight(35);

        // 1. SÉLECTION UNIQUE (Empêche la sélection multiple)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 2. COULEUR DE SÉLECTION (Rouge Pastel)
        table.setSelectionBackground(Color.decode("#FF9999"));
        table.setSelectionForeground(Color.BLACK);

        // 3. DESIGN GRILLE & FOND
        table.setShowGrid(false); // On dessine nos propres bordures via le Renderer
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setForeground(Color.BLACK);

        // 4. RENDERER (Centrage + Bordures épaisses)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            int epaisseurLigne = 2;
            Color couleurLigne = Color.decode("#CCCCCC");
            Border border = BorderFactory.createMatteBorder(0, 0, epaisseurLigne, epaisseurLigne, couleurLigne);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(border); // Applique la bordure épaisse
                return this;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // 5. EN-TÊTE (Header)
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#E2C2FE"));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.decode("#CCCCCC")));

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Assemblage
        cardContainer.add(searchPanel, BorderLayout.NORTH);
        cardContainer.add(scrollPane, BorderLayout.CENTER);

        // Listener Sélection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());
                this.clientSelectionne = tableModel.getClientAt(modelIndex);
                showDetails(clientSelectionne);
            }
        });

        mainPanel.add(cardContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    // --- ZONE DROITE : DÉTAILS ---
    private JPanel createDetailPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#464646"));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialisation des labels (Vides au départ)
        labelNom = new JLabel("-"); labelPrenom = new JLabel("-"); labelAge = new JLabel("-");
        labelAbonnement = new JLabel("-"); labelPrix = new JLabel("-"); labelDuree = new JLabel("-");

        // Carte 1 : Identité
        JPanel carteClient = creerCarte("DÉTAILS DU CLIENT", true);
        carteClient.add(creerLigneInfo("Nom : ", labelNom));
        carteClient.add(Box.createVerticalStrut(8));
        carteClient.add(creerLigneInfo("Prénom : ", labelPrenom));
        carteClient.add(Box.createVerticalStrut(8));
        carteClient.add(creerLigneInfo("Âge : ", labelAge));

        // Carte 2 : Contrat (AVEC BOUTON MODIFIER ACTIVÉ)
        JPanel carteContrat = creerCarte("CONTRAT ACTUEL", true);
        carteContrat.add(creerLigneInfo("Formule : ", labelAbonnement));
        carteContrat.add(Box.createVerticalStrut(8));
        carteContrat.add(creerLigneInfo("Prix : ", labelPrix));
        carteContrat.add(Box.createVerticalStrut(8));
        carteContrat.add(creerLigneInfo("Durée : ", labelDuree));

        // Assemblage vertical
        mainPanel.add(carteClient);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(carteContrat);
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    // --- LOGIQUE MÉTIER & ACTIONS ---

    // 1. Afficher les détails à droite
    private void showDetails(Client c) {
        labelNom.setText(c.getNom());
        labelPrenom.setText(c.getPrenom());
        labelAge.setText(c.getAge() + " ans");

        if (c.getUnAbonnement() != null) {
            labelAbonnement.setText(c.getUnAbonnement().getFormule());
            labelPrix.setText(c.getUnAbonnement().getPrix() + " €");
            labelDuree.setText(c.getUnAbonnement().getDuree());
        } else {
            labelAbonnement.setText("Aucun");
            labelPrix.setText("-");
            labelDuree.setText("-");
        }
    }

    // 2. Modifier le Nom du Client
    private void modifierClientAction() {
        if (clientSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client.");
            return;
        }
        String nouveauNom = JOptionPane.showInputDialog(this, "Modifier le nom :", clientSelectionne.getNom());

        if (nouveauNom != null && !nouveauNom.isBlank()) {
            try {
                clientSelectionne.setNom(nouveauNom);
                // clientDAO.update(clientSelectionne); // TODO: Décommenter quand le DAO aura la méthode update
                tableModel.fireTableDataChanged();
                showDetails(clientSelectionne);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        }
    }

    // 3. Modifier l'Abonnement (NOUVEAU !)
    private void modifierAbonnementAction() {
        if (clientSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client.");
            return;
        }

        try {
            // Récupérer la liste des abonnements via le DAO
            AbonnementDAO aboDAO = new AbonnementDAO(Connexion.getConnexion());
            List<Abonnement> lesAbonnements = aboDAO.findAll();

            if (lesAbonnements.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun abonnement trouvé en base.");
                return;
            }

            // Convertir en tableau pour la ComboBox
            Abonnement[] choixAbos = lesAbonnements.toArray(new Abonnement[0]);
            JComboBox<Abonnement> comboBox = new JComboBox<>(choixAbos);

            // Pré-sélectionner l'abonnement actuel
            if (clientSelectionne.getUnAbonnement() != null) {
                // On tente de sélectionner l'abonnement qui a le même ID
                for (Abonnement a : choixAbos) {
                    if (a.getIdAbonnement() == clientSelectionne.getUnAbonnement().getIdAbonnement()) {
                        comboBox.setSelectedItem(a);
                        break;
                    }
                }
            }

            // Afficher la popup
            int retour = JOptionPane.showConfirmDialog(this,
                    comboBox,
                    "Choisir la nouvelle formule :",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // Validation
            if (retour == JOptionPane.OK_OPTION) {
                Abonnement nouveauAbo = (Abonnement) comboBox.getSelectedItem();
                if (nouveauAbo != null) {
                    clientSelectionne.setUnAbonnement(nouveauAbo);
                    // clientDAO.update(clientSelectionne); // TODO: Sauvegarder en BDD
                    
                    tableModel.fireTableDataChanged(); // Met à jour le tableau
                    showDetails(clientSelectionne);    // Met à jour la fiche
                    
                    JOptionPane.showMessageDialog(this, "Abonnement modifié !");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des abonnements : " + e.getMessage());
        }
    }

    // 4. Recherche
    private void effectuerRecherche() {
        String texte = fieldSearch.getText().trim().toLowerCase();
        if (texte.isEmpty()) return;

        boolean trouve = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            // On suppose colonne 1 = Nom, 2 = Prénom
            String nom = table.getValueAt(i, 1).toString().toLowerCase();
            String prenom = table.getValueAt(i, 2).toString().toLowerCase();

            if (nom.contains(texte) || prenom.contains(texte)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                trouve = true;
                break;
            }
        }
        if (!trouve) {
            JOptionPane.showMessageDialog(this, "Aucun client trouvé pour : " + texte);
        }
    }

    // --- MÉTHODES UTILITAIRES DE DESIGN ---

    private JPanel creerCarte(String titre, boolean avecBoutonModifier) {
        RoundedPanel carte = new RoundedPanel(Color.decode("#E2C2FE"));
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setBorder(new EmptyBorder(15, 20, 15, 20));
        carte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JPanel entete = new JPanel(new BorderLayout());
        entete.setOpaque(false);

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitre.setForeground(Color.BLACK);
        entete.add(lblTitre, BorderLayout.WEST);

        if (avecBoutonModifier) {
            JButton btnModif = new JButton("MODIFIER");
            styleButton(btnModif);
            
            // On branche la bonne action selon le titre de la carte
            if (titre.contains("CLIENT")) {
                btnModif.addActionListener(e -> modifierClientAction());
            } else {
                btnModif.addActionListener(e -> modifierAbonnementAction());
            }
            entete.add(btnModif, BorderLayout.EAST);
        }

        carte.add(entete);
        carte.add(Box.createVerticalStrut(15));
        return carte;
    }

    private JPanel creerLigneInfo(String libelle, JLabel labelValeur) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ligne.setOpaque(false);
        JLabel lblKey = new JLabel(libelle);
        lblKey.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblKey.setForeground(Color.decode("#2c2c2c"));
        
        labelValeur.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelValeur.setForeground(Color.BLACK);

        ligne.add(lblKey);
        ligne.add(labelValeur);
        return ligne;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- CLASSE INTERNE POUR LES ANGLES ARRONDIS ---
    static class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 30;
        public RoundedPanel(Color bgColor) {
            this.backgroundColor = bgColor;
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