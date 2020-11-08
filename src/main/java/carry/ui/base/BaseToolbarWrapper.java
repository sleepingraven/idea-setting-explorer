package carry.ui.base;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class BaseToolbarWrapper {
    protected final DefaultActionGroup myActionGroup;
    protected final ActionToolbar myToolbar;
    
    public BaseToolbarWrapper(AnAction[] anActions, String place) {
        this(new DefaultActionGroup(anActions), place);
    }
    
    public BaseToolbarWrapper(DefaultActionGroup myActionGroup, String place) {
        this.myActionGroup = myActionGroup;
        myToolbar = ActionManager.getInstance().createActionToolbar(place, myActionGroup, true);
    }
    
    public JComponent getComponent() {
        return myToolbar.getComponent();
    }
    
}
