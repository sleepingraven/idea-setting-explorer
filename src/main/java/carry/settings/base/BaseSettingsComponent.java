package carry.settings.base;

import com.intellij.openapi.Disposable;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/9/1
 */
public abstract class BaseSettingsComponent implements Disposable {
    protected JPanel myMainPanel;
    
    public BaseSettingsComponent() {
    }
    
    public JPanel getPanel() {
        // can't init in constructor
        if (myMainPanel == null) {
            myMainPanel = createFormBuilder().getPanel();
        }
        return myMainPanel;
    }
    
    public JLabel label(String text) {
        return new JLabel(text);
    }
    
    public abstract FormBuilder createFormBuilder();
    
    public abstract JComponent getPreferredFocused();
    
    @Override
    public void dispose() {
    }
    
}
