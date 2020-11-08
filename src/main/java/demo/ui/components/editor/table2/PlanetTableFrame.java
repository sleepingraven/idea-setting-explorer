package demo.ui.components.editor.table2;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.print.PrinterException;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class PlanetTableFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 200;
    
    public PlanetTableFrame() {
        setTitle("TableTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        TableModel model = new MyTableModel();
        final JTable table = new JTable(model);
        // 自动排序
        table.setAutoCreateRowSorter(true);
        // 填充视口
        table.setFillsViewportHeight(true);
        // 设置渲染器
        table.setDefaultRenderer(Color.class, new ColorRender());
        // 设置编辑器
        table.setDefaultEditor(Color.class, new ColorTableCellEditor());
        table.setRowSelectionAllowed(false);
        Font font = table.getFont();
        table.setFont(new Font(font.getName(),font.getStyle(),20));
        table.setRowHeight(30);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton printButton = new JButton("Print");
        printButton.addActionListener(event -> {
            try {
                table.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(printButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private static class ColorRender extends JPanel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground((Color) value);
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(null);
            }
            return this;
        }
        
    }
    
}
