package LoireUrbanisme.menu;

import Interface.EcranFenetre;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import metiers.Utilisateur;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Menu extends JPanel { // = private JPanel

    Font customFont;
    private List<MenuEvent> events = new ArrayList<>();

    private final Utilisateur utilisateurConnecte;
    private JLabel userNameLabel = new JLabel("");
    private final EcranFenetre appli;

    private final String menuItems[][] = {
            {"Vue d'ensemble"},
            {"Réseau & Trafic", "Bus", "Tram"},
            {"Clients"},
            {"Réseau & Bornes"},
            {"Véhicules"},
            {"Chauffeurs"},
            {"Carte"}
    };

    //lui qui lance tt
    public Menu(Utilisateur utilisateurConnecte, EcranFenetre appli) {
        this.utilisateurConnecte = utilisateurConnecte;
        this.appli = appli;

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

        // Utilisateur connecté + bouton déconnexion
        add(Box.createVerticalGlue());

        add(utilisateurPanel());

        add(Box.createVerticalStrut(10));

    }

    private JComponent utilisateurPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.putClientProperty("FlatLaf.style",
                "background: #000000; " +
                "arc: 20; " +
                "border: 10,10,10,10"
        );

        userNameLabel = new JLabel(utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom());
        userNameLabel.setFont(customFont.deriveFont(Font.PLAIN, 14f));
        userNameLabel.setForeground(new Color(165, 55, 255));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.setFont(customFont.deriveFont(Font.PLAIN, 13f));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButton.putClientProperty(
                FlatClientProperties.STYLE,
                "arc: 15; background: #4F378A"
        );

        logoutButton.addActionListener(e -> {
            appli.dispose();
        });

        panel.add(userNameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(logoutButton);

        return panel;
    }

    public void setUserText(Utilisateur user) {
        userNameLabel.setText(user.getPrenom() + " " + user.getNom());
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }
}