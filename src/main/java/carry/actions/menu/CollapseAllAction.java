package carry.actions.menu;

import carry.actions.base.MyAnAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.util.ui.tree.TreeUtil;
import icons.PluginIcons;

/**
 * @author Carry
 * @date 2020/11/7
 */
public class CollapseAllAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public CollapseAllAction() {
        super("Collapse All", "Collapse all nodes in the catalog tree", PluginIcons.COLLAPSE_ALL);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        TreeUtil.collapseAll(configsTreeController.getCatalogPanel().tree, false, 0);
        IdeFocusManager.getInstance(project).requestFocus(configsTreeController.getCatalogPanel().tree, true);
    }
    
}
