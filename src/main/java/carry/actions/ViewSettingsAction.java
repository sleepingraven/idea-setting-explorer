package carry.actions;

import carry.actions.base.MyAnAction;
import carry.common.AppConstants;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;

/**
 * @author Carry
 * @date 2020/7/23
 */
public class ViewSettingsAction extends MyAnAction {
    public static final String ACTION_ID = AppConstants.getCommonId(ViewSettingsAction.class);
    
    public ViewSettingsAction() {
        super(AppConstants.VIEW_TITLE, AppConstants.VIEW_DESCRIPTION, null);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        // new DemoDispatcher().doDemo(project, e);
        
        // ConfigsListSearchPopup.getInstance(project).show();
        configsTreeController.showUi();
    }
    
    public static void redispatch(Project project, ConfigsTreeController configsTreeController) {
        ViewSettingsAction viewSettingsAction = (ViewSettingsAction) ActionManager.getInstance().getAction(ACTION_ID);
        viewSettingsAction.actionPerformed(project, configsTreeController);
    }
    
}
