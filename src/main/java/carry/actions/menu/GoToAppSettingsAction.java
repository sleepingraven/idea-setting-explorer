package carry.actions.menu;

import carry.actions.base.MyAnAction;
import carry.common.AppConstants;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class GoToAppSettingsAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public GoToAppSettingsAction() {
        super("Settings", AppConstants.PLUGIN_NAME + " settings", PluginIcons.SETTINGS);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        CONFIGS_UTIL.showSettingsDialog(configsTreeController.getProject(), AppConstants.PLUGIN_NAME, null);
    }
    
}
