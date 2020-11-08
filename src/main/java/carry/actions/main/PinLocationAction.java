package carry.actions.main;

import carry.actions.base.MyToggleAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import icons.PluginIcons;

/**
 * @author Carry
 * @date 2020/8/26
 */
public class PinLocationAction extends MyToggleAction implements ApplicationServiceAccessor {
    
    public PinLocationAction() {
        super("Pin Location",
              "Avoid the relocation following the tree selection, or movement by mouse dragging (View Mode)",
              PluginIcons.PIN_LOCATION, PERSISTENCE.pinLocationNonViewMode);
    }
    
    @Override
    public void onSelect(Project project, ConfigsTreeController configsTreeController) {
        if (configsTreeController.nonViewMode()) {
            configsTreeController.getDescriptionPopup().locationPinned = true;
            PERSISTENCE.pinLocationNonViewMode = true;
        } else {
            configsTreeController.getViewModePopup().locationPinned = true;
            PERSISTENCE.pinLocation = true;
        }
    }
    
    @Override
    public void onUnselect(Project project, ConfigsTreeController configsTreeController) {
        if (configsTreeController.nonViewMode()) {
            configsTreeController.getDescriptionPopup().locationPinned = false;
            PERSISTENCE.pinLocationNonViewMode = false;
        } else {
            configsTreeController.getViewModePopup().locationPinned = false;
            PERSISTENCE.pinLocation = false;
        }
    }
    
}
