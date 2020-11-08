package demo.ui.components.editor.table;

import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class TableTest1 {
    
    public static void main(String[] args) {
        Object[][] data = new Object[][] {
                { new MColoredBoolValue(false, false), new MColoredStringValue("hah1", false), Boolean.FALSE },
                { new MColoredBoolValue(false, true), new MColoredStringValue("hah2", true), Boolean.TRUE },
        };
        String[] columnNames = new String[] { "Colored", "String", "Bool" };
        JTable jTable = new JTable(new DefaultTableModel(data, columnNames) {
            Class<?>[] cols = new Class[] { MColoredBoolValue.class, MColoredStringValue.class, Boolean.class };
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return cols[columnIndex];
            }
        });
        
        MCellRenderer renderer = new MCellRenderer();
        jTable.setDefaultRenderer(MColoredBoolValue.class, renderer);
        jTable.setDefaultRenderer(MColoredStringValue.class, renderer);
        MCellEditor anEditor = new MCellEditor();
        jTable.setDefaultEditor(MColoredBoolValue.class, anEditor);
        jTable.setDefaultEditor(MColoredStringValue.class, anEditor);
        
        // show(jTable);
        showFrame(jTable);
    }
    
    static void showFrame(JTable jTable) {
        JFrame frame = new JFrame("Book Tree");
        frame.getContentPane().add(jTable, BorderLayout.CENTER);
        frame.setSize(920, 470);
        frame.setLocationRelativeTo(frame.getOwner());
        frame.setVisible(true);
    }
    
    private static void show(JTable jTable) {
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(jTable, BorderLayout.CENTER);
        ComponentPopupBuilder builder = JBPopupFactory.getInstance()
                                                      .createComponentPopupBuilder(jPanel, jTable)
                                                      .setCancelOnClickOutside(true)
                                                      .setCancelOnOtherWindowOpen(false)
                                                      .setCancelOnWindowDeactivation(false)
                                                      .setCancelKeyEnabled(false)
                                                      .setAdText("Press " + KeymapUtil.getShortcutsText(
                                                              CommonShortcuts.ALT_ENTER.getShortcuts()) +
                                                                 " to collapse/expand")
                                                      .setTitle("Search Settings")
                                                      .setMovable(true)
                                                      .setResizable(true)
                                                      .setRequestFocus(true)
                                                      .setMayBeParent(true)
                                                      .setMinSize(new Dimension(800, 500));
        JBPopup jbPopup = builder.createPopup();
        jbPopup.showInFocusCenter();
    }
    
}
