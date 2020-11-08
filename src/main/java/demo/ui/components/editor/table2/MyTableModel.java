package demo.ui.components.editor.table2;

import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class MyTableModel extends AbstractTableModel {
    private final Object[][] cells = {
            { "Mercury", 2440.0, 0, false, Color.YELLOW }, { "Venus", 6052.0, 0, false, Color.YELLOW },
            { "Earth", 6378.0, 1, false, Color.BLUE }, { "Mars", 3397.0, 2, false, Color.RED },
            { "Jupiter", 71492.0, 16, true, Color.ORANGE }, { "Saturn", 60268.0, 18, true, Color.ORANGE },
            { "Uranus", 25559.0, 17, true, Color.BLUE }, { "Neptune", 24766.0, 8, true, Color.BLUE },
            { "Pluto", 1137.0, 1, false, Color.BLACK }
    };
    
    private final String[] columnNames = { "Planet", "Radius", "Moons", "Gaseous", "Color" };
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int getRowCount() {
        return cells.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return cells[rowIndex][columnIndex];
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    //如果不覆盖这个方法每列的类型都为String
    public Class<?> getColumnClass(int columnIndex) {
        return cells[0][columnIndex].getClass();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        cells[rowIndex][columnIndex] = aValue;
    }
    
}
