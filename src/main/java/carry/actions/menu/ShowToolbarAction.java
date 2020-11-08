package carry.actions.menu;

import carry.actions.base.MyAnAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.base.BasePopupWrapper;
import carry.ui.desc.DescriptionPanel;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;

import java.awt.*;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class ShowToolbarAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public ShowToolbarAction() {
        super("Show Toolbar", "Show description toolbar", PluginIcons.SHOW_TOOL_WINDOW);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        BasePopupWrapper currentPopup =
                configsTreeController.nonViewMode() ? configsTreeController.getDescriptionPopup() :
                configsTreeController.getViewModePopup();
        DescriptionPanel descriptionPanel = configsTreeController.getDescriptionPanel();
        
        // size changes at the first time
        Dimension size = currentPopup.calcSize();
        descriptionPanel.add(descriptionPanel.getDescriptionToolbarPanel(), PERSISTENCE.toolbarLocation);
        descriptionPanel.revalidate();
        descriptionPanel.repaint();
        currentPopup.resizeTo(size);
        descriptionPanel.getMainPopupMenu().fireShowing();
        PERSISTENCE.toolbarVisible = true;
    }
    
}
