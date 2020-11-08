/*
 * Created by JFormDesigner on Tue Jul 28 19:27:15 GMT+08:00 2020
 */

package demo.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Carry
 */
public class GridBagLayoutDemo extends JFrame {
    
    public GridBagLayoutDemo() {
        initComponents();
    }

    private void listMousePressed(MouseEvent e) {
    }

    private void textFieldKeyPressed(KeyEvent e) {
    }

    private void mainPanelFocusGained(FocusEvent e) {
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        mainPanel = new JPanel();
        textField = new JTextField();
        scrollPane1 = new JScrollPane();
        list = new JList<>();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new CardLayout());

        //======== mainPanel ========
        {
            mainPanel.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    mainPanelFocusGained(e);
                }
            });
            mainPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)mainPanel.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)mainPanel.getLayout()).rowHeights = new int[] {20, 4, 0};
            ((GridBagLayout)mainPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
            ((GridBagLayout)mainPanel.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

            //---- textField ----
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    textFieldKeyPressed(e);
                }
            });
            mainPanel.add(textField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

            //======== scrollPane1 ========
            {

                //---- list ----
                list.setModel(new AbstractListModel<String>() {
                    String[] values = {
                        "123",
                        "4",
                        "56"
                    };
                    @Override
                    public int getSize() { return values.length; }
                    @Override
                    public String getElementAt(int i) { return values[i]; }
                });
                list.setSelectedIndex(0);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        listMousePressed(e);
                    }
                });
                scrollPane1.setViewportView(list);
            }
            mainPanel.add(scrollPane1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(mainPanel, "card1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel mainPanel;
    private JTextField textField;
    private JScrollPane scrollPane1;
    private JList<String> list;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    
    public static void main(String[] args) {
    }
}
