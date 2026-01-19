package LoireUrbanisme.VueEnsemble;

import passerelle.ClientDAO;
import passerelle.Connexion;
import passerelle.DAOException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Map;

public class VueEnsemble extends JPanel {

    public VueEnsemble() {
        setOpaque(false);
        setLayout(new GridLayout(2, 1, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            java.sql.Connection conn = Connexion.getConnexion();

            // (Donut)
            passerelle.ClientDAO clientDao = new passerelle.ClientDAO(conn);
            Map<String, Integer> statsClients = clientDao.getStatsParFormule();
            add(creerBlocStat("Répartition Abonnements", statsClients));

            // Bornes (Renta)
            passerelle.BorneDAO borneDao = new passerelle.BorneDAO(conn);
            Map<String, String> statsRenta = borneDao.getStatsExtremes();

            add(creerBlocRenta(statsRenta));

        } catch (Exception e) {
            add(new JLabel("Erreur SQL : " + e.getMessage()));
            e.printStackTrace();
        }
    }


    private JPanel creerBlocStat(String titre, Map<String, Integer> stats) {

        JPanel bloc = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x484848)); // rounded panel derriere
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };

        bloc.setOpaque(false);
        bloc.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // T bloc
        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setForeground(Color.WHITE);
        bloc.add(lblTitre, BorderLayout.NORTH);

        // (légende à gauche, cercle à droite)
        JPanel contenu = new JPanel(new GridLayout(1, 2));
        contenu.setOpaque(false);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(Box.createVerticalStrut(10));

        int total = stats.values().stream().mapToInt(Integer::intValue).sum(); //Integer::intValue = convert en int

        Color[] couleursAleatoires = new Color[stats.size()];
        for (int j = 0; j < stats.size(); j++) {
            couleursAleatoires[j] = new Color((int)(Math.random() * 0x1000000));
        }

        int[] i = {0}; // on force pr start a palette[0]

        stats.forEach((formule, nombre) -> {
            int pourcentage = (total > 0) ? (nombre * 100) / total : 0;

            Color couleurPourCetteLigne = couleursAleatoires[i[0]]; //on pioche ds tableau cree alea

            // add la ligne direct avec la couleur générée
            leftPanel.add(createLegendItem(formule + " (" + pourcentage + "%)", couleurPourCetteLigne));
            leftPanel.add(Box.createVerticalStrut(10));

            i[0]++; // next couleur
        });

        contenu.add(leftPanel);

        //  passe stats et couleurs au DonutChart pour qu'il soit multicolore
        contenu.add(new DonutChart(stats, couleursAleatoires));
        bloc.add(contenu, BorderLayout.CENTER);

        return bloc;
    }

    private JPanel creerBlocRenta(Map<String, String> data) {

        JPanel bloc = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor( Color.black);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        bloc.setOpaque(false);
        bloc.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitre = new JLabel("Rentabilité des Bornes (Extrêmes)");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setForeground(Color.WHITE);
        bloc.add(lblTitre, BorderLayout.NORTH);

        JPanel container = new JPanel(new GridLayout(1, 2, 30, 0));
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        //  LA MEILLEURE
        JPanel cardBest = new JPanel();
        cardBest.setLayout(new BoxLayout(cardBest, BoxLayout.Y_AXIS));
        cardBest.setBackground(Color.WHITE);
        cardBest.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tBest = new JLabel("Borne la plus rentable");
        tBest.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tBest.setForeground(new Color(38, 166, 91)); // Vert

        JLabel idBest = new JLabel("ID Borne : " + data.getOrDefault("best_id", "INDISPO"));
        JLabel valBest = new JLabel(data.getOrDefault("best_ventes", "0") + " ventes");
        valBest.setFont(new Font("Segoe UI", Font.BOLD, 20));

        cardBest.add(tBest);
        cardBest.add(Box.createVerticalStrut(5));
        cardBest.add(idBest);
        cardBest.add(Box.createVerticalStrut(10));
        cardBest.add(valBest);
        container.add(cardBest);

        // LA PIRE
        JPanel cardWorst = new JPanel();
        cardWorst.setLayout(new BoxLayout(cardWorst, BoxLayout.Y_AXIS));
        cardWorst.setBackground(Color.WHITE);
        cardWorst.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tWorst = new JLabel("Borne la moins rentable");
        tWorst.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tWorst.setForeground(new Color(192, 57, 43)); // Rouge

        JLabel idWorst = new JLabel("ID Borne : " + data.getOrDefault("pire_id", "N/A"));
        JLabel valWorst = new JLabel(data.getOrDefault("pire_ventes", "0") + " ventes");
        valWorst.setFont(new Font("Segoe UI", Font.BOLD, 20));

        cardWorst.add(tWorst);
        cardWorst.add(Box.createVerticalStrut(5));
        cardWorst.add(idWorst);
        cardWorst.add(Box.createVerticalStrut(10));
        cardWorst.add(valWorst);
        container.add(cardWorst);

        bloc.add(container, BorderLayout.CENTER);
        return bloc;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT));
        item.setOpaque(false);
        //on crée "."
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(color);
                g.fillOval(0, 0, 10, 10);
            }
        };
        dot.setPreferredSize(new Dimension(10, 10));
        JLabel label = new JLabel(text);
        label.setForeground(Color.LIGHT_GRAY);
        item.add(dot);
        item.add(label);
        return item;
    }

    class DonutChart extends JComponent {
        private Map<String, Integer> stats;
        private Color[] colors;

            //w 2 nv arguments!!
        public DonutChart(Map<String, Integer> stats, Color[] colors) {
            this.stats = stats;
            this.colors = colors;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 60;
           // pour centrer
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            //pr know quoi correspond cercle complet
            int total = stats.values().stream().mapToInt(Integer::intValue).sum();

            //epaisseur trait                        type de trait
            g2.setStroke(new BasicStroke(25, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));

            //  fond gris
            g2.setColor(new Color(70, 70, 70));
            g2.drawOval(x, y, size, size);

            // Dessiner les lignes de couleur
            double startAngle = 90; // start en haut
            int i = 0;
            for (int valeur : stats.values()) {
                double arcAngle = -(valeur * 360.0) / total; // "-" aiguille montre
                g2.setColor(colors[i % colors.length]);
                g2.draw(new Arc2D.Double(x, y, size, size, startAngle, arcAngle, Arc2D.OPEN));
                startAngle += arcAngle; //memo ou on s'est stop
                i++;
            }
            g2.dispose();
        }
    }
}