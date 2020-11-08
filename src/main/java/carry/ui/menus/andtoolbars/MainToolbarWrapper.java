package carry.ui.menus.andtoolbars;

import carry.actions.ActionCenter;
import carry.ui.base.BaseToolbarWrapper;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class MainToolbarWrapper extends BaseToolbarWrapper {
    
    public MainToolbarWrapper(ActionCenter actionCenter) {
        super(actionCenter.mainGroup, "idea-setting-explorer-group:Description-Toolbar-Main");
        ((ActionToolbarImpl) myToolbar).setForceMinimumSize(true);
    }
    
}
