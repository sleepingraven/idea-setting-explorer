package carry.ui.menus.andtoolbars;

import carry.actions.ActionCenter;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.base.BasePopupMenuWrapper;
import com.intellij.openapi.actionSystem.*;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class MainPopupMenu extends BasePopupMenuWrapper implements ApplicationServiceAccessor {
    private final ActionCenter actionCenter;
    
    public MainPopupMenu(ActionCenter actionCenter) {
        super(new AnAction[] {
                PERSISTENCE.toolbarVisible ? actionCenter.hideToolbar : actionCenter.showToolbar,
                Separator.getInstance(), actionCenter.collapseAll, actionCenter.mainGroup, Separator.getInstance(),
                actionCenter.goToAppSettings, actionCenter.refreshAction,
        });
        this.actionCenter = actionCenter;
    }
    
    @Override
    protected ActionPopupMenu createPopupMenu() {
        actionCenter.hideToolbar.prepareIcon();
        return ActionManager.getInstance()
                            .createActionPopupMenu("idea-setting-explorer-group:Description-PopupMenu-Main",
                                                   myActionGroup);
    }
    
    public void fireHiding() {
        myActionGroup.replaceAction(actionCenter.hideToolbar, actionCenter.showToolbar);
    }
    
    public void fireShowing() {
        myActionGroup.replaceAction(actionCenter.showToolbar, actionCenter.hideToolbar);
    }
    
}
