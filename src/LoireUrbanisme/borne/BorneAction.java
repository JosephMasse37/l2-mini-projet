package LoireUrbanisme.borne;

import LoireUrbanisme.borne.ButtonEditor;
import LoireUrbanisme.borne.ButtonRenderer;
import metiers.*;
import passerelle.ArretDAO;
import passerelle.BorneDAO;
import passerelle.Connexion;
import passerelle.DAOException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

public class BorneAction {
    public static BorneDAO borneDAO = new BorneDAO(Connexion.getConnexion());
    public static ArretDAO arretDAO = new ArretDAO(Connexion.getConnexion());

    public static void choixBornesUnArret(String nomArret, JPanel parentPanel) {
        try {
            Arret lArret = arretDAO.findByNom(nomArret);

            List<Borne> bornes = borneDAO.getBornesUnArret(lArret.getIdArret());

            // Création du tableau
            String[] colonnes = {"N° Borne", "nom de l'arrêt", "Nombre de tickets vendus", "Nombre de voyages vendues", "Actions"};

            Object[][] donnees = new Object[bornes.size()][6];

            for (int i = 0; i < bornes.size(); i++) {
                Borne b = bornes.get(i);
                donnees[i][0] = b.getIdBorne();
                donnees[i][1] = b.getArret().getNom();
                donnees[i][2] = b.getNbVentesTickets();
                donnees[i][3] = b.getNbVoyageVendu();
                donnees[i][4] = ""; // colonne boutons
            }

            JTable tableau = new JTable(donnees, colonnes) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4;
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
                        c.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // applique la police directement
                        setHorizontalAlignment(SwingConstants.CENTER);      // centre le texte
                        return c;
                    }
                });
            }

            tableau.setShowGrid(true);
            tableau.setGridColor(Color.LIGHT_GRAY);

            tableau.getTableHeader().setBackground(new Color(30, 30, 30));
            tableau.getTableHeader().setForeground(Color.WHITE);
            tableau.getTableHeader().setFont(new Font("Segio UI", Font.BOLD, 18));

            // Centrer le texte dans toutes les colonnes Object directement
            tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                {
                    setHorizontalAlignment(SwingConstants.CENTER); // centre horizontalement
                    setVerticalAlignment(SwingConstants.CENTER);   // centre verticalement si besoin
                }
            });

            JPanel bornesPanel = new JPanel();
            bornesPanel.setLayout(new BorderLayout(10, 10));
            bornesPanel.setOpaque(false);

            // Ajouter les boutons
            tableau.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
            tableau.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(tableau, bornesPanel));

            JLabel titre = new JLabel("Bornes de l'arrêt " + nomArret);
            titre.setFont(new Font("Segoe UI", Font.BOLD, 26));
            titre.setHorizontalAlignment(SwingConstants.CENTER);

            JScrollPane scrollPane = new JScrollPane(tableau);
            scrollPane.setPreferredSize(new Dimension(900, 400));
            scrollPane.getViewport().setOpaque(false);

            bornesPanel.add(titre, BorderLayout.NORTH);
            bornesPanel.add(scrollPane, BorderLayout.CENTER);

            // Affichage du tableau
            JOptionPane.showConfirmDialog(parentPanel, bornesPanel,
                    "Modifier la borne",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
        } catch (DAOException e) {
            new JLabel("Erreur chargement Bornes");
        }
    }

    public static void editBorne(int idBorne, JPanel parentPanel) throws DAOException {
        Borne b = borneDAO.find(idBorne);

        if (b == null) {
            JOptionPane.showMessageDialog(parentPanel,
                    "Borne introuvable.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(9999);
        formatter.setAllowsInvalid(false);

        JFormattedTextField nbTickets = new JFormattedTextField(formatter);
        nbTickets.setValue(b.getNbVentesTickets());


        JFormattedTextField nbVoyages = new JFormattedTextField(formatter);
        nbVoyages.setValue(b.getNbVoyageVendu());

        // Construire le panneau pour la boîte de dialogue
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Borne :"));
        panel.add(new JLabel("N° " + b.getIdBorne() + " à l'arrêt " + b.getArret().getNom()));

        panel.add(new JLabel("Nombre de tickets vendus :"));
        panel.add(nbTickets);

        panel.add(new JLabel("Nombre de voyages vendus :"));
        panel.add(nbVoyages);

        int result = JOptionPane.showConfirmDialog(parentPanel, panel,
                "Modifier la conduite",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Récupérer les nouvelles valeurs
            int newNbTicketsVendus = (Integer) nbTickets.getValue();
            int newNbVoyagesVendus = (Integer) nbVoyages.getValue();

            // Appliquer les modifications
            b.setNbVoyageVendu(newNbTicketsVendus);
            b.setNbVentesTickets(newNbVoyagesVendus);

            // Sauvegarder en base
            boolean r1 = borneDAO.update(b);

            if (r1) {
                JOptionPane.showMessageDialog(parentPanel,
                        "La borne a été modifiée avec succès.",
                        "Modification réussie",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentPanel,
                        "La borne n'a pas pu être modifiée.",
                        "Modification échouée",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void deleteBorne(int idBorne, JPanel parentPanel) throws DAOException {
        Borne b = borneDAO.find(idBorne);
        boolean result = borneDAO.delete(b);
        // Affiche un message de confirmation
        if (result) {
            JOptionPane.showMessageDialog(
                    parentPanel,
                    "La borne a été supprimée avec succès.",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    parentPanel,
                    "La borne n'a pas pu être supprimée.",
                    "Suppression échouée",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
