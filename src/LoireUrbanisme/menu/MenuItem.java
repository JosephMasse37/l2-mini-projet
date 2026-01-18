package LoireUrbanisme.menu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class MenuItem extends JPanel {

    private final Menu menu;
    private final String[] menuItems;
    private final int menuIndex;
    private final List<MenuEvent> events;
    private boolean sousMenuVisible = false;
    Font customFont;


    public MenuItem(Menu menu, String[] menuItems, int menuIndex, List<MenuEvent> events) {
        this.menu = menu;
        this.menuItems = menuItems;
        this.menuIndex = menuIndex;
        this.events = events;

        chargerPolice();
        init();
    }

    //on build
    private void init() {

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // onglets DES SOUS-MENUS
        JPanel panelSousMenu = new JPanel();
        panelSousMenu.setLayout(new BoxLayout(panelSousMenu, BoxLayout.Y_AXIS));
        panelSousMenu.setVisible(false);
        panelSousMenu.setOpaque(false);

        // onglet PRINCIPAUX
        JButton btnPrincipal = creerBouton(menuItems[0], false);
        add(btnPrincipal);

        add(Box.createRigidArea(new Dimension(0, 15))); // 15 pixels d'espace

        for (int i = 1; i < menuItems.length; i++) {
            int subIndex = i;
            JButton btnSousMenu = creerBouton(menuItems[i], true);

            btnSousMenu.addActionListener(e -> {
                for (MenuEvent event : events) {
                    event.menuSelected(menuIndex, subIndex, new MenuAction());
                }
            });
            panelSousMenu.add(btnSousMenu);
        }
        add(panelSousMenu);


        btnPrincipal.addActionListener(e -> {
            if (menuItems.length > 1) { // plus d'1 elem

                sousMenuVisible = !sousMenuVisible; //true
                panelSousMenu.setVisible(sousMenuVisible);
                // On prévient le parent de se redessiner
                revalidate();
            } else {
                for (MenuEvent event : events) {
                    event.menuSelected(menuIndex, 0, new MenuAction());
                }
            }
        });
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

    private static JButton boutonSelectionne = null;

    private JButton creerBouton(String texte, boolean estSousMenu) {
        JButton b = new JButton(texte) {
            private boolean isHover = false;

            {
                // gestion du survol (Hover)
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { isHover = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { isHover = false; repaint(); }
                }); //paintComponent forcé
            }

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create(); //copie pinc

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //pr pixels

                if (texte.equals("Vue d'ensemble")) {
                    g2.setColor(new Color(0xE8DEF8)); // Fond clair comme le logo
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                }

                //border radius au fond dcp
                else if (boutonSelectionne == this && !estSousMenu && !texte.equals("Vue d'ensemble")) {
                    g2.setColor(new Color(0x665582));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); //pr arc
                }
                // EFFET HOVER (Voile blanc léger)
                else if (isHover) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40,40 );
                }

                g2.dispose();
                super.paintComponent(g);


            }
        };

        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); //remove les trucs par defauts
        b.setBorderPainted(false);
        b.setFocusPainted(false); //pts
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.LEFT); //Force texte et icône à coller à gauche du bouton

        if (!estSousMenu) {
            // On réduit la marge à 10 pour que ça colle bien au bord noir
            b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            b.setIconTextGap(12);
        }

            if (texte.equals("Vue d'ensemble")) {
            b.setForeground(new Color(0x4A4459));
        } else {
            b.setForeground(new Color(0x79747E));
        }

        if (!estSousMenu) {
            b.setFont(customFont.deriveFont(Font.PLAIN, 14f));
            b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            try {
                String fileName = texte.replace(" ", "_").replace("'", "").replace("é", "e");
                ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/" + fileName + ".png")));
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                b.setIcon(new ImageIcon(img));
                b.setIconTextGap(15);
            } catch (Exception e) { /* Pas d'icône trouvée */ }
        } else {
            b.setFont(customFont.deriveFont(Font.PLAIN, 12f));
            b.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));        }

        b.addActionListener(e -> {
            boutonSelectionne = b; // ref b sauv.
            if (menu != null) menu.repaint(); //car changement d etat donc paintcompenent too
        });

        return b;
    }

}