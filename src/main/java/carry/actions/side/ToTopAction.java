package carry.actions.side;

import carry.actions.base.MyAnAction;
import carry.common.data.AppPersistence.ToolbarLocation;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.desc.DescriptionPanel;
import carry.ui.desc.DescriptionToolbarPanel;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;

import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class ToTopAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public ToTopAction() {
        super("to Top", "Move toolbar to top", PluginIcons.MOVE_TO_TOP_RIGHT);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        DescriptionPanel descriptionPanel = configsTreeController.getDescriptionPanel();
        DescriptionToolbarPanel descriptionToolbarPanel = descriptionPanel.getDescriptionToolbarPanel();
        
        descriptionPanel.remove(descriptionToolbarPanel);
        descriptionPanel.add(descriptionToolbarPanel, BorderLayout.NORTH);
        descriptionPanel.revalidate();
        descriptionPanel.repaint();
        descriptionToolbarPanel.getSideToolBar().fireAtTop();
        PERSISTENCE.toolbarLocation = ToolbarLocation.TOP;
    }
    
}
