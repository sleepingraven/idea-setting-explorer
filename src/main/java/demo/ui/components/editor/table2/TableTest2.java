package demo.ui.components.editor.table2;

import javax.swing.*;
import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class TableTest2 {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new PlanetTableFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(800, 500));
            frame.setLocationRelativeTo(frame.getOwner());
            frame.setVisible(true);
        });
    }
    
}
