/*
 * Created by JFormDesigner on Tue Aug 04 17:29:22 GMT+08:00 2020
 */
package demo.ui;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * @author Carry
 */
public class JTreeDemo extends JFrame {
    public JTreeDemo() {
        initComponents();
    }
    
    public static void main(String[] args) {
        JTreeDemo demo = new JTreeDemo();
        DefaultTreeCellRenderer render = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                return new JLabel("hah");
            }
        };
        demo.tree.setEditable(true);
        // demo.tree.setCellRenderer(render);
        TreeCellEditor hah = new TreeCellEditor() {
            @Override
            public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
                    boolean leaf, int row) {
                JLabel hah = new JLabel("hah");
                hah.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                    }
                });
                return hah;
            }
            
            @Override
            public Object getCellEditorValue() {
                return "hah";
            }
            
            @Override
            public boolean isCellEditable(EventObject anEvent) {
                return true;
            }
            
            @Override
            public boolean shouldSelectCell(EventObject anEvent) {
                return true;
            }
            
            @Override
            public boolean stopCellEditing() {
                return true;
            }
            
            @Override
            public void cancelCellEditing() {
            }
            
            @Override
            public void addCellEditorListener(CellEditorListener l) {
            }
            
            @Override
            public void removeCellEditorListener(CellEditorListener l) {
            }
        };
        // demo.tree.setCellEditor(hah);
        // demo.tree.setCellEditor(new DefaultCellEditor(new JTextField("hahhh")));
        
        demo.setVisible(true);
    }
    
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane = new JScrollPane();
        tree = new JTree();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

        //======== scrollPane ========
        {

            //---- tree ----
            tree.setModel(new DefaultTreeModel(
                new DefaultMutableTreeNode("(root)") {
                    {
                        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("1");
                            node1.add(new DefaultMutableTreeNode("2"));
                            node1.add(new DefaultMutableTreeNode("3"));
                        add(node1);
                        node1 = new DefaultMutableTreeNode("4");
                            node1.add(new DefaultMutableTreeNode("5"));
                            node1.add(new DefaultMutableTreeNode("6"));
                        add(node1);
                        node1 = new DefaultMutableTreeNode("7");
                            DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("8");
                                node2.add(new DefaultMutableTreeNode("9"));
                            node1.add(node2);
                        add(node1);
                    }
                }));
            tree.setRootVisible(false);
            scrollPane.setViewportView(tree);
        }
        contentPane.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane;
    private JTree tree;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
