package demo.ui.components.editor.table;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class MCellRenderer implements TableCellRenderer {
    
    //从JTable源码里抄过来即可
    static class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {
        private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        
        public BooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
            setBorderPainted(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelected((value != null && ((Boolean) value).booleanValue()));
            
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(noFocusBorder);
            }
            
            return this;
        }
        
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component component;
    
        TableCellRenderer renderer;
        if (obj instanceof MColoredBoolValue) {
            MColoredBoolValue value = (MColoredBoolValue) obj;
            renderer = new BooleanRenderer();
            component =
                    renderer.getTableCellRendererComponent(table, value.getValue(), isSelected, hasFocus, row, column);
            if (value.isColored()) {
                component.setBackground(Color.RED);
            } else {
                component.setBackground(Color.WHITE);
            }
        } else if (obj instanceof MColoredStringValue) {
            MColoredStringValue value = (MColoredStringValue) obj;
            renderer = new DefaultTableCellRenderer();
            component =
                    renderer.getTableCellRendererComponent(table, value.getValue(), isSelected, hasFocus, row, column);
            if (value.isColored()) {
                component.setBackground(Color.RED);
            } else {
                component.setBackground(Color.WHITE);
            }
        } else if (obj instanceof Boolean) {
            renderer = new BooleanRenderer();
            component = renderer.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
            component.setBackground(Color.WHITE);
        } else {
            renderer = new DefaultTableCellRenderer();
            component = renderer.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
            component.setBackground(Color.WHITE);
        }
        
        return component;
    }
    
}
