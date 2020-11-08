package demo.ui.components.editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

/**
 * @author Carry
 * @date 2020/8/14
 */
public class SearchTable extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final int columnWithoutButton = 5;
    private Boolean[] enableBooleans = { true, false, false, true }; //记录button的启用/禁用状态
    
    public SearchTable() {
        setLayout(new BorderLayout());
        
        setBounds(10, 10, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setBackground(Color.white);
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setAutoscrolls(true);
        panel.add(scrollPane);
        
        tableModel = new DefaultTableModel() {
            // 设置列能否编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6 || column == 7 || column == 8;
            }
        };
        
        String[] headName = {
                "Column1", "Column2", "Column3", "Column4", "Column5", "Column6", "Column7", "Column8", "Column9"
        };
        tableModel.setColumnIdentifiers(headName);
        table = new JTable(tableModel);
        table.getColumn("Column6").setCellRenderer(new ButtonRenderer());
        // table.getColumn("Column6").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        table.getColumn("Column7").setCellRenderer(new ButtonRenderer());
        table.getColumn("Column7").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        table.getColumn("Column8").setCellRenderer(new ButtonRenderer());
        table.getColumn("Column8").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        table.getColumn("Column9").setCellRenderer(new ButtonRenderer());
        table.getColumn("Column9").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        table.setShowGrid(true);
        
        // 设置列标题不能移动
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        scrollPane.setViewportView(table);
        
        // 设置列宽,并且当调整Panel时列宽也能相应的变化
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //若设置它，列宽永远固定
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(55);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        
        // 设置行高
        table.setRowHeight(20);
        setVisible(true);
        
        initValue();
    }
    
    private void initValue() {
        for (int i = 0; i < 5; i++) {
            Vector<String> vector = new Vector<>();
            vector.addElement("1");
            vector.addElement("Open Close Msg");
            vector.addElement("do");
            vector.addElement("2");
            vector.addElement("2010-10-18 00:00:00");
            
            vector.addElement("Send");
            vector.addElement("Request");
            vector.addElement("Ack");
            vector.addElement("Emergency");
            
            tableModel.getDataVector().add(vector);
        }
    }
    
    public class ButtonRenderer extends JButton implements TableCellRenderer {
        
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            // 情况一：启用/禁用按钮
            setEnabled(enableBooleans[column - columnWithoutButton]);
            // 情况二：禁用按钮时，不显示按钮，显示空白
            // if(enableBooleans[column] - columnWithoutButton)
            //  setEnabled(setEnabled(enableBooleans[column-columnWithoutButton]);
            // else
            //  return null;
            return this;
        }
        
    }
    
    public class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> buttonClick());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table1, Object value, boolean isSelected, int row,
                int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            
            button.setText(table1.getValueAt(row, column).toString());
            
            // 情况一：启用/禁用按钮
            button.setEnabled(enableBooleans[column - columnWithoutButton]);
            // 情况二：禁用按钮时，不显示按钮，显示空白
            // if(enableBooleans[column] - columnWithoutButton)
            //  button.setEnabled(setEnabled(enableBooleans[column-columnWithoutButton]);
            // else
            //  return null;
            return button;
        }
        
        //若缺少此方法，点击按钮后将获取不到Button的Text值，将会显示false.
        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
        
        protected void buttonClick() {
            System.out.println(table.getSelectedColumn() + " and  " + table.getSelectedRow());
        }
        
    }
    
    public static void main(String args[]) {
        try {
            new SearchTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
