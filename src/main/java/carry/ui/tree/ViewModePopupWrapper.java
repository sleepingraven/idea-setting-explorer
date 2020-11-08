package carry.ui.tree;

import carry.actions.base.MyShortcuts;
import carry.common.AppConstants;
import carry.ui.base.BoundKeepingPopupWrapper;
import carry.ui.desc.DescriptionPanel;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.impl.IdeFrameDecorator;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Carry
 * @date 2020/8/28
 */
public class ViewModePopupWrapper extends BoundKeepingPopupWrapper {
    private final ConfigsTreeController controller;
    private final CatalogPanel catalogPanel;
    private final DescriptionPanel descriptionPanel;
    final ViewModePanel viewModePanel;
    
    private boolean firstTimeShowing = true;
    
    public boolean locationPinned = PERSISTENCE.pinLocation;
    
    public ViewModePopupWrapper(ConfigsTreeController controller) {
        super(new Point(), PERSISTENCE.getViewModePopupSize(), e -> {
            if (controller.getViewModePopup().locationPinned) {
                if (!UIUtil.isControlKeyDown(e)) {
                    return false;
                }
                controller.getActionCenter().pinLocation.setSelected(controller.getProject(), false);
            }
            controller.getViewModePopup().descriptionPanel.setSelectable(false);
            return true;
        }, e -> {
            controller.getViewModePopup().descriptionPanel.setSelectable(true);
        });
        this.controller = controller;
        catalogPanel = controller.getCatalogPanel();
        descriptionPanel = controller.getDescriptionPanel();
        this.viewModePanel = new ViewModePanel();
    }
    
    @Override
    public void doCreate() {
        viewModePanel.setFirstComponent(null);
        viewModePanel.setSecondComponent(null);
        viewModePanel.setFirstComponent(catalogPanel);
        viewModePanel.setSecondComponent(descriptionPanel);
        
        final JComponent toFocusComponent = catalogPanel.tree;
        ComponentPopupBuilder builder = JBPopupFactory.getInstance()
                                                      .createComponentPopupBuilder(viewModePanel, toFocusComponent)
                                                      .setCancelOnClickOutside(true)
                                                      .setCancelOnOtherWindowOpen(false)
                                                      .setCancelOnWindowDeactivation(false)
                                                      .setCancelKeyEnabled(false)
                                                      .setMovable(true)
                                                      .setResizable(true)
                                                      .setRequestFocus(true)
                                                      .setMayBeParent(true)
                                                      .setLocateWithinScreenBounds(false);
        jbPopup = builder.createPopup();
        JComponent content = jbPopup.getContent();
        
        // higher size of jbTextField
        Dimension preferredSize = catalogPanel.jbTextField.getPreferredSize();
        final int nonHeight = preferredSize.height;
        int height = descriptionPanel.getDescriptionToolbarPanel().getHeight();
        if (height != 0) {
            PERSISTENCE.viewModeSearchTextFieldHeight = height;
        }
        preferredSize.height = PERSISTENCE.viewModeSearchTextFieldHeight;
        catalogPanel.jbTextField.setPreferredSize(preferredSize);
        jbPopup.addListener(new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                catalogPanel.clearStatusBar(controller.getProject());
                // restore size of jbTextField
                // Dimension preferredSize = catalogPanel.jbTextField.getPreferredSize();
                // preferredSize.height = nonHeight;
                // catalogPanel.jbTextField.setPreferredSize(preferredSize);
                catalogPanel.jbTextField.setPreferredSize(null);
                
                catalogPanel.lastSelectedRow = catalogPanel.tree.getSelectionModel().getLeadSelectionRow();
            }
        });
        
        // apply shortcuts
        MyShortcuts.register(CommonShortcuts.ESCAPE, content, (e) -> jbPopup.cancel(), true);
        MyShortcuts.register(MyShortcuts.SWITCH_SHORTCUT, content, (e) -> {
            final JComponent toFocus;
            if (!catalogPanel.tree.hasFocus()) {
                toFocus = catalogPanel.tree;
            } else {
                toFocus = catalogPanel.jbTextField;
            }
            IdeFocusManager.getInstance(controller.getProject()).requestFocus(toFocus, true);
        }, false);
        // TreeActionsInstaller.install(catalogPanel.tree, content);
        
        // movement and size keeper
        descriptionPanel.addMouseAdapter(movetiveAdapter);
        descriptionPanel.addComponentListener(sizeRemember);
        Disposer.register(jbPopup, () -> {
            descriptionPanel.removeMouseAdapter(movetiveAdapter);
            descriptionPanel.removeComponentListener(sizeRemember);
        });
    }
    
    @Override
    public void show() {
        jbPopup.setLocation(location);
        jbPopup.setSize(size);
        
        if (firstTimeShowing) {
            jbPopup.showInFocusCenter();
        } else {
            // cause x+1,y+1 if fills screen
            // jbPopup.show(WindowManager.getInstance().findVisibleFrame());
            jbPopup.show(new RelativePoint(location));
        }
        firstTimeShowing = false;
    }
    
    public static class ViewModePanel extends OnePixelSplitter {
        public static final int MIN_WIDTH = 4;
        
        private float activeLimit;
        
        public ViewModePanel() {
            super(false, AppConstants.VIEW_MODE_PROPORTION, AppConstants.DEFAULT_PROPORTION);
            // prevent the unnormal of activateCatalog button
            loadProportion();
            setHonorComponentsMinimumSize(false);
            if (IdeFrameDecorator.isCustomDecorationActive()) {
                getDivider().setOpaque(false);
            }
            
            // trigger in a interval, can't update at the real time
            addPropertyChangeListener(PROP_PROPORTION, evt -> {
                // controller.getMainToolBar().getActivateCatalog().setState(!inactive());
            });
            // now activeLimit is always 0
            // if use this, may need to init it
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    // activeLimit = (float) MIN_WIDTH / getWidth();
                }
            });
            
            // can trigger all over the panel, without mouse outside
            getDivider().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                }
            });
        }
        
        public boolean inactive() {
            return inactive(getProportion());
        }
        
        public boolean inactive(float proportion) {
            return proportion <= activeLimit;
        }
        
    }
    
}
