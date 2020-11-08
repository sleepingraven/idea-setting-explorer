package carry.ui.tree;

import carry.actions.ActionCenter;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.base.BasePopupWrapper;
import carry.ui.desc.DescriptionPanel;
import carry.ui.desc.DescriptionPopupWrapper;
import carry.ui.tree.ViewModePopupWrapper.ViewModePanel;
import carry.ui.tree.expansion.ExpansionRestore;
import carry.ui.tree.expansion.OnShowing;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author Carry
 * @date 2020/7/29
 */
@Getter
public class ConfigsTreeController implements ApplicationServiceAccessor {
    private final Project project;
    
    private final ExpansionRestore keeper;
    public boolean caseSensitive;
    
    private final CatalogPanel catalogPanel;
    private final DescriptionPanel descriptionPanel;
    private final ViewModePanel viewModePanel;
    private final CatalogPopupWrapper catalogPopup;
    private final DescriptionPopupWrapper descriptionPopup;
    private final ViewModePopupWrapper viewModePopup;
    
    private final ActionCenter actionCenter;
    
    public static ConfigsTreeController getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ConfigsTreeController.class);
    }
    
    public ConfigsTreeController(Project project) {
        this.project = project;
        actionCenter = new ActionCenter(this);
        
        catalogPanel = new CatalogPanel(this, () -> caseSensitive);
        catalogPopup = new CatalogPopupWrapper(this, catalogPanel);
        descriptionPanel = new DescriptionPanel(this, actionCenter);
        descriptionPopup = new DescriptionPopupWrapper(this, () -> catalogPopup, catalogPanel.tree, descriptionPanel);
        viewModePopup = new ViewModePopupWrapper(this);
        viewModePanel = viewModePopup.viewModePanel;
        
        keeper = OnShowing.builder().catalogPanel(catalogPanel).build();
        keeper.addRestorerListener();
    }
    
    public void showUi() {
        ConfigsUi ui;
        if (nonViewMode()) {
            ui = new SearchUi();
        } else {
            ui = new ViewUi();
        }
        
        ui.createUi();
        BasePopupWrapper[] currentPopups = ui.currentPopups();
        ApplicationManager.getApplication().invokeLater(() -> {
            catalogPanel.prepareToShow(currentPopups[0]).onSuccess(res -> {
                keeper.recover(o -> {
                    ui.showUi();
                    UIUtil.invokeLaterIfNeeded(() -> {
                        // stay here to make sure can get the location on screen of tree
                        catalogPanel.reselect();
                        catalogPanel.jbTextField.selectAll();
                    });
                    
                    for (BasePopupWrapper popup : currentPopups) {
                        actionCenter.goToSetting.registerCustomShortcutSet(GO_TO_SETTING, popup.getContent(), popup);
                        actionCenter.activateCatalog.registerCustomShortcutSet(ACTIVATE_CATALOG, popup.getContent(),
                                                                               popup);
                        actionCenter.viewMode.registerCustomShortcutSet(VIEW_MODE, popup.getContent(), popup);
                        actionCenter.pinLocation.registerCustomShortcutSet(PIN_LOCATION, popup.getContent(), popup);
                        // conflict fixed in the class
                        actionCenter.hideToolbar.registerCustomShortcutSet(TOOLBARS_VISIBILITY, popup.getContent(),
                                                                           popup);
                        actionCenter.showToolbar.registerCustomShortcutSet(TOOLBARS_VISIBILITY, popup.getContent(),
                                                                           popup);
                    }
                    
                    // if row == -1, TreeUtil can't "moveUp".
                    // now it causes a bug, and isn't need
                    if (catalogPanel.tree.getVisibleRowCount() > 0) {
                        // mainPanel.tree.setLeadSelectionPath(mainPanel.tree.getPathForRow(0));
                    }
                });
            });
        }, ModalityState.NON_MODAL, project.getDisposed());
    }
    
    private class SearchUi implements ConfigsUi {
        private final BasePopupWrapper[] currentPopups = { catalogPopup, descriptionPopup };
        
        @Override
        public void createUi() {
            catalogPopup.create();
        }
        
        @Override
        public void showUi() {
            catalogPopup.show();
            // stay here so that can cover jbPopup, and make focus
            descriptionPopup.show();
            if (actionCenter.activateCatalog.isSelected(null)) {
                catalogPopup.toFront();
            }
        }
        
        @Override
        public BasePopupWrapper[] currentPopups() {
            return currentPopups;
        }
        
    }
    
    private class ViewUi implements ConfigsUi {
        private final BasePopupWrapper[] currentPopups = { viewModePopup };
        
        @Override
        public void createUi() {
            viewModePopup.create();
        }
        
        @Override
        public void showUi() {
            viewModePopup.show();
        }
        
        @Override
        public BasePopupWrapper[] currentPopups() {
            return currentPopups;
        }
        
    }
    
    private interface ConfigsUi {
        
        void createUi();
        
        void showUi();
        
        BasePopupWrapper[] currentPopups();
        
    }
    
    public boolean nonViewMode() {
        return !actionCenter.viewMode.isSelected(null);
    }
    
    public static final CustomShortcutSet GO_TO_SETTING =
            new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.ALT_DOWN_MASK));
    public static final CustomShortcutSet ACTIVATE_CATALOG =
            new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_DOWN_MASK));
    public static final CustomShortcutSet VIEW_MODE =
            new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.ALT_DOWN_MASK));
    public static final CustomShortcutSet PIN_LOCATION =
            new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.ALT_DOWN_MASK));
    public static final CustomShortcutSet TOOLBARS_VISIBILITY =
            new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.ALT_DOWN_MASK));
    
}
