package LoireUrbanisme.borne;

import LoireUrbanisme.chauffeurs.AjouterChauffeurDialog;
import metiers.Arret;
import passerelle.DAOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BornePanel extends JPanel {

    private JLabel lblVoyages;
    private JLabel lblTickets;
    private JLabel lblBornes;
    private JList<String> listeArrets;

    public BornePanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setOpaque(false);

        add(titre(), BorderLayout.NORTH);
        add(contenuCentral(), BorderLayout.CENTER);

        try {
            chargerDonnees();
        } catch (DAOException e) {
            afficherErreur();
        }
    }

    private JComponent titre() {
        JLabel titre = new JLabel("BORNE – STATISTIQUES RÉSEAU");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        return titre;
    }

    private JComponent contenuCentral() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setOpaque(false);

        lblBornes = new JLabel("-");
        lblVoyages = new JLabel("-");
        lblTickets = new JLabel("-");

        panel.add(new StatCard("Bornes actives", lblBornes));
        panel.add(new StatCard("Voyages vendus", lblVoyages));
        panel.add(new StatCard("Tickets vendus", lblTickets));

        panel.add(carteListeArrets());


        return panel;
    }

    private JComponent carteListeArrets() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(40, 40, 40));

        JLabel titre = new JLabel("Arrêts équipés d'une borne");
        titre.setForeground(Color.LIGHT_GRAY);

        listeArrets = new JList<>();
        listeArrets.setCellRenderer(new ArretListRenderer());
        listeArrets.setFixedCellHeight(40);
        listeArrets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listeArrets.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String value = listeArrets.getSelectedValue();
                if (value != null) {
                    BorneAction.choixBornesUnArret(value, this);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listeArrets);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(30, 30, 30));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        listeArrets.setBackground(new Color(30, 30, 30));
        listeArrets.setForeground(Color.WHITE);

        panel.add(titre, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(addButton(), BorderLayout.SOUTH);

        return panel;
    }

    private void chargerDonnees() throws DAOException {
        BorneService service = new BorneService();

        lblBornes.setText(String.valueOf(service.nombreBornes()));
        lblVoyages.setText(String.valueOf(service.totalVoyages()));
        lblTickets.setText(String.valueOf(service.totalTickets()));

        DefaultListModel<String> model = new DefaultListModel<>();
        for (Arret a : service.arretsAvecBorne()) {
            model.addElement(a.getNom());
        }
        listeArrets.setModel(model);
    }

    private void afficherErreur() {
        lblBornes.setText("ERR");
        lblVoyages.setText("ERR");
        lblTickets.setText("ERR");

        JOptionPane.showMessageDialog(
                this,
                "Impossible de charger les données de la borne.",
                "Erreur base de données",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private Component addButton() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false); // garde le fond sombre
        JButton addButton = new JButton("Ajouter une borne") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Remplir le fond avec couleur
                g2.setColor(new Color(165, 55, 255)); // violet
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

        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        bottomPanel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                AjouterBorneDialog.show(this);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'ajout de la borne",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return bottomPanel;
    }

    public void refreshPanel() {
        removeAll();

        add(titre(), BorderLayout.NORTH);
        add(contenuCentral(), BorderLayout.CENTER);

        try {
            chargerDonnees();
        } catch (DAOException e) {
            afficherErreur();
        }

        revalidate();
        repaint();
    }
}
