package carry.ui.menus.andtoolbars;

import carry.actions.ActionCenter;
import carry.actions.base.HiddenAction;
import carry.common.data.AppPersistence.ToolbarLocation;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.base.BaseToolbarWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Separator;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class SideToolbarWrapper extends BaseToolbarWrapper implements ApplicationServiceAccessor {
    private final ActionCenter actionCenter;
    
    public SideToolbarWrapper(ActionCenter actionCenter) {
        super(new AnAction[] {
                ToolbarLocation.TOP.equals(PERSISTENCE.toolbarLocation) ? actionCenter.toBottom : actionCenter.toTop,
                Separator.getInstance(), HiddenAction.getInstance(),
        }, "idea-setting-explorer-group:Description-Toolbar-Side");
        this.actionCenter = actionCenter;
    }
    
    public void fireAtBottom() {
        myActionGroup.replaceAction(actionCenter.toBottom, actionCenter.toTop);
    }
    
    public void fireAtTop() {
        myActionGroup.replaceAction(actionCenter.toTop, actionCenter.toBottom);
    }
    
}
