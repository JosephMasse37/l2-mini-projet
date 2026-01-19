package Interface;

import LoireUrbanisme.ReseauTrafic.ReseauTrafic;
import LoireUrbanisme.menu.MenuAction;
import LoireUrbanisme.menu.MenuEvent;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import LoireUrbanisme.menu.Menu;
import java.sql.Connection;
import metiers.Ligne;
import passerelle.LigneDAO;


public class EcranFenetre extends JFrame implements MenuEvent {
    private Connection maConnexion;
    private LigneDAO dao;
    private Menu menu;
    private JPanel zoneContenu;

    public EcranFenetre() {

        maConnexion = passerelle.Connexion.getConnexion();

        if (maConnexion != null) {
            dao = new LigneDAO(maConnexion);
        } else {
            System.err.println("Impossible de se connecter à la base bd_gestbus");
        }

        setTitle("LoireUrbanisme - Système de Gestion des Transports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null); // Centre la fenêtre

        setLayout(new BorderLayout()); //plan fenetre (divions ) :
        //  Utilisation de la POO
        menu = new Menu();
        zoneContenu = new JPanel(new BorderLayout()); // on redivise
        zoneContenu.setBackground(new Color(0x413B3B));

        // ajoute les composants à la fenêtre
        add(menu, BorderLayout.WEST);
        add(zoneContenu, BorderLayout.CENTER);

        menu.addMenuEvent((index, subIndex, action) -> {
            menuSelected(index, subIndex, action);
        });

    }

   @Override
    public void menuSelected(int index, int subIndex, MenuAction action) {

        zoneContenu.removeAll();

        switch (index) {

            case 0: // Vue d'ensemble
                zoneContenu.add(new JLabel("Vue d'ensemble"));
                break;

            case 1: // Réseau & Trafic
                if (subIndex == 1) {
                    zoneContenu.add(new ReseauTrafic(dao, "Bus"), BorderLayout.CENTER);
                } else if (subIndex == 2) {
                    zoneContenu.add(new ReseauTrafic(dao, "Tram"), BorderLayout.CENTER);
                }
                break;

            case 6: // Map

                break;
        }

        zoneContenu.revalidate();
        zoneContenu.repaint();
    }

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Échec au chargement du thème");
        }

        // Lancement
        SwingUtilities.invokeLater(() -> {
            new EcranFenetre().setVisible(true);
        });


    }
}