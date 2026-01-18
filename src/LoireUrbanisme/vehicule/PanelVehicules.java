package LoireUrbanisme.vehicule;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import metiers.Vehicule;
import metiers.Bus;
import metiers.Tram;
import metiers.TypeVehicule;
import metiers.ConduitSur;
import passerelle.VehiculeDAO;
import passerelle.ConduitSurDAO;
import passerelle.TypeVehiculeDAO;
import passerelle.Connexion;
import passerelle.DAOException;

public class PanelVehicules extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    
    private List<Vehicule> listeCompleteVehicules; 
    private VehiculeDAO vehiculeDAO;
    private ConduitSurDAO conduitSurDAO;
    private Map<Integer, String> mapVehiculeLigne = new HashMap<>();

    public PanelVehicules(VehiculeDAO vehiculeDAO, ConduitSurDAO conduitSurDAO) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.decode("#1E1E1E"));

        this.vehiculeDAO = vehiculeDAO;
        this.conduitSurDAO = conduitSurDAO;

        chargerDonnees();

        JPanel panelTop = createFilterPanel();
        JPanel panelCenter = createTablePanel();
        JPanel panelBottom = createBottomPanel(); // Bouton Ajout

        this.add(panelTop, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
    }

    private void chargerDonnees() {
        try {
            listeCompleteVehicules = vehiculeDAO.findAll();
            List<ConduitSur> conduites = conduitSurDAO.findAll();
            mapVehiculeLigne.clear();
            for (ConduitSur cs : conduites) {
                if (cs.getUnVehicule() != null && cs.getUneLigne() != null) {
                    mapVehiculeLigne.put(cs.getUnVehicule().getNumVehicule(), cs.getUneLigne().getLibelle());
                }
            }
        } catch (DAOException e) {
            listeCompleteVehicules = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Erreur chargement : " + e.getMessage());
        }
    }

    private void rafraichirTableau(List<Vehicule> donnees) {
        tableModel.setRowCount(0);
        for (Vehicule v : donnees) {
            Color statutColor = calculerStatutMaintenance(v);
            String ligne = mapVehiculeLigne.getOrDefault(v.getNumVehicule(), "Non assignée");
            String modele = v.getMarque() + " " + v.getModele();
            String dateMaint = (v.getDateHeureDerniereMaintenance() != null) 
                    ? v.getDateHeureDerniereMaintenance().toLocalDate().toString() 
                    : "Jamais";

            tableModel.addRow(new Object[]{statutColor, modele, ligne, dateMaint, "✎"});
        }
    }

    // --- ACTIONS AJOUT / MODIF ---

    private void ajouterVehiculeAction() {
        JTextField txtNum = new JTextField();
        JTextField txtMarque = new JTextField();
        JTextField txtModele = new JTextField();
        JTextField txtDateFab = new JTextField("2023-01-01");
        JTextField txtDateService = new JTextField("2023-02-01");
        String[] types = {"Bus", "Tram"};
        JComboBox<String> cbType = new JComboBox<>(types);

        Object[] message = {
            "Numéro Véhicule (ID) :", txtNum,
            "Type :", cbType,
            "Marque :", txtMarque,
            "Modèle :", txtModele,
            "Date Fabrication (AAAA-MM-JJ) :", txtDateFab,
            "Date Mise en Service (AAAA-MM-JJ) :", txtDateService
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ajouter Véhicule", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(txtNum.getText());
                String marque = txtMarque.getText();
                String modele = txtModele.getText();
                LocalDate dateFab = LocalDate.parse(txtDateFab.getText());
                LocalDate dateService = LocalDate.parse(txtDateService.getText());
                LocalDateTime dateMaint = LocalDateTime.now(); // Initialisé à maintenant

                // Récupération du TypeVehicule
                TypeVehiculeDAO typeDAO = new TypeVehiculeDAO(Connexion.getConnexion());
                List<TypeVehicule> typesDispo = typeDAO.findAll();
                String selection = (String) cbType.getSelectedItem();
                TypeVehicule typeV = typesDispo.stream()
                        .filter(t -> t.getLibelle().equalsIgnoreCase(selection))
                        .findFirst()
                        .orElse(null);
                
                if(typeV == null) {
                    JOptionPane.showMessageDialog(this, "Erreur : Type de véhicule introuvable en base.");
                    return;
                }

                Vehicule v;
                if(selection.equals("Bus")) v = new Bus(id, marque, modele, dateFab, dateService, dateMaint);
                else v = new Tram(id, marque, modele, dateFab, dateService, dateMaint);
                
                v.setTypevehicule(typeV);

                vehiculeDAO.create(v);
                
                chargerDonnees(); 
                filtrerTableau("Tous");
                JOptionPane.showMessageDialog(this, "Véhicule ajouté !");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur saisie : " + e.getMessage());
            }
        }
    }

    private void modifierVehiculeAction(int row) {
        // Recherche du véhicule via le modèle affiché dans le tableau
        String modeleAffiche = (String) table.getValueAt(row, 1);
        Vehicule target = null;
        for(Vehicule v : listeCompleteVehicules) {
            if((v.getMarque() + " " + v.getModele()).equals(modeleAffiche)) {
                target = v; break;
            }
        }
        
        if(target == null) return;

        JTextField txtMarque = new JTextField(target.getMarque());
        JTextField txtModele = new JTextField(target.getModele());
        // Formatage date pour affichage
        String dateMaintStr = (target.getDateHeureDerniereMaintenance() != null) 
                ? target.getDateHeureDerniereMaintenance().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) 
                : "";
        JTextField txtDateMaint = new JTextField(dateMaintStr);

        Object[] message = {
            "Modifier Véhicule #" + target.getNumVehicule(),
            "Marque :", txtMarque,
            "Modèle :", txtModele,
            "Dernière Maintenance (AAAA-MM-JJ HH:mm) :", txtDateMaint
        };

        // Boutons personnalisés
        Object[] options = {"Enregistrer", "Supprimer", "Annuler"};

        int choix = JOptionPane.showOptionDialog(this, 
                message, 
                "Modifier Véhicule", 
                JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                options, 
                options[0]);

        // --- ENREGISTRER ---
        if (choix == 0) {
            try {
                target.setMarque(txtMarque.getText());
                target.setModele(txtModele.getText());
                if(!txtDateMaint.getText().isBlank()) {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    target.setDateHeureDerniereMaintenance(LocalDateTime.parse(txtDateMaint.getText(), formatter));
                }
                
                vehiculeDAO.update(target);
                chargerDonnees();
                filtrerTableau("Tous");
                JOptionPane.showMessageDialog(this, "Modifié !");
                
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur format : " + e.getMessage());
            }
        }
        
        // --- SUPPRIMER ---
        else if (choix == 1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Supprimer définitivement le véhicule " + target.getNumVehicule() + " (" + target.getModele() + ") ?", 
                    "Confirmation", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    vehiculeDAO.delete(target);
                    chargerDonnees();
                    filtrerTableau("Tous");
                    JOptionPane.showMessageDialog(this, "Véhicule supprimé.");
                } catch (DAOException e) {
                    JOptionPane.showMessageDialog(this, "Erreur suppression : " + e.getMessage());
                }
            }
        }
    }

    // --- DESIGN & UTILS (Identique précédent) ---

    private Color calculerStatutMaintenance(Vehicule v) {
        if (v.getDateHeureDerniereMaintenance() == null) return Color.RED;
        long semaines = ChronoUnit.WEEKS.between(v.getDateHeureDerniereMaintenance().toLocalDate(), LocalDate.now());
        if (semaines < 2) return Color.GREEN;
        if (semaines < 4) return Color.ORANGE;
        return Color.RED;
    }

    private void filtrerTableau(String type) {
        if(type.equals("Tous")) rafraichirTableau(listeCompleteVehicules);
        else {
            rafraichirTableau(listeCompleteVehicules.stream()
                .filter(v -> v.getTypevehicule() != null && v.getTypevehicule().getLibelle().equalsIgnoreCase(type))
                .collect(Collectors.toList()));
        }
    }

    private JPanel createFilterPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20)); p.setOpaque(false);
        p.add(createFilterButton("Tous", true));
        p.add(createFilterButton("Bus", false));
        p.add(createFilterButton("Tram", false));
        return p;
    }
    
    private JToggleButton createFilterButton(String t, boolean s) {
        JToggleButton b = new JToggleButton(t); b.setSelected(s); b.setBackground(Color.WHITE); b.setForeground(Color.BLACK);
        b.setBorder(BorderFactory.createEmptyBorder(8,20,8,20)); b.setFocusPainted(false);
        b.addActionListener(e->{
            for(Component c : b.getParent().getComponents()) if(c instanceof JToggleButton && c!=b) ((JToggleButton)c).setSelected(false);
            b.setSelected(true); filtrerTableau(t);
        });
        return b;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false); p.setBorder(new EmptyBorder(0,20,0,20));
        String[] cols = {"Statut", "Modèle", "Ligne", "Maintenance", ""};
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) {return false;} };
        table = new JTable(tableModel); table.setRowHeight(50);
        table.setBackground(Color.decode("#2D2D2D")); table.setForeground(Color.WHITE);
        table.setGridColor(Color.decode("#1E1E1E")); table.setShowVerticalLines(false);
        
        table.getColumnModel().getColumn(0).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(4).setMaxWidth(50);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer(); center.setHorizontalAlignment(JLabel.CENTER);
        center.setBackground(Color.decode("#2D2D2D")); center.setForeground(Color.WHITE);
        for(int i=1; i<4; i++) table.getColumnModel().getColumn(i).setCellRenderer(center);
        
        JTableHeader h = table.getTableHeader(); h.setBackground(Color.decode("#1E1E1E")); h.setForeground(Color.WHITE); h.setBorder(null); h.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        RoundedPanel c = new RoundedPanel(Color.decode("#2D2D2D"), 20); c.setLayout(new BorderLayout());
        c.add(h, BorderLayout.NORTH); 
        JScrollPane sp = new JScrollPane(table); sp.getViewport().setBackground(Color.decode("#1E1E1E")); sp.setBorder(null);
        c.add(sp, BorderLayout.CENTER);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(table.columnAtPoint(e.getPoint()) == 4 && table.getSelectedRow() != -1) {
                    modifierVehiculeAction(table.getSelectedRow());
                }
            }
        });

        p.add(c, BorderLayout.CENTER);
        if(listeCompleteVehicules!=null) filtrerTableau("Tous");
        return p;
    }

    private JPanel createBottomPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT)); p.setOpaque(false);
        JButton b = new JButton("Ajouter Véhicule"); b.setBackground(Color.WHITE); b.setForeground(Color.BLACK);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.addActionListener(e-> ajouterVehiculeAction());
        p.add(b); return p;
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JLabel l = (JLabel)super.getTableCellRendererComponent(t,v,s,f,r,c); l.setText(""); l.setIcon(new DotIcon((Color)v)); return l;
        }
    }
    class DotIcon implements Icon {
        Color c; public DotIcon(Color color) {c=color;}
        public void paintIcon(Component o, Graphics g, int x, int y) {g.setColor(c); g.fillOval(x,y,10,10);}
        public int getIconWidth() {return 10;} public int getIconHeight() {return 10;}
    }
    class ActionCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JLabel l = (JLabel)super.getTableCellRendererComponent(t,v,s,f,r,c); l.setText("✎"); l.setHorizontalAlignment(CENTER); l.setForeground(Color.LIGHT_GRAY); l.setFont(new Font("SansSerif", Font.PLAIN, 18)); return l;
        }
    }
    static class RoundedPanel extends JPanel {
        Color bg; int rad; public RoundedPanel(Color c, int r) {bg=c; rad=r; setOpaque(false);}
        protected void paintComponent(Graphics g) {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(bg); g.fillRoundRect(0,0,getWidth(),getHeight(),rad,rad);
        }
    }
}