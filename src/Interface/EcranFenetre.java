package Interface;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import LoireUrbanisme.menu.Menu;

public class EcranFenetre extends JFrame {

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