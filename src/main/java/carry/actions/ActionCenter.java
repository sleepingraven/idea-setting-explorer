package carry.actions;

import carry.actions.main.ActivateCatalogAction;
import carry.actions.main.GoToSettingAction;
import carry.actions.main.PinLocationAction;
import carry.actions.main.ViewModeAction;
import carry.actions.menu.*;
import carry.actions.side.ToBottomAction;
import carry.actions.side.ToTopAction;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class ActionCenter {
    private final ConfigsTreeController controller;
    
    public final ToBottomAction toBottom;
    public final ToTopAction toTop;
    
    public final HideToolbarAction hideToolbar;
    public final ShowToolbarAction showToolbar;
    
    public final GoToSettingAction goToSetting;
    
    public final ActivateCatalogAction activateCatalog;
    public final ViewModeAction viewMode;
    public final PinLocationAction pinLocation;
    
    public final CollapseAllAction collapseAll;
    public final GoToAppSettingsAction goToAppSettings;
    public final RefreshAction refreshAction;
    
    public final DefaultActionGroup mainGroup;
    
    public ActionCenter(ConfigsTreeController controller) {
        this.controller = controller;
        
        toBottom = new ToBottomAction();
        toTop = new ToTopAction();
        
        hideToolbar = new HideToolbarAction();
        showToolbar = new ShowToolbarAction();
        
        goToSetting = new GoToSettingAction();
        
        activateCatalog = new ActivateCatalogAction(controller);
        viewMode = new ViewModeAction();
        pinLocation = new PinLocationAction();
        
        collapseAll = new CollapseAllAction();
        goToAppSettings = new GoToAppSettingsAction();
        refreshAction = new RefreshAction();
        
        AnAction[] mainToolbarActions = {
                goToSetting, Separator.getInstance(), activateCatalog, viewMode, pinLocation,
        };
        mainGroup = new DefaultActionGroup(mainToolbarActions);
    }
    
}
