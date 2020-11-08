package demo.ui.components.editor.tree;

import javax.swing.*;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Carry
 * @date 2020/8/14
 */
public class Start {
    
    public static class ButtonCellEditor extends AbstractCellEditor implements TreeCellEditor, ActionListener, MouseListener {
        private JButton button;
        private JLabel label;
        private JPanel panel;
        private Object value;
        
        public ButtonCellEditor() {
            panel = new JPanel(new BorderLayout());
            button = new JButton("Press me!");
            button.addActionListener(this);
            label = new JLabel();
            label.addMouseListener(this);
            panel.add(button, BorderLayout.EAST);
            panel.add(label, BorderLayout.CENTER);
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }
            });
            
            // panel.setPreferredSize(new Dimension(450, 30));
            panel.setMinimumSize(new Dimension(450, 30));
            panel.setMaximumSize(new Dimension(450, 30));
            // panel.setSize(new Dimension(450, 30));
        }
        
        @Override
        public Object getCellEditorValue() {
            return value.toString();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String val = value.toString();
            System.out.println("Pressed: " + val);
            // stopCellEditing();
        }
        
        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
                boolean leaf, int row) {
            this.value = value;
            label.setText(value.toString());
            return panel;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            String val = value.toString();
            System.out.println("Clicked: " + val);
            stopCellEditing();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }
    
    public static class ButtonCellRenderer extends JPanel implements TreeCellRenderer {
        TreeCellRenderer cellRenderer;
        JButton button;
        JLabel label;
        
        ButtonCellRenderer(TreeCellRenderer cellRenderer) {
            super(new BorderLayout());
            this.cellRenderer = cellRenderer;
            button = new JButton("Press me!");
            label = new JLabel();
            add(button, BorderLayout.EAST);
            add(label, BorderLayout.CENTER);
        }
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            label.setText(value.toString());
            // add(cellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus),
            //     BorderLayout.CENTER);
            return this;
        }
        
    }
    
    public static void main(String[] args) {
        JTree tree = new JTree();
        tree.setEditable(true);
        tree.setCellRenderer(new ButtonCellRenderer(tree.getCellRenderer()));
        tree.setCellEditor(new ButtonCellEditor());
        
        JFrame test = new JFrame();
        test.add(new JScrollPane(tree));
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setSize(500, 500);
        test.setLocationRelativeTo(null);
        test.setVisible(true);
    }
    
}
