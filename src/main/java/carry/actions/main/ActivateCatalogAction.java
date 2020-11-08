package carry.actions.main;

import carry.actions.base.MyToggleAction;
import carry.common.AppConstants;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import carry.ui.tree.ViewModePopupWrapper.ViewModePanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class ActivateCatalogAction extends MyToggleAction implements ApplicationServiceAccessor {
    private final ConfigsTreeController controller;
    private float proportion;
    private boolean selection;
    
    public ActivateCatalogAction(ConfigsTreeController controller) {
        super("Activate Catalog", "Show main catalog at front", PluginIcons.ACTIVATE_CATALOG,
              PERSISTENCE.activateCatalogNonViewMode);
        this.controller = controller;
    }
    
    @Override
    public boolean isSelected(@Nullable AnActionEvent e) {
        if (!controller.nonViewMode() && !selection) {
            selected = !controller.getViewModePanel().inactive();
        }
        return selected;
    }
    
    @Override
    public void setSelected(@NotNull Project project, boolean state) {
        selection = true;
        super.setSelected(project, state);
        selection = false;
    }
    
    @Override
    public void onSelect(Project project, ConfigsTreeController configsTreeController) {
        if (configsTreeController.nonViewMode()) {
            // prevent a exception at leaving view mode
            if (!configsTreeController.getCatalogPopup().isDisposed()) {
                configsTreeController.getCatalogPopup().toFront();
            }
            PERSISTENCE.activateCatalogNonViewMode = true;
        } else {
            ViewModePanel viewModePanel = configsTreeController.getViewModePanel();
            if (!viewModePanel.inactive()) {
                return;
            }
            if (viewModePanel.inactive(proportion)) {
                proportion = AppConstants.DEFAULT_PROPORTION;
            }
            
            configsTreeController.getDescriptionPanel().fireReadjust();
            viewModePanel.setProportion(proportion);
        }
    }
    
    @Override
    public void onUnselect(Project project, ConfigsTreeController configsTreeController) {
        if (configsTreeController.nonViewMode()) {
            // toBack may make a flicker of Screen
            if (!configsTreeController.getDescriptionPopup().isDisposed()) {
                configsTreeController.getDescriptionPopup().toFront();
            }
            PERSISTENCE.activateCatalogNonViewMode = false;
        } else {
            ViewModePanel viewModePanel = configsTreeController.getViewModePanel();
            if (viewModePanel.inactive()) {
                return;
            }
            proportion = viewModePanel.getProportion();
            
            configsTreeController.getDescriptionPanel().fireReadjust();
            viewModePanel.setProportion(0);
        }
    }
    
}
