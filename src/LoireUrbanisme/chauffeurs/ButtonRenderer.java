package LoireUrbanisme.chauffeurs;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class ButtonRenderer extends JPanel implements TableCellRenderer {
    private final JButton editButton = new JButton("Modifier");
    private final JButton deleteButton = new JButton("Supprimer");

    public ButtonRenderer() {
        setOpaque(false);

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
}
