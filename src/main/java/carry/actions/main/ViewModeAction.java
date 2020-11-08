package carry.actions.main;

import carry.actions.ViewSettingsAction;
import carry.actions.base.MyToggleAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class ViewModeAction extends MyToggleAction implements ApplicationServiceAccessor {
    
    public ViewModeAction() {
        super("View Mode", "Fill the window for viewing", PluginIcons.VIEW_MODE, false);
    }
    
    @Override
    public void onSelect(Project project, ConfigsTreeController configsTreeController) {
        configsTreeController.getCatalogPopup().cancel();
        
        configsTreeController.getDescriptionPanel().fireReadjust();
        ViewSettingsAction.redispatch(project, configsTreeController);
        
        configsTreeController.getActionCenter().pinLocation.setSelected(project, PERSISTENCE.pinLocation);
    }
    
    @Override
    public void onUnselect(Project project, ConfigsTreeController configsTreeController) {
        configsTreeController.getViewModePopup().cancel();
        
        // make sure popups not null to activate, which handled in that action
        configsTreeController.getActionCenter().activateCatalog.setSelected(project,
                                                                            PERSISTENCE.activateCatalogNonViewMode);
        configsTreeController.getActionCenter().pinLocation.setSelected(project, PERSISTENCE.pinLocationNonViewMode);
        
        configsTreeController.getDescriptionPanel().fireReadjust();
        ViewSettingsAction.redispatch(project, configsTreeController);
    }
    
}
