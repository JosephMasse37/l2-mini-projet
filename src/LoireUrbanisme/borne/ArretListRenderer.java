package LoireUrbanisme.borne;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ArretListRenderer extends JLabel implements ListCellRenderer<String> {

    public ArretListRenderer() {
        setOpaque(true);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends String> list,
            String value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        setText(value);

        if (isSelected) {
            setBackground(new Color(70, 70, 70));
            setForeground(Color.WHITE);
        } else {
            setBackground(new Color(45, 45, 45));
            setForeground(Color.LIGHT_GRAY);
        }

        return this;
    }
}
