package LoireUrbanisme.ReseauTrafic;

import metiers.Chauffeur;
import metiers.Ligne;
import passerelle.ConduitSurDAO;
import passerelle.DAOException;
import passerelle.LigneDAO;
import passerelle.ArretDAO;
import metiers.Arret;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.awt.*;
import LoireUrbanisme.ReseauTrafic.zoneContenuLigne;

public class ReseauTrafic extends JPanel {
    private LigneDAO dao;
    private String typeLigne;
    private JPanel partieDroite; // pr le mettre à jour au clic
    private JPanel conteneurLigne;

    public ReseauTrafic(LigneDAO dao, String typeLigne) {
        this.dao = dao;
        this.typeLigne = typeLigne;

        // On configure le look du JPanel Réseau de base
        this.setLayout(new BorderLayout(0, 0)); //sep : g/d
        this.setOpaque(false);
        this.conteneurLigne = new JPanel();
        this.partieDroite = new JPanel();
        initialiserInterface();

    }

    private void initialiserInterface() {

        this.removeAll(); // vide panel avant d'afficher

        // COLONNE GAUCHE : RECHERCHE + LISTE

        JPanel partieGauche = new JPanel(new BorderLayout(0, 15)); // div + gap vertic entre comp
        partieGauche.setBackground(Color.BLACK);
        partieGauche.setOpaque(true);
        partieGauche.setPreferredSize(new Dimension(380, 0));
        //marg int:
        partieGauche.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x5F5178), 1),
                BorderFactory.createEmptyBorder(30, 0, 20, 0)
        ));

        // Barre de recherche
        JTextField search = new JTextField();
        search.setPreferredSize(new Dimension(0, 45));
        search.setForeground(Color.WHITE);
        search.setCaretColor(Color.WHITE); // le "|"
        search.putClientProperty("FlatLaf.placeholderText", "Nom de Ligne..");
        search.putClientProperty("FlatLaf.style", "arc: 999; background: #434343; focusWidth: 1; outlineColor: #808080");
        //icône à la fin :
        search.putClientProperty("JTextField.trailingIcon", new ImageIcon(new ImageIcon("resources/icon/Search.png").getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        // Décolle icône du bord
        search.putClientProperty("JTextField.trailingIconMargin", new Insets(0, 0, 0, 15));
        //pr txt typed:
        search.setMargin(new Insets(0, 20, 0, 40));

        // SEARCH BAR QD ON TYPE
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrer(); // add text
            }
            public void removeUpdate(DocumentEvent e) {
                filtrer(); //supp text
            }
            public void changedUpdate(DocumentEvent e) {
                filtrer(); //gras text ou +
            }

            private void filtrer() {
                String texte = search.getText().trim(); //merci a JTextField! ->String
                chargerListe(texte); //ref !filtre.isEmpty dcp
            }
        });

        partieGauche.add(search, BorderLayout.NORTH);

        // Conteneur pour empiler les elem de liste lignes verticalement
        conteneurLigne.setLayout(new BoxLayout(conteneurLigne, BoxLayout.Y_AXIS));
        conteneurLigne.setOpaque(false);

        // add du scroll pour la liste de gauche si +lignes
        JScrollPane scroll = new JScrollPane(conteneurLigne);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        partieGauche.add(scroll, BorderLayout.CENTER);

        // COLONNE DROITE : details each ligne

        partieDroite.setLayout(new BorderLayout());
        partieDroite.setOpaque(false);

        this.add(partieGauche, BorderLayout.WEST);
        this.add(partieDroite, BorderLayout.CENTER); // Tout le reste à droite
        chargerListe(""); // Affiche tt au démarrage!
    }

    private void chargerListe(String filtre) {

        conteneurLigne.removeAll(); // On vide les anciens filtresss

        // DATA
        try {
            List<Ligne> listeLignes;
            if (filtre.isEmpty()) { //si r
                listeLignes = dao.getLignesParType(this.typeLigne);
            } else {
                listeLignes = dao.findByNomEtType(filtre, this.typeLigne);
            }

            passerelle.ChauffeurDAO chauffeurDAO = new passerelle.ChauffeurDAO(passerelle.Connexion.getConnexion());                String nomAffiche;

            ConduitSurDAO conduiteDAO = new ConduitSurDAO(passerelle.Connexion.getConnexion());
            for (Ligne L : listeLignes) {

                String statutDynamique = conduiteDAO.getStatutLigne(L.getIdLigne());
                String idV = conduiteDAO.getVehiculeId(L.getIdLigne());
                int idChauffeur = conduiteDAO.getIdChauffeurPourLigne(L.getIdLigne());

                String nomChauffeur = "Aucun conducteur";
                if (idChauffeur != -1) {
                    Chauffeur c = chauffeurDAO.find(idChauffeur);
                    if (c != null && c.getUtilisateur() != null) {
                        nomChauffeur = c.getUtilisateur().getNom() + " " + c.getUtilisateur().getPrenom();
                    }
                }

                // car modifié milles fois..
                final String fStatut = statutDynamique;
                final String fVehicule = idV;
                final String fNom = nomChauffeur;

                zoneContenuLigne carte = new zoneContenuLigne(L.getLibelle(), L.getTrajet(), statutDynamique) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        Boolean sel = (Boolean) getClientProperty("selectionnee");
                        if (sel != null && sel) {
                            GradientPaint grad = new GradientPaint(0, 0, new Color(0x5F5178), 0, getHeight(), new Color(0xB196DE));
                            g2.setPaint(grad);
                        } else {
                            g2.setColor(Color.BLACK);
                        }

                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                        g2.setColor(new Color(0x5F5178));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 0, 0);
                        //pr pas depasser^^
                        g2.dispose(); //pr supp copie
                        super.paintComponent(g);// pr fonct standards de Swing TEXT AVANT DESSIN
                    }
                };

                carte.setOpaque(false);

                carte.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {

                        for (Component c : conteneurLigne.getComponents()) {
                            if (c instanceof JComponent) ((JComponent) c).putClientProperty("selectionnee", false);
                            //jcomponent = can puclientproperty ONLY
                        }
                        carte.putClientProperty("selectionnee", true);
                        conteneurLigne.repaint();
                        afficherDetailsLigne(L, fStatut, fVehicule, fNom);
                    }});

                conteneurLigne.add(carte);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        conteneurLigne.revalidate(); //Layout Manager recalcule les dim et pos des comp enfants = jscrollpane ajusté.
        conteneurLigne.repaint();
    }

    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) { //pr component enfant
            this.radius = radius;
            setOpaque(false);
            setBackground(new Color(0x373030));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    // pr màj de la partie droite au clique
        private void afficherDetailsLigne(metiers.Ligne ligne, String statut, String idV, String nomChauffeur) {

            partieDroite.removeAll();
            partieDroite.setLayout(new BoxLayout(partieDroite, BoxLayout.Y_AXIS));
            partieDroite.setBackground(new Color(0x1A1A1A));
            partieDroite.setOpaque(true);


            // SECTION DU HAUT
            JPanel sectionHaut = new JPanel();
            sectionHaut.setLayout(new BoxLayout(sectionHaut, BoxLayout.Y_AXIS));
            sectionHaut.setOpaque(false);
            sectionHaut.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));


            //ligne +statut
            JPanel ligneTitre = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            ligneTitre.setOpaque(false);
            ligneTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblNomLigne = new JLabel("LIGNE " + ligne.getLibelle());
            lblNomLigne.setFont(new Font("Segoe UI", Font.BOLD, 42));
            lblNomLigne.setForeground(Color.WHITE);

            String texteStatut = (statut != null) ? statut.toUpperCase() : "HORS SERVICE";
            JLabel lblStatut = new JLabel("<html>● " + texteStatut + "</html>");
            lblStatut.setFont(new Font("Segoe UI", Font.BOLD, 18));

            if ("EN SERVICE".equals(texteStatut)) {
                lblStatut.setForeground(new Color(0x32CD32));
            } else {
                lblStatut.setForeground(new Color(0xFF4444));
            }

            ligneTitre.add(lblNomLigne);
            ligneTitre.add(lblStatut);
            sectionHaut.add(ligneTitre);
            sectionHaut.add(Box.createVerticalStrut(25));

            // add le trajet
            JLabel lblTrajet = new JLabel(ligne.getTrajet()); //
            lblTrajet.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTrajet.setForeground(Color.WHITE);
            lblTrajet.setAlignmentX(Component.LEFT_ALIGNMENT);

            sectionHaut.add(lblTrajet);

            // ROUNDED PANEL Bus
            RoundedPanel carteBus = new RoundedPanel(40);
            carteBus.setBackground(new Color(0x373030));
            carteBus.setMaximumSize(new Dimension(499, 253));
            carteBus.setLayout(new BorderLayout());
            carteBus.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Image du bus
            JLabel imgBus = new JLabel(new ImageIcon(new ImageIcon("resources/images/bus.png").getImage().getScaledInstance(206, 149, Image.SCALE_SMOOTH)));
            imgBus.setHorizontalAlignment(JLabel.CENTER);
            carteBus.add(imgBus, BorderLayout.NORTH);

            // Infos le bus
            JPanel blocInfo = creerBlocInfoBus(idV, nomChauffeur);
            carteBus.add(blocInfo, BorderLayout.CENTER);
            sectionHaut.add(carteBus);
            // forceeeeeeeee
            ligneTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTrajet.setAlignmentX(Component.CENTER_ALIGNMENT);
            carteBus.setAlignmentX(Component.CENTER_ALIGNMENT);

            // separation
            sectionHaut.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x333333)),
                    BorderFactory.createEmptyBorder(0, 0, 20, 0)
            ));


            // SECTION DU BAS : stats

            JPanel sectionBas = new JPanel(new BorderLayout(0, 20));
            sectionBas.setOpaque(false);
            sectionBas.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

            JLabel lblStatsTitre = new JLabel("STATISTIQUE DE LIGNE");
            lblStatsTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblStatsTitre.setForeground(Color.WHITE);
            sectionBas.add(lblStatsTitre, BorderLayout.NORTH);

            // cont stats sans text
            JPanel conteneurData = new JPanel(new BorderLayout(25, 0));
            conteneurData.setOpaque(false);

            //arrets + tickets validés
            JPanel blocGauche = new JPanel();
            blocGauche.setLayout(new BoxLayout(blocGauche, BoxLayout.Y_AXIS));
            blocGauche.setOpaque(false);
            blocGauche.setPreferredSize(new Dimension(287, 0));

            // tickets valides
            ConduitSurDAO conduiteDAO = new ConduitSurDAO(passerelle.Connexion.getConnexion());
            int totalTickets = conduiteDAO.getNbValidationsJour(ligne.getIdLigne());

            // panel
            RoundedPanel carteTickets = new RoundedPanel(30);
            carteTickets.setBackground(new Color(0x373030));
            carteTickets.setLayout(new BorderLayout());
            carteTickets.setPreferredSize(new Dimension(287, 152));
            carteTickets.setMaximumSize(new Dimension(287, 152));

            JPanel blocTitreTickets = new JPanel();
            blocTitreTickets.setLayout(new BoxLayout(blocTitreTickets, BoxLayout.Y_AXIS));
            blocTitreTickets.setOpaque(false);
            blocTitreTickets.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));

            JLabel lblTitrePrincipal = new JLabel("Tickets Validés");
            lblTitrePrincipal.setForeground(Color.WHITE);
            lblTitrePrincipal.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JLabel lblSousTitre = new JLabel("(Aujourd'hui)");
            lblSousTitre.setForeground(new Color(0x888888));
            lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            blocTitreTickets.add(lblTitrePrincipal);
            blocTitreTickets.add(lblSousTitre);
            carteTickets.add(blocTitreTickets, BorderLayout.NORTH);

            JLabel nbT = new JLabel(String.valueOf(totalTickets)); // use la var SQL
            nbT.setFont(new Font("Segoe UI", Font.BOLD, 50));
            nbT.setForeground(Color.WHITE);
            nbT.setHorizontalAlignment(JLabel.CENTER);
            carteTickets.add(nbT, BorderLayout.CENTER);

            // Label du pourcentage
            String evolutionTexte = conduiteDAO.getEvolutionValidations(ligne.getIdLigne());

            JLabel evolT = new JLabel(evolutionTexte, SwingConstants.RIGHT);
            evolT.setFont(new Font("Segoe UI", Font.BOLD, 14));
            evolT.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));

            if (evolutionTexte.startsWith("-")) {
                evolT.setForeground(new Color(0xFF4444)); // Rouge
            } else {
                evolT.setForeground(new Color(0x32CD32)); // Vert
            }
            carteTickets.add(evolT, BorderLayout.SOUTH);

            // liste Arrêts
            RoundedPanel carteArrets = new RoundedPanel(30);
            carteArrets.setBackground(new Color(0x373030));
            carteArrets.setLayout(new BorderLayout());
            carteArrets.setPreferredSize(new Dimension(287, 300));

            JPanel containerListe = new JPanel();
            containerListe.setLayout(new BoxLayout(containerListe, BoxLayout.Y_AXIS));
            containerListe.setOpaque(false);
            containerListe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            try {
                ArretDAO arretDAO = new ArretDAO(passerelle.Connexion.getConnexion());
                List<Arret> listeDesArrets = arretDAO.getArretsParLigne(ligne.getIdLigne());
                for (int i = 0; i < listeDesArrets.size(); i++) {
                    Arret a = listeDesArrets.get(i);
                    JPanel ligneArret = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                    ligneArret.setOpaque(false);
                    JLabel dot = new JLabel("●");
                    dot.setForeground((i == 0) ? new Color(0x32CD32) : new Color(0x9370DB));
                    JLabel nomArret = new JLabel(a.getNom());
                    nomArret.setForeground(Color.WHITE);
                    ligneArret.add(dot); ligneArret.add(nomArret);
                    containerListe.add(ligneArret);
                }
            } catch (DAOException e) {
                containerListe.add(new JLabel("Erreur SQL"));
            }

            JScrollPane scrollA = new JScrollPane(containerListe);
            scrollA.setOpaque(false);
            scrollA.getViewport().setOpaque(false); //fond moche
            scrollA.setBorder(null); // carre moche
            carteArrets.add(scrollA, BorderLayout.CENTER); //predn espace restant

            //  ASSEMBLAGE DU BLOC GAUCHE
            blocGauche.add(carteTickets);
            blocGauche.add(Box.createVerticalStrut(20));
            blocGauche.add(carteArrets);

            //  BLOC DROIT
            RoundedPanel ValidationsArretsGraphe = new RoundedPanel(30);
            ValidationsArretsGraphe.setBackground(new Color(0x2A2A2A));
            ValidationsArretsGraphe.setLayout(new BorderLayout());

            conteneurData.add(blocGauche, BorderLayout.WEST);
            conteneurData.add(ValidationsArretsGraphe, BorderLayout.CENTER);

            // 2. On met ce conteneur dans la section du bas
            sectionBas.add(conteneurData, BorderLayout.CENTER);

            // 3. ON AJOUTE LES DEUX SECTIONS À LA PARTIE DROITE (C'est ce qui te manque !)
            partieDroite.add(sectionHaut); // Le bus et le titre
            partieDroite.add(sectionBas);  // Les tickets, arrêts et graphique

            // 4. On force Java à redessiner le tout
            partieDroite.revalidate();
            partieDroite.repaint();
        }

    private JPanel creerBlocInfoBus(String idV, String nomChauffeur) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Ligne 1 : Véhicule
        JLabel lblVehi = new JLabel("Véhicule id : " + idV);
        lblVehi.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblVehi.setForeground(Color.WHITE);
        lblVehi.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ligne 2 : Chauffeur
        JLabel lblChauff = new JLabel("conduit par : " + nomChauffeur);
        lblChauff.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblChauff.setForeground(Color.WHITE);
        lblChauff.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblVehi);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblChauff);

        return panel;
    }

}