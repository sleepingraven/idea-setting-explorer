package demo.ui.components.editor.table;

import org.jdesktop.swingx.JXTable.GenericEditor;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class MCellEditor implements TableCellEditor {
    private TableCellEditor editor;
    
    static class ColoredStringEditor extends GenericEditor {
        private MColoredStringValue value;
        
        @Override
        public MColoredStringValue getCellEditorValue() {
            String vString = (String) super.getCellEditorValue();
            value.setValue(vString);
            return value;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.value = (MColoredStringValue) value;
            return super.getTableCellEditorComponent(table, this.value.getValue(), isSelected, row, column);
        }
        
    }
    
    static class ColoredBooleanEditor extends DefaultCellEditor {
        
        private MColoredBoolValue value;
        
        public ColoredBooleanEditor() {
            super(new JCheckBox());
            JCheckBox checkBox = (JCheckBox) getComponent();
            checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        }
        
        @Override
        public MColoredBoolValue getCellEditorValue() {
            Boolean vBoolean = (Boolean) super.getCellEditorValue();
            value.setValue(vBoolean);
            return value;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.value = (MColoredBoolValue) value;
            return super.getTableCellEditorComponent(table, this.value.getValue(), isSelected, row, column);
        }
        
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener arg0) {
        editor.addCellEditorListener(arg0);
    }
    
    @Override
    public void cancelCellEditing() {
        editor.cancelCellEditing();
    }
    
    @Override
    public Object getCellEditorValue() {
        return editor.getCellEditorValue();
    }
    
    @Override
    public boolean isCellEditable(EventObject arg0) {
        return true;
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener arg0) {
        editor.removeCellEditorListener(arg0);
    }
    
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
    
    @Override
    public boolean stopCellEditing() {
        return editor.stopCellEditing();
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row, int column) {
        Component component = null;
        
        if (obj instanceof MColoredBoolValue) {
            editor = new ColoredBooleanEditor();
            component = editor.getTableCellEditorComponent(table, obj, isSelected, row, column);
            if (((MColoredBoolValue) obj).isColored()) {
                component.setBackground(Color.RED);
            } else {
                component.setBackground(Color.WHITE);
            }
        } else if (obj instanceof MColoredStringValue) {
            editor = new ColoredStringEditor();
            component = editor.getTableCellEditorComponent(table, obj, isSelected, row, column);
            if (((MColoredStringValue) obj).isColored()) {
                component.setBackground(Color.RED);
            } else {
                component.setBackground(Color.WHITE);
            }
        }
        
        return component;
    }
    
}
