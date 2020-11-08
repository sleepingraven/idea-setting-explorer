package carry.actions.base;

import com.intellij.openapi.actionSystem.ex.DefaultCustomComponentAction;

import javax.swing.*;

/**
 * make of a separator at the side
 *
 * @author Carry
 * @date 2020/8/26
 */
public class HiddenAction extends DefaultCustomComponentAction {
    private static final HiddenAction SINGLETON_INSTANCE = new HiddenAction();
    
    private HiddenAction() {
        super(JLabel::new);
    }
    
    public static HiddenAction getInstance() {
        return SINGLETON_INSTANCE;
    }
    
}
