package demo.ui.components.editor.table2;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class ColorTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JColorChooser chooser;
    private final JDialog dialog;
    private final JPanel panel;
    
    public ColorTableCellEditor() {
        panel = new JPanel();
        chooser = new JColorChooser();
        dialog = JColorChooser.createDialog(null, "Planet Color", false, chooser, e -> stopCellEditing(),
                                            e -> cancelCellEditing());
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelCellEditing();
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }
    
    // 编辑单元格时这个组件会暂时取代绘制器
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        chooser.setColor((Color) value);
        return panel;
    }
    
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        dialog.setVisible(true);
        return true;
    }
    
    @Override
    public void cancelCellEditing() {
        dialog.setVisible(false);
        super.cancelCellEditing();
    }
    
    @Override
    public boolean stopCellEditing() {
        // 会调用getCellEditorValue方法的值更改模型数据
        dialog.setVisible(false);
        super.stopCellEditing();
        return true;
    }
    
    @Override
    public Color getCellEditorValue() {
        return chooser.getColor();
    }
    
    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
    
}
