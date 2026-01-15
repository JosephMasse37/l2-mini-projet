package Interface;

import LoireUrbanisme.borne.BornePanel;
import LoireUrbanisme.map.Map;
import LoireUrbanisme.menu.MenuAction;
import LoireUrbanisme.menu.MenuEvent;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import LoireUrbanisme.menu.Menu;
import metiers.Borne;

public class EcranFenetre extends JFrame implements MenuEvent {

    private Menu menu;
    private JPanel zoneContenu;

    public EcranFenetre() {

        setTitle("LoireUrbanisme - Système de Gestion des Transports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null); // Centre la fenêtre

        setLayout(new BorderLayout()); //plan fenetre (divions ) :
        //  Utilisation de la POO
        menu = new Menu();
        menu.addMenuEvent(this);
        zoneContenu = creerZoneContenu();

        // ajoute les composants à la fenêtre
        add(menu, BorderLayout.WEST);
        add(zoneContenu, BorderLayout.CENTER);

    }

    // a ne pas le mettre ici obvs
    private JPanel creerZoneContenu() {
        JPanel container = new JPanel(new BorderLayout(15, 15));
        return container;
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
                    zoneContenu.add(new JLabel("Bus"));
                } else if (subIndex == 2) {
                    zoneContenu.add(new JLabel("Tram"));
                }
                break;

            case 3: // Réseau & Borne
                zoneContenu.add(new BornePanel());
                break;

            case 6: // Map
                zoneContenu.add(new Map());
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

        EcranFenetre fenetreMain = new EcranFenetre();

        // Lancement
        SwingUtilities.invokeLater(() -> {
            fenetreMain.setVisible(true);
        });
    }
}