package carry.actions.main;

import carry.actions.base.MyAnAction;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import icons.PluginIcons;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class GoToSettingAction extends MyAnAction {
    
    public GoToSettingAction() {
        super(!SystemInfo.isMac ? "Show Settings" : "Show Preferences",
              !SystemInfo.isMac ? "Show in settings dialog" : "Show in preferences dialog", PluginIcons.GO_TO_SETTING);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        configsTreeController.getCatalogPanel().okIfSelected();
    }
    
}
