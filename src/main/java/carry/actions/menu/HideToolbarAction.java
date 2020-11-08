package carry.actions.menu;

import carry.actions.base.MyAnAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.desc.DescriptionPanel;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import icons.PluginIcons;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class HideToolbarAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public HideToolbarAction() {
        super("Hide Toolbar", "Hide description toolbar", null);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        if (!PERSISTENCE.toolbarVisible) {
            configsTreeController.getActionCenter().showToolbar.actionPerformed(project, configsTreeController);
            return;
        }
        
        DescriptionPanel descriptionPanel = configsTreeController.getDescriptionPanel();
        
        descriptionPanel.remove(descriptionPanel.getDescriptionToolbarPanel());
        descriptionPanel.revalidate();
        descriptionPanel.repaint();
        descriptionPanel.getMainPopupMenu().fireHiding();
        PERSISTENCE.toolbarVisible = false;
    }
    
    public void prepareIcon() {
        Icon newIcon = !UIUtil.isUnderDarcula() ? PluginIcons.HIDE_TOOL_WINDOW : PluginIcons.HIDE_TOOL_WINDOW_dark;
        getTemplatePresentation().setIcon(newIcon);
    }
    
}
