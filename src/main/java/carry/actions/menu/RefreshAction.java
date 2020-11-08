package carry.actions.menu;

import carry.actions.base.MyAnAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;

/**
 * @author Carry
 * @date 2020/11/6
 */
public class RefreshAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public RefreshAction() {
        super("Refresh", "Refresh configurations in the catalog", PluginIcons.REFRESH);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        PERSISTENCE.refresh = true;
        configsTreeController.getCatalogPanel().reload();
    }
    
}
