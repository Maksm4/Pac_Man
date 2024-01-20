
import javax.swing.table.AbstractTableModel;


public class GameTableModel extends AbstractTableModel {
    private Object[][] data;
    private int rows;
    private int columns;


    public GameTableModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new Object[rows][columns];

    }


    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];

    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);


    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }


}

