import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColorCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        label.setBackground(Color.BLACK);

        if (value instanceof Color) {
            label.setBackground((Color) value);
            label.setForeground((Color) value);
            label.setIcon(null);
        } else if (value instanceof Coin) {
            if (((Coin) value).isCollected()) {
                label.setVisible(false);
            }
            label.setVisible(true);
            label.setText(null);
            label.setIcon(((Coin) value).getIcon());

        } else if (value instanceof ImageIcon) {
            label.setBackground(Color.BLACK);
            label.setText(null);
            label.setIcon((ImageIcon) value);

        } else if (value instanceof Ghost) {
            label.setText(null);
            label.setIcon(((Ghost) value).getIcon());
        }
        if (value == null) {
            label.setForeground(Color.BLACK);
            label.setBackground(Color.BLACK);
            label.setText(null);
            label.setIcon(null);
        }


        return label;
    }
}

