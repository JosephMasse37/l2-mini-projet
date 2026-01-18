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

// Imports metiers and DAO
import metiers.Client;
import metiers.Abonnement;
import passerelle.ClientDAO;
import passerelle.AbonnementDAO;
import passerelle.Connexion;
import passerelle.DAOException;

public class PanelClients extends JPanel {

    //Attributes
    private JTable table;
    private ClientTableModel tableModel;
    private ClientDAO clientDAO;

    //Details
    private JLabel labelNom, labelPrenom, labelAge;
    private JLabel labelAbonnement, labelPrix, labelDuree;

    //SearchField
    private JTextField fieldSearch;

    //Current Selected Client
    private Client clientSelectionne;

    //Constructor
    public PanelClients(ClientDAO DAOReceived) {
        this.clientDAO = DAOReceived;
        this.setLayout(new BorderLayout());

        //Loading clients from DB
        List<Client> clients = new ArrayList<>();
        try {
            clients = clientDAO.findAll();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des clients : " + e.getMessage());
        }

        //Creation of the table model
        this.tableModel = new ClientTableModel(clients);

        //Creation of sub-panels
        JPanel panelLeft = createListPanel();
        JPanel panelRight = createDetailPanel();
        JPanel panelBottom = createBottomPanel(); // Nouveau bouton Ajouter

        //Creation of the separator
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeft, panelRight);
        splitPane.setDividerLocation(700);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        splitPane.setDividerSize(0);

        this.add(splitPane, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
    }

    //Left panel : client list and search bar
    private JPanel createListPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#464646"));
        mainPanel.setBorder(new EmptyBorder(20, 20, 0, 20));

        //Purple panel
        RoundedPanel cardContainer = new RoundedPanel(Color.decode("#E2C2FE"));
        cardContainer.setLayout(new BorderLayout());
        cardContainer.setBorder(new EmptyBorder(15, 15, 15, 15));

        //Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Rechercher (ID, Nom ou Prénom) : ");
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSearch.setForeground(Color.BLACK);

        fieldSearch = new JTextField(20);
        fieldSearch.setBackground(Color.WHITE);
        fieldSearch.setForeground(Color.BLACK); // Correction Couleur Texte
        fieldSearch.setCaretColor(Color.BLACK); // Curseur visible
        fieldSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE), 
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        
        JButton btnSearch = new JButton("🔍");
        styleButton(btnSearch);

        //Search function
        ActionListener searchAction = e -> effectuerRecherche();
        fieldSearch.addActionListener(searchAction);
        btnSearch.addActionListener(searchAction);

        searchPanel.add(lblSearch);
        searchPanel.add(fieldSearch);
        searchPanel.add(btnSearch);

        //Table creation with customization
        table = new JTable(tableModel) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintEmptyRows(g);
            }
            private void paintEmptyRows(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.decode("#CCCCCC"));
                int rowHeight = getRowHeight();
                int startY = getRowCount() * rowHeight;
                while (startY < getHeight()) {
                    g2.drawLine(0, startY, getWidth(), startY);
                    startY += rowHeight;
                }
            }
        };

        // Table styling
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.decode("#FF9999"));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setForeground(Color.BLACK);

        //Center cell with purple borders
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            Border border = BorderFactory.createMatteBorder(0, 0, 2, 2, Color.decode("#CCCCCC"));
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(border);
                return this;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        //Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#E2C2FE"));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.decode("#CCCCCC")));

        //Adding a ScrollPane to be able to scroll if the table has more content than the visible area
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        //Assembling
        cardContainer.add(searchPanel, BorderLayout.NORTH);
        cardContainer.add(scrollPane, BorderLayout.CENTER);

        //Selection listener to update details panel
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

    //Right panel : client and abonnement details
    private JPanel createDetailPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#464646"));
        mainPanel.setBorder(new EmptyBorder(20, 20, 0, 20));

        labelNom = new JLabel("-"); labelPrenom = new JLabel("-"); labelAge = new JLabel("-");
        labelAbonnement = new JLabel("-"); labelPrix = new JLabel("-"); labelDuree = new JLabel("-");

        //Client card
        JPanel carteClient = creerCarte("DÉTAILS DU CLIENT", true);
        carteClient.add(creerLigneInfo("Nom : ", labelNom));
        carteClient.add(Box.createVerticalStrut(8));
        carteClient.add(creerLigneInfo("Prénom : ", labelPrenom));
        carteClient.add(Box.createVerticalStrut(8));
        carteClient.add(creerLigneInfo("Âge : ", labelAge));

        //Contract card
        JPanel carteContrat = creerCarte("CONTRAT ACTUEL", true);
        carteContrat.add(creerLigneInfo("Formule : ", labelAbonnement));
        carteContrat.add(Box.createVerticalStrut(8));
        carteContrat.add(creerLigneInfo("Prix : ", labelPrix));
        carteContrat.add(Box.createVerticalStrut(8));
        carteContrat.add(creerLigneInfo("Durée : ", labelDuree));

        mainPanel.add(carteClient);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(carteContrat);
        mainPanel.add(Box.createVerticalGlue()); //Push cards to the top

        return mainPanel;
    }

    //Low panel : add client button
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setBackground(Color.decode("#464646"));

        //Add Client Button styling and action
        JButton btnAjouter = new JButton("+ AJOUTER CLIENT");
        btnAjouter.setBackground(Color.decode("#E2C2FE"));
        btnAjouter.setForeground(Color.BLACK);
        btnAjouter.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAjouter.setFocusPainted(false);
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAjouter.addActionListener(e -> ajouterClientAction());

        panel.add(btnAjouter);
        return panel;
    }

    //Functions

    //Search
    private void effectuerRecherche() {
        String texte = fieldSearch.getText().trim().toLowerCase();
        if (texte.isEmpty()) return;

        boolean isRechercheID = texte.matches("\\d+");
        int idRecherche = -1;
        if (isRechercheID) idRecherche = Integer.parseInt(texte);

        boolean trouve = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            int modelIndex = table.convertRowIndexToModel(i);
            Client c = tableModel.getClientAt(modelIndex);
            boolean correspondance = false;

            if (isRechercheID) {
                if (c.getIdClient() == idRecherche) correspondance = true;
            } else {
                String nom = c.getNom().toLowerCase();
                String prenom = c.getPrenom().toLowerCase();
                if (nom.contains(texte) || prenom.contains(texte) || 
                    (nom + " " + prenom).contains(texte) || (prenom + " " + nom).contains(texte)) {
                    correspondance = true;
                }
            }

            if (correspondance) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                trouve = true;
                break;
            }
        }
        if (!trouve) JOptionPane.showMessageDialog(this, "Aucun client trouvé.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    //Display details of selected client
    private void showDetails(Client c) {
        labelNom.setText(c.getNom());
        labelPrenom.setText(c.getPrenom());
        labelAge.setText(c.getAge() + " ans"); // Age stocké en int

        if (c.getUnAbonnement() != null) {
            labelAbonnement.setText(c.getUnAbonnement().getFormule());
            labelPrix.setText(c.getUnAbonnement().getPrix() + " €");
            // toString() par défaut ou méthode spécifique si elle existe
            labelDuree.setText(String.valueOf(c.getUnAbonnement().getDuree())); 
        } else {
            labelAbonnement.setText("Aucun");
            labelPrix.setText("-");
            labelDuree.setText("-");
        }
    }

    //Modify Client with delete option
    private void modifierClientGlobalAction() {
        if (clientSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client.");
            return;
        }

        // Création du formulaire avec les valeurs actuelles
        JTextField txtNom = new JTextField(clientSelectionne.getNom());
        JTextField txtPrenom = new JTextField(clientSelectionne.getPrenom());
        JTextField txtAge = new JTextField(String.valueOf(clientSelectionne.getAge()));

        Object[] message = {
            "Nom :", txtNom,
            "Prénom :", txtPrenom,
            "Âge :", txtAge
        };

        // Définition des boutons personnalisés
        Object[] options = {"Enregistrer", "Supprimer", "Annuler"};

        // Affichage de la boîte de dialogue avec OptionDialog
        int choix = JOptionPane.showOptionDialog(this, 
                message, 
                "Modifier Client", 
                JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                options, 
                options[0]);

        // --- CAS 1 : ENREGISTRER (Index 0) ---
        if (choix == 0) {
            try {
                String nom = txtNom.getText().trim();
                String prenom = txtPrenom.getText().trim();
                int age = Integer.parseInt(txtAge.getText().trim());

                if (nom.isEmpty() || prenom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom et le prénom ne peuvent pas être vides.");
                    return;
                }

                clientSelectionne.setNom(nom);
                clientSelectionne.setPrenom(prenom);
                clientSelectionne.setAge(age);

                clientDAO.update(clientSelectionne); // Mise à jour BDD
                
                tableModel.fireTableDataChanged();
                showDetails(clientSelectionne);
                JOptionPane.showMessageDialog(this, "Client modifié avec succès !");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "L'âge doit être un nombre entier.");
            } catch (DAOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + e.getMessage());
            }
        }
        
        // --- CAS 2 : SUPPRIMER (Index 1) ---
        else if (choix == 1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Êtes-vous sûr de vouloir supprimer le client " + clientSelectionne.getNom() + " ?", 
                    "Confirmation Suppression", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    clientDAO.delete(clientSelectionne);
                    
                    // Rafraîchissement complet
                    List<Client> maj = clientDAO.findAll();
                    this.tableModel = new ClientTableModel(maj);
                    table.setModel(this.tableModel);
                    // Réapplication du renderer centré
                    table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Object.class));
                    
                    // Reset de l'affichage à droite
                    clientSelectionne = null;
                    labelNom.setText("-"); labelPrenom.setText("-"); labelAge.setText("-");
                    labelAbonnement.setText("-"); labelPrix.setText("-"); labelDuree.setText("-");
                    
                    JOptionPane.showMessageDialog(this, "Client supprimé.");
                    
                } catch (DAOException e) {
                    JOptionPane.showMessageDialog(this, "Impossible de supprimer (Probablement lié à des données existantes) : " + e.getMessage());
                }
            }
        }
    }

    //Modify Abonnement
    private void modifierAbonnementAction() {
        if (clientSelectionne == null) return;
        try {
            AbonnementDAO aboDAO = new AbonnementDAO(Connexion.getConnexion());
            List<Abonnement> liste = aboDAO.findAll();
            if(liste.isEmpty()) return;
            
            JComboBox<Abonnement> cb = new JComboBox<>(liste.toArray(new Abonnement[0]));
            if(clientSelectionne.getUnAbonnement() != null) {
                for(Abonnement a : liste) {
                    if(a.getIdAbonnement() == clientSelectionne.getUnAbonnement().getIdAbonnement()) cb.setSelectedItem(a);
                }
            }
            if(JOptionPane.showConfirmDialog(this, cb, "Choix Abonnement", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                clientSelectionne.setUnAbonnement((Abonnement)cb.getSelectedItem());
                clientDAO.update(clientSelectionne);
                tableModel.fireTableDataChanged();
                showDetails(clientSelectionne);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    //Add Client
    private void ajouterClientAction() {
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtAge = new JTextField();
        JComboBox<Abonnement> cbAbonnement = new JComboBox<>();

        //Load abonnements from DB
        try {
            AbonnementDAO aboDAO = new AbonnementDAO(Connexion.getConnexion());
            List<Abonnement> listeAbos = aboDAO.findAll();
            for (Abonnement abo : listeAbos) {
                cbAbonnement.addItem(abo);
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement abonnements: " + e.getMessage());
        }

        Object[] message = {
            "Nom :", txtNom,
            "Prénom :", txtPrenom,
            "Âge :", txtAge,
            "Abonnement :", cbAbonnement
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ajouter Nouveau Client", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nom = txtNom.getText().trim();
                String prenom = txtPrenom.getText().trim();
                int age = Integer.parseInt(txtAge.getText().trim());
                Abonnement abonnement = (Abonnement) cbAbonnement.getSelectedItem();

                //ID put to 0 since it's auto-incremented in DB
                Client newClient = new Client(0, nom, prenom, age, abonnement);
                
                clientDAO.create(newClient);
                
                //Refresh table
                List<Client> maj = clientDAO.findAll();
                //Create a new model to simplyfy data refresh
                this.tableModel = new ClientTableModel(maj);
                table.setModel(this.tableModel);
                
                //Reapply the renders
                table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Object.class)); 
                
                JOptionPane.showMessageDialog(this, "Client ajouté !");

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Erreur : L'âge doit être un entier.");
            } catch (DAOException e) {
                JOptionPane.showMessageDialog(this, "Erreur BDD : " + e.getMessage());
            }
        }
    }

    //Design methods

    private JPanel creerCarte(String titre, boolean btn) {
        RoundedPanel p = new RoundedPanel(Color.decode("#E2C2FE"));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(15,20,15,20));

        JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
        JLabel l = new JLabel(titre); l.setFont(new Font("SansSerif", Font.BOLD, 14)); l.setForeground(Color.BLACK);
        h.add(l, BorderLayout.WEST);
        
        if(btn) {
            JButton b = new JButton("MODIFIER"); styleButton(b);
            //Depending of the card activate a different action
            if(titre.contains("CLIENT")) b.addActionListener(e->modifierClientGlobalAction());
            else b.addActionListener(e->modifierAbonnementAction());
            h.add(b, BorderLayout.EAST);
        }
        
        p.add(h); p.add(Box.createVerticalStrut(15));
        
        //Structure the right panel cards
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height)); 
        
        return p;
    }

    //Create info line for details cards
    private JPanel creerLigneInfo(String k, JLabel v) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); p.setOpaque(false);
        JLabel lk = new JLabel(k); lk.setForeground(Color.decode("#2c2c2c")); lk.setFont(new Font("SansSerif",Font.PLAIN,14));
        v.setForeground(Color.BLACK); v.setFont(new Font("SansSerif",Font.BOLD,14));
        p.add(lk); p.add(v); return p;
    }

    //Button styling
    private void styleButton(JButton b) {
        b.setBackground(Color.WHITE); b.setForeground(Color.BLACK); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE), BorderFactory.createEmptyBorder(5,15,5,15)));
    }

    //Rounded panel class
    static class RoundedPanel extends JPanel {
        private Color bg; public RoundedPanel(Color c) {bg=c; setOpaque(false);}
        protected void paintComponent(Graphics g) {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(bg); g.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
        }
    }
}