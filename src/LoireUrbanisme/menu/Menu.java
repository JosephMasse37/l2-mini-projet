package LoireUrbanisme.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Menu extends JPanel { // = private JPanel

    Font customFont;
    private List<MenuEvent> events = new ArrayList<>();

    private final String menuItems[][] = {
            {"Vue d'ensemble"},
            {"Réseau & Trafic", "Bus", "Tram"},
            {"Clients"},
            {"Reseau & Bornes"},
            {"Vehicule"},
            {"Chauffeurs"},
            {"Map"}
    };

    //lui qui lance tt
    public Menu() {
        init();
    }

    private void chargerPolice() {
        try {
            File fontFile = new File("resources/LoireUrbanisme.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("Erreur police : " + e.getMessage());


        }}

    private void init() {

        chargerPolice();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(270, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // lex-direction: column = rangement
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15)); //marg int
        putClientProperty("FlatLaf.style",
                "arc: 30 "
        );
        setOpaque(false);

        // ---- conteneur pour le logo titre

        JPanel logoConteneur = new JPanel();

        logoConteneur.putClientProperty("FlatLaf.style",
                "background: #E6DDF7; " +
                        "arc: 30; " +
                        "border: 5,5,5,5"       // margin interne (t, l, b, r)
        );
        logoConteneur.setOpaque(false);
        logoConteneur.setMaximumSize(new Dimension(200, 60));  //taille bloc
        logoConteneur.setLayout(new GridBagLayout()); // centrer le texte dedans

        //  texte à l'intérieur
        JLabel LoireUrbanisme = new JLabel("LoireUrbanisme");
        LoireUrbanisme.setFont(customFont.deriveFont(Font.PLAIN, 16f));
        LoireUrbanisme.setForeground(new Color(0x4F378A));


        //on add a la fin
        add(Box.createVerticalStrut(50)); //add un gap
        logoConteneur.add(LoireUrbanisme);
        logoConteneur.setAlignmentX(Component.CENTER_ALIGNMENT); //center dans sidebar
        add(logoConteneur);
        add(Box.createVerticalStrut(140));

        for (int i = 0; i < menuItems.length; i++) {
            MenuItem item = new MenuItem(this, menuItems[i], i, events);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(item);
        }
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }
}