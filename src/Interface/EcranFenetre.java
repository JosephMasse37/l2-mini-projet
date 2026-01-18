package Interface;

import LoireUrbanisme.chauffeurs.Chauffeurs;
import LoireUrbanisme.client.PanelClients;
import LoireUrbanisme.vehicule.PanelVehicules;
import LoireUrbanisme.conduites.Conduites;
import LoireUrbanisme.map.Map;
import LoireUrbanisme.menu.MenuAction;
import LoireUrbanisme.menu.MenuEvent;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import LoireUrbanisme.menu.Menu;
import metiers.Arret;
import metiers.Ligne;
import metiers.Utilisateur;
import metiers.Vehicule;
import passerelle.*;

public class EcranFenetre extends JFrame implements MenuEvent {
    private Menu menu;
    private JPanel zoneContenu;

    private Connection connexion = Connexion.getConnexion();
    private ArretDAO arretDAO = new ArretDAO(connexion);
    private LigneDAO ligneDAO = new LigneDAO(connexion);
    private DessertDAO dessertDAO = new DessertDAO(connexion);
    private ClientDAO clientDAO = new ClientDAO(connexion);
    private VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

    public EcranFenetre(Utilisateur utilisateurConnecte) {
        setTitle("LoireUrbanisme - Système de Gestion des Transports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1720, 980);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        menu = new Menu(utilisateurConnecte, this);
        menu.addMenuEvent(this);
        zoneContenu = new JPanel(new BorderLayout(15, 15));

        add(menu, BorderLayout.WEST);
        add(zoneContenu, BorderLayout.CENTER);

        afficherVueEnsemble();
    }

    @Override
    public void menuSelected(int index, int subIndex, MenuAction action) {

        zoneContenu.removeAll();

        switch (index) {
            case 0:
                afficherVueEnsemble();
                break;

            case 1:
                if (subIndex == 1) {
                    zoneContenu.add(new JLabel("Bus"));
                } else if (subIndex == 2) {
                    zoneContenu.add(new JLabel("Tram"));
                }
                break;

            case 2:
                zoneContenu.add(new PanelClients(clientDAO), BorderLayout.CENTER);
                break;

            case 4:
                zoneContenu.add(new PanelVehicules(vehiculeDAO), BorderLayout.CENTER);
                break;

            case 5:
                Chauffeurs chauffeurs = new Chauffeurs(menu);

                zoneContenu.add(chauffeurs, BorderLayout.CENTER);
                break;

            case 6:
                Conduites conduites = new Conduites();

                zoneContenu.add(conduites, BorderLayout.CENTER);
                break;

            case 7:
                afficherMap();
                break;

            default:
                zoneContenu.add(new JLabel("Vue d'ensemble"));
                break;
        }

        zoneContenu.revalidate();
        zoneContenu.repaint();
    }

    private void afficherVueEnsemble() {
        zoneContenu.add(new JLabel("Vue d'ensemble"));
    }

    private void afficherMap() {
        Map map = new Map();

        List<Arret> arrets;
        List<Ligne> lignes;
        try {
            arrets = arretDAO.findAll();
            lignes = ligneDAO.findAll();
            for (Ligne l : lignes) {
                l.setArretsDesservis(dessertDAO.getDessertesUneLigne(l.getIdLigne()));
            }
            map.afficherReseau(arrets, lignes);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        // --- Menu flottant ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(255, 255, 255, 160));
        menuPanel.setOpaque(true);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // --- Radio "Toutes les lignes" ---
        JRadioButton allLinesRadio = new JRadioButton("Toutes les lignes");
        allLinesRadio.setForeground(Color.BLACK);
        allLinesRadio.setBackground(new Color(255, 255, 255, 0));
        allLinesRadio.setOpaque(false);
        allLinesRadio.setFocusPainted(false);
        allLinesRadio.setSelected(true);
        menuPanel.add(allLinesRadio);

        // --- Checkboxes pour chaque ligne ---
        List<JCheckBox> ligneCheckBoxes = new ArrayList<>();
        for (Ligne ligne : lignes) {
            JCheckBox checkBox = new JCheckBox("Ligne " + ligne.getLibelle());
            checkBox.setForeground(Color.BLACK);
            checkBox.setBackground(new Color(255, 255, 255, 0));
            checkBox.setOpaque(false);
            checkBox.setFocusPainted(false);
            menuPanel.add(checkBox);
            ligneCheckBoxes.add(checkBox);

            checkBox.addActionListener(e -> {
                // Décoche le radio si au moins une checkbox est cochée
                if (checkBox.isSelected()) {
                    allLinesRadio.setSelected(false);
                }

                mettreAJourSelection(map, lignes, ligneCheckBoxes, allLinesRadio, arrets);
            });
        }

        // Action radio "Toutes les lignes"
        allLinesRadio.addActionListener(e -> {
            if (allLinesRadio.isSelected()) {
                // Décocher toutes les checkboxes
                for (JCheckBox cb : ligneCheckBoxes) {
                    cb.setSelected(false);
                }
                mettreAJourSelection(map, lignes, ligneCheckBoxes, allLinesRadio, arrets);
            }
        });

        // --- LayeredPane pour menu flottant ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        map.setBounds(0, 0, zoneContenu.getWidth(), zoneContenu.getHeight());
        layeredPane.add(map, JLayeredPane.DEFAULT_LAYER);

        int menuWidth = 180;
        int menuHeight = menuPanel.getPreferredSize().height;
        menuPanel.setBounds(layeredPane.getWidth() - menuWidth - 10, 10, menuWidth, menuHeight);
        layeredPane.add(menuPanel, JLayeredPane.PALETTE_LAYER);

        // Gestion redimensionnement
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                map.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                menuPanel.setBounds(layeredPane.getWidth() - menuWidth - 10, 10, menuWidth, menuHeight);
            }
        });

        zoneContenu.add(layeredPane, BorderLayout.CENTER);
    }

    private void mettreAJourSelection(Map map, List<Ligne> lignes,
                                      List<JCheckBox> ligneCheckBoxes,
                                      JRadioButton allLinesRadio,
                                      List<Arret> arrets) {
        List<Ligne> selectionnees = new ArrayList<>();
        for (int i = 0; i < lignes.size(); i++) {
            if (ligneCheckBoxes.get(i).isSelected()) {
                selectionnees.add(lignes.get(i));
            }
        }

        if (selectionnees.isEmpty()) {
            allLinesRadio.setSelected(true);
            map.setLignesSelectionnees(null); // toutes les lignes
        } else {
            map.setLignesSelectionnees(selectionnees);
        }
        map.afficherReseau(arrets, lignes);
    }
}
