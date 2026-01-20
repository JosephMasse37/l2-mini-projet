package LoireUrbanisme.borne;

import LoireUrbanisme.conduites.Conduites;
import LoireUrbanisme.conduites.ConduitesAction;
import passerelle.DAOException;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.LocalDateTime;

class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel();
    private final JButton editButton = new JButton("Modifier");
    private final JButton deleteButton = new JButton("Supprimer");
    private final JPanel parentPanel;
    private int currentRow;
    private JTable table;

    public ButtonEditor(JTable table, JPanel parentPanel) {
        this.table = table;
        this.parentPanel = parentPanel;

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(editButton, gbc);
        gbc.gridx = 1;
        panel.add(deleteButton, gbc);

        editButton.addActionListener(e -> {
            int idBorne = (int) table.getValueAt(currentRow, 0);

            try {
                BorneAction.editBorne(idBorne, parentPanel);
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });

        deleteButton.addActionListener(e -> {
            int idBorne = (int) table.getValueAt(currentRow, 0);

            int response = JOptionPane.showConfirmDialog(
                    parentPanel, // parent pour centrer la boîte
                    "Voulez-vous vraiment supprimer cette borne ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                try {
                    BorneAction.deleteBorne(idBorne, parentPanel);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.currentRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}