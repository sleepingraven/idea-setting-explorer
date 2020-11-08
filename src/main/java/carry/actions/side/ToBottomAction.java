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
public class ToBottomAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public ToBottomAction() {
        super("to Bottom", "Move toolbar to bottom", PluginIcons.MOVE_TO_BOTTOM_RIGHT);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        DescriptionPanel descriptionPanel = configsTreeController.getDescriptionPanel();
        DescriptionToolbarPanel descriptionToolbarPanel = descriptionPanel.getDescriptionToolbarPanel();
        
        descriptionPanel.remove(descriptionToolbarPanel);
        descriptionPanel.add(descriptionToolbarPanel, BorderLayout.SOUTH);
        descriptionPanel.revalidate();
        descriptionPanel.repaint();
        descriptionToolbarPanel.getSideToolBar().fireAtBottom();
        PERSISTENCE.toolbarLocation = ToolbarLocation.BOTTOM;
    }
    
}
