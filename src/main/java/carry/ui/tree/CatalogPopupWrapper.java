package carry.ui.tree;

import admin.actions.AdminActionCenter;
import carry.actions.base.MyShortcuts;
import carry.actions.base.TreeActionsInstaller;
import carry.common.AppConstants;
import carry.ui.base.BasePopupWrapper;
import carry.ui.menus.andtoolbars.AdminToolbarWrapper;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.TitlePanel;
import com.intellij.ui.popup.AbstractPopup;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Carry
 * @date 2020/8/28
 */
public class CatalogPopupWrapper extends BasePopupWrapper {
    private final ConfigsTreeController controller;
    private final CatalogPanel catalogPanel;
    
    private final Dimension size = PERSISTENCE.getCatalogPopupSize();
    
    private final MyPopupListener popupListener = new MyPopupListener();
    
    public CatalogPopupWrapper(ConfigsTreeController controller, CatalogPanel catalogPanel) {
        this.controller = controller;
        this.catalogPanel = catalogPanel;
    }
    
    @Override
    public void doCreate() {
        // if reuse the builder, it will be a npe on the second time
        ComponentPopupBuilder builder = JBPopupFactory.getInstance()
                                                      .createComponentPopupBuilder(catalogPanel, catalogPanel.tree)
                                                      .setCancelOnClickOutside(true)
                                                      .setCancelOnOtherWindowOpen(false)
                                                      .setCancelOnWindowDeactivation(false)
                                                      .setCancelKeyEnabled(false)
                                                      .setAdText("Press " + KeymapUtil.getShortcutsText(
                                                              MyShortcuts.SWITCH_SHORTCUT.getShortcuts()) +
                                                                 " to switch component")
                                                      .setTitle(AppConstants.SEARCH_TITLE)
                                                      .setMovable(true)
                                                      .setResizable(true)
                                                      .setRequestFocus(true)
                                                      .setMayBeParent(true);
        if (AppConstants.ADMIN_MODE) {
            AdminToolbarWrapper adminToolbar = new AdminToolbarWrapper(new AdminActionCenter());
            builder.setSettingButtons(adminToolbar.getComponent());
        }
        jbPopup = builder.createPopup();
        JComponent content = jbPopup.getContent();
        TitlePanel header = getHeader();
        
        // apply size
        jbPopup.setSize(size);
        jbPopup.addListener(popupListener);
        
        // apply shortcuts
        MyShortcuts.register(CommonShortcuts.ESCAPE, content, (e) -> jbPopup.cancel(), true);
        MyShortcuts.register(MyShortcuts.SWITCH_SHORTCUT, content, (e) -> {
            JComponent toFocus;
            if (catalogPanel.jbTextField.hasFocus()) {
                toFocus = catalogPanel.tree;
            } else {
                toFocus = catalogPanel.jbTextField;
            }
            IdeFocusManager.getInstance(controller.getProject()).requestFocus(toFocus, true);
        }, false);
        TreeActionsInstaller.install(catalogPanel.tree, content, true, false, false);
        
        // moving sync
        MouseAdapter movementListener = new MouseAdapter() {
            final Point anchor = new Point();
            
            @Override
            public void mousePressed(MouseEvent e) {
                anchor.setLocation(e.getXOnScreen(), e.getYOnScreen());
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                controller.getDescriptionPopup().moveBy(e.getXOnScreen() - anchor.x, e.getYOnScreen() - anchor.y);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                controller.getDescriptionPopup().stopMoving();
            }
        };
        header.addMouseListener(movementListener);
        header.addMouseMotionListener(movementListener);
        // move desc popup on jbPopup resize
        ((AbstractPopup) jbPopup).addResizeListener(() -> {
            int x = jbPopup.getLocationOnScreen().x + jbPopup.getContent().getSize().width;
            int y = catalogPanel.tree.getLocationOnScreen().y +
                    catalogPanel.tree.getPathBounds(catalogPanel.tree.getLeadSelectionPath()).y;
            controller.getDescriptionPopup().moveTo(x, y);
        }, jbPopup);
    }
    
    @Override
    public void show() {
        jbPopup.showInFocusCenter();
    }
    
    private void reload() {
    }
    
    /**/
    
    private class MyPopupListener implements JBPopupListener {
        int headerH, footerH;
        
        @Override
        public void beforeShown(@NotNull LightweightWindowEvent event) {
            // cannot effect outside when resized to minimum size
            // it dose not show in center if remove the code outside
            jbPopup.setSize(size);
            // h:30
            Dimension h = ((AbstractPopup) jbPopup).getHeaderPreferredSize();
            // h:20
            Dimension f = ((AbstractPopup) jbPopup).getFooterPreferredSize();
            // h:30
            Dimension tf = catalogPanel.jbTextField.getPreferredSize();
            headerH = h.height;
            footerH = f.height;
            jbPopup.setMinimumSize(new Dimension(tf.width, headerH + tf.height + footerH));
            
            int x = jbPopup.getLocationOnScreen().x + jbPopup.getContent().getSize().width;
            int y = jbPopup.getLocationOnScreen().y + catalogPanel.getLocation().y +
                    catalogPanel.jScrollPane.getLocation().y;
            controller.getDescriptionPopup().moveTo(x, y);
            
            // to make sure be created after jbPopup
            controller.getDescriptionPopup().create();
        }
        
        @Override
        public void onClosed(@NotNull LightweightWindowEvent event) {
            calcSize(size);
            
            catalogPanel.clearStatusBar(controller.getProject());
            controller.getDescriptionPopup().cancel();
            catalogPanel.lastSelectedRow = catalogPanel.tree.getSelectionModel().getLeadSelectionRow();
        }
        
        void calcSize(Dimension size) {
            Dimension size1 = ((AbstractPopup) jbPopup).getComponent().getSize();
            // w+2, h+52
            Dimension size2 = jbPopup.getContent().getSize();
            
            int width = size1.width;
            int height = size1.height + headerH - 1;
            size.setSize(width, height);
        }
        
    }
    
    @Override
    public void calcSize(Dimension size) {
        popupListener.calcSize(size);
    }
    
}
