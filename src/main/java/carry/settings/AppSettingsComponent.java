package carry.settings;

import carry.settings.base.BaseSettingsComponent;
import carry.ui.tree.ConfigsTreeCellRender.MyTitledSeparator;
import com.intellij.ui.JBIntSpinner;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI.CurrentTheme;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Supports creating and managing a JPanel for the Settings Dialog.
 *
 * @author Carry
 * @date 2020/7/26
 */
public class AppSettingsComponent extends BaseSettingsComponent {
    private final JBCheckBox myRefreshOnStartup;
    
    private final InverseIntSpinner mySeparatorAdjustValue;
    private final MyTitledSeparator myTitledSeparator;
    private final Tree separatorView;
    
    public AppSettingsComponent() {
        myRefreshOnStartup = new JBCheckBox("Refresh on startup");
        
        mySeparatorAdjustValue = new InverseIntSpinner(0, 0, 20);
        myTitledSeparator = new MyTitledSeparator("Separator", this);
        separatorView = new SimpleTree(new DefaultTreeModel(new DefaultMutableTreeNode(new Object())));
        
        separatorView.setCellRenderer((tree, value, selected, expanded, leaf, row, hasFocus) -> myTitledSeparator);
        separatorView.setOpaque(false);
        separatorView.setEnabled(false);
        separatorView.setBackground(CurrentTheme.CustomFrameDecorations.paneBackground());
        mySeparatorAdjustValue.addChangeListener(e -> {
            myTitledSeparator.adjust(mySeparatorAdjustValue.getNumber());
            separatorView.revalidate();
            separatorView.repaint();
        });
    }
    
    @Override
    public FormBuilder createFormBuilder() {
        return FormBuilder.createFormBuilder()
                          .addLabeledComponent(myRefreshOnStartup, new JPanel(), false)
                          .addComponent(label("Set separator's position if it doesn't display correctly"), 8)
                          .addLabeledComponent(mySeparatorAdjustValue, separatorView, 1, false)
                          .addComponentFillVertically(new JPanel(), 0);
    }
    
    @Override
    public JComponent getPreferredFocused() {
        return myRefreshOnStartup;
    }
    
    /* wrapped components */
    
    public int getSeparatorAdjustValue() {
        return mySeparatorAdjustValue.getNumber();
    }
    
    public void setSeparatorAdjustValue(int val) {
        mySeparatorAdjustValue.setNumber(val);
    }
    
    public boolean getRefreshOnStartup() {
        return myRefreshOnStartup.isSelected();
    }
    
    public void setRefreshOnStartup(boolean booleanValue) {
        myRefreshOnStartup.setSelected(booleanValue);
    }
    
    /* components' definition an creator */
    
    static class InverseIntSpinner extends JBIntSpinner {
        
        public InverseIntSpinner(int value, int minValue, int maxValue) {
            super(value, minValue, maxValue);
            setModel(new SpinnerNumberModel(value, minValue, maxValue, 1) {
                @Override
                public Object getNextValue() {
                    return super.getPreviousValue();
                }
                
                @Override
                public Object getPreviousValue() {
                    return super.getNextValue();
                }
            });
        }
        
    }
    
}
