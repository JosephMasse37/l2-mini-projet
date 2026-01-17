package LoireUrbanisme.conduites;

import LoireUrbanisme.chauffeurs.Chauffeurs;
import LoireUrbanisme.chauffeurs.ChauffeursAction;
import LoireUrbanisme.menu.Menu;
import passerelle.DAOException;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel();
    private final JButton editButton = new JButton("Modifier");
    private final JButton deleteButton = new JButton("Supprimer");
    private final Conduites parentPanel;
    private int currentRow;
    private JTable table;

    public ButtonEditor(JTable table, Conduites parentPanel) {
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
            int id = (int) table.getValueAt(currentRow, 0); // ID caché
            try {
                ConduitesAction.editChauffeur(id, parentPanel);
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
            parentPanel.refreshTable();
        });

        deleteButton.addActionListener(e -> {
            int id = (int) table.getValueAt(currentRow, 0); // ID caché

            int response = JOptionPane.showConfirmDialog(
                    parentPanel, // parent pour centrer la boîte
                    "Voulez-vous vraiment supprimer ce chauffeur ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                try {
                    ChauffeursAction.deleteChauffeur(id, parentPanel);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
                parentPanel.refreshTable();
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