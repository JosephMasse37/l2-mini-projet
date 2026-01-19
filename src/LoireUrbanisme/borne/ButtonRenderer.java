package LoireUrbanisme.borne;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;

class ButtonRenderer extends JPanel implements TableCellRenderer {
    private Font customFont;

    private final JButton editButton = new JButton("Modifier") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Remplir le fond avec couleur
            g2.setColor(new Color(0, 10, 142, 100)); // Bleu
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 20 px d’arrondi

            // Dessiner le texte centré
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    };
    private final JButton deleteButton = new JButton("Supprimer") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Remplir le fond avec couleur
            g2.setColor(new Color(119, 0, 0)); // Rouge
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 20 px d’arrondi

            // Dessiner le texte centré
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    };

    public ButtonRenderer() {
        chargerPolice();

        setOpaque(false);

        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font(customFont.getFontName(), Font.PLAIN, 14));

        // GridBagLayout pour centrer horizontalement et verticalement
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5); // petit espace entre boutons

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(editButton, gbc);

        gbc.gridx = 1;
        add(deleteButton, gbc);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
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
}
