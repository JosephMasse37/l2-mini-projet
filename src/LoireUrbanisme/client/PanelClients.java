package LoireUrbanisme.client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import metiers.Client;
import passerelle.ClientDAO;
import passerelle.DAOException;


public class PanelClients extends JPanel {
    private JTable table;
    private ClientTableModel tableModel;

    private ClientDAO clientDAO;

    private JLabel labelNom;
    private JLabel labelPrenom;
    private JLabel labelAge;
    private JLabel labelAbonnement;
    private JLabel labelPrix;
    private JLabel labelDuree;

    private JTextField fieldSearch;

    public PanelClients(ClientDAO DAOReceived) throws DAOException {
        this.clientDAO = DAOReceived;

        this.setLayout (new BorderLayout());

        //Extract clients from database
        List<Client> clients = new ArrayList<>();
        try {
            clients = clientDAO.findAll();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des clients : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        //Create the table model
        this.tableModel = new ClientTableModel(clients);

        //Create two panels
        JPanel panelLeft = createListPanel();
        JPanel panelRight = createDetailPanel();

        //Add the two panels to the main panel (using SplitPane to add dynamic resizing)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeft, panelRight);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);
        this.add(splitPane, BorderLayout.CENTER);

    }

        //Left Panel : List of clients and search bar
        private JPanel createListPanel() {
            JPanel panel = new JPanel(new BorderLayout());

            //Search bar at the top
            JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fieldSearch = new JTextField(20);
            JButton buttonSearch = new JButton("🔍");
            panelSearch.add(new JLabel("Rechercher : "));
            panelSearch.add(fieldSearch);
            panelSearch.add(buttonSearch);

            //Table in the center
            table = new JTable(tableModel);
            table.setRowHeight(25);
            //select one line at a time
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

            //Add action listener
            table.getSelectionModel().addListSelectionListener(e -> {
                //Assure that the event is only processed once
                if(!e.getValueIsAdjusting()) {
                        int selectedRow = table.getSelectedRow();
                        if(selectedRow != -1) {
                            Client c = tableModel.getClientAt(table.convertRowIndexToModel(selectedRow));
                            showDetails(c);
                    }
                }
            });

            //Structure panel
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(panelSearch, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        //Right Panel : Details of the selected client
        private JPanel createDetailPanel() {
            //Initialize panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.decode("#e2c2fe"));

            //Initialize labels
            labelNom = new JLabel("Nom : -");
            labelPrenom = new JLabel("Prénom : -");
            labelAge = new JLabel("Age : -");
            labelAbonnement = new JLabel("Abonnement : -");

            //Client infos
            panel.add(createTitle("Détails du client"));
            panel.add(labelNom);
            panel.add(Box.createVerticalStrut(10));
            panel.add(labelPrenom);
            panel.add(Box.createVerticalStrut(10));
            panel.add(labelAge);
            panel.add(Box.createVerticalStrut(30));

            //Abonnement infos
            panel.add(createTitle("Abonnement du client"));
            panel.add(Box.createVerticalStrut(10));
            panel.add(labelAbonnement);
            panel.add(Box.createVerticalStrut(10));
            panel.add(labelPrix);
            panel.add(Box.createVerticalStrut(10));
            panel.add(labelDuree);

            //Set to the top
            panel.add(Box.createVerticalGlue());

            return panel;
        }

        private void showDetails(Client c) {
            labelNom.setText(c.getNom());
            labelPrenom.setText(c.getPrenom());
            labelAge.setText(String.valueOf(c.getAge()));
            if(c.getUnAbonnement() != null) {
                labelAbonnement.setText(c.getUnAbonnement().getFormule());
                labelPrix.setText(String.valueOf(c.getUnAbonnement().getPrix()));
                labelDuree.setText(String.valueOf(c.getUnAbonnement().getDuree()));
            } else {
                labelAbonnement.setText("Aucun");
            }
        }

        private Component createTitle(String string) {
            JLabel label = new JLabel(string);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            return label;
        }

}
