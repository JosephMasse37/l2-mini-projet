package LoireUrbanisme.connexion;

import Interface.EcranConnexion;
import Interface.EcranFenetre;
import metiers.Utilisateur;
import passerelle.DAOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class PageConnexion extends JPanel {
    private Font customFont;
    private EcranConnexion ecranConnexion;

    public PageConnexion(EcranConnexion ecranConnexion) throws DAOException {
        this.ecranConnexion = ecranConnexion;
        init();
    }

    private void init() throws DAOException {
        chargerPolice();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        mainPanel.add(titre());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(logo());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formulaire());

        add(mainPanel, BorderLayout.CENTER);
    }

    private void chargerPolice() {
        try {
            File fontFile = new File("resources/LoireUrbanisme.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("Erreur police : " + e.getMessage());
        }
    }

    private JComponent titre() {
        JLabel titre = new JLabel("BIENVENUE SUR LOIREURBANISME");
        titre.setFont(new Font(customFont.getFontName(), Font.BOLD, 26));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titre;
    }

    private JComponent logo() {
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            ImageIcon icon = new ImageIcon(
                    Objects.requireNonNull(getClass().getResource("/icon/LogoConnexion.png"))
            );

            // Redimensionnement si besoin
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));

        } catch (Exception e) {
            System.out.println("Erreur chargement logo : " + e.getMessage());
        }

        return logo;
    }

    private JComponent formulaire() throws DAOException {
        JTextField usernameField;
        JPasswordField passwordField;
        JButton boutonConnexion;

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setFont(customFont);

        usernameField = new JTextField(15);
        usernameField.setFont(customFont);

        JLabel passLabel = new JLabel("Mot de passe");
        passLabel.setFont(customFont);

        passwordField = new JPasswordField(15);
        passwordField.setFont(customFont);

        boutonConnexion = new JButton("Se connecter");
        boutonConnexion.setFont(customFont);

        // Action
        boutonConnexion.addActionListener( e -> {
            try {
                Utilisateur user = PageConnexionAction.seConnecter(usernameField.getText(), passwordField.getPassword());
                if (user != null) {
                    EcranFenetre appli = new EcranFenetre(user);
                    appli.setVisible(true);
                    ecranConnexion.dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Le nom d'utilisateur ou le mot de passe est erroné.",
                            "Connexion échouée",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    boutonConnexion.doClick();
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boutonConnexion, gbc);

        return panel;
    }
}
