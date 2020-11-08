package carry.ui.desc;

import carry.actions.base.MyShortcuts;
import carry.common.AppConstants;
import carry.common.entity.BaseComposite;
import carry.ui.base.BasePopupWrapper;
import carry.ui.base.BoundKeepingPopupWrapper;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.actionSystem.impl.ActionMenuItem;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.ScreenUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/8/19
 */
public class DescriptionPopupWrapper extends BoundKeepingPopupWrapper {
    private final ConfigsTreeController controller;
    private final Supplier<BasePopupWrapper> parent;
    private final JComponent preferableFocusComponent;
    private final DescriptionPanel descriptionPanel;
    
    private final Point relative;
    
    public boolean locationPinned = PERSISTENCE.pinLocationNonViewMode;
    
    public DescriptionPopupWrapper(ConfigsTreeController controller, Supplier<BasePopupWrapper> parent,
            JComponent preferableFocusComponent, DescriptionPanel descriptionPanel) {
        // non-static to keep difference sizes between multiple-size screens
        super(PERSISTENCE.getDescriptionPopupLocation(),
              new Dimension(AppConstants.DESCRIPTION_POPUP_WIDTH, AppConstants.DESCRIPTION_POPUP_HEIGHT), e -> {
                    if (UIUtil.isControlKeyDown(e)) {
                        controller.getDescriptionPopup().descriptionPanel.setSelectable(false);
                        return true;
                    }
                    return false;
                }, e -> {
                    controller.getDescriptionPopup().descriptionPanel.setSelectable(true);
                });
        this.controller = controller;
        this.parent = parent;
        this.preferableFocusComponent = preferableFocusComponent;
        this.descriptionPanel = descriptionPanel;
        relative = new Point();
    }
    
    /* popup's visibility */
    
    /**
     * setLocateWithinScreenBounds should be false, to set true to myMouseOutCanceller.myEverEntered, see {@link
     * com.intellij.ui.popup.AbstractPopup#show(Component, int, int, boolean)}
     */
    @Override
    public void doCreate() {
        // disable cancel key to support popup menu
        ComponentPopupBuilder builder = JBPopupFactory.getInstance()
                                                      .createComponentPopupBuilder(descriptionPanel,
                                                                                   preferableFocusComponent)
                                                      .setCancelOnClickOutside(false)
                                                      .setCancelOnOtherWindowOpen(false)
                                                      .setCancelOnWindowDeactivation(false)
                                                      .setCancelKeyEnabled(false)
                                                      .setMovable(true)
                                                      .setResizable(true)
                                                      .setRequestFocus(true)
                                                      .setMayBeParent(true)
                                                      .setLocateWithinScreenBounds(false)
                                                      // ?
                                                      .setFocusOwners(new Component[] { preferableFocusComponent })
                                                      .setCancelOnMouseOutCallback(e -> {
                                                          if (e.getButton() == MouseEvent.NOBUTTON) {
                                                              return false;
                                                          }
                                                          if (new Rectangle(
                                                                  parent.get().getContent().getLocationOnScreen(),
                                                                  parent.get().getContent().getSize()).contains(
                                                                  e.getLocationOnScreen())) {
                                                              return false;
                                                          }
                                                          if (e.getSource() instanceof ActionMenuItem &&
                                                              controller.getDescriptionPanel()
                                                                        .getMainPopupMenu()
                                                                        .containsAction(
                                                                                ((ActionMenuItem) e.getSource()).getAnAction())) {
                                                              return false;
                                                          }
                                                          return true;
                                                      });
        jbPopup = builder.createPopup();
        
        JComponent content = jbPopup.getContent();
        jbPopup.addListener(new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                parent.get().cancel();
            }
        });
        MyShortcuts.register(CommonShortcuts.ESCAPE, content, (e) -> jbPopup.cancel(), true);
        MyShortcuts.register(MyShortcuts.SWITCH_SHORTCUT, content,
                             (e) -> IdeFocusManager.getInstance(controller.getProject())
                                                   .requestFocus(preferableFocusComponent, true), false);
        
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
        jbPopup.setLocation(new Point(location.x - 1, location.y));
        jbPopup.setSize(size);
        
        if (!jbPopup.isVisible()) {
            // cause y on screen increase sometimes
            // jbPopup.show(WindowManager.getInstance().findVisibleFrame());
            jbPopup.show(new RelativePoint(location));
        }
    }
    
    /* popup's bounds */
    
    public void moveToWith(int x, int y, BaseComposite composite) {
        descriptionPanel.setComposite(composite);
        moveTo(x, y);
    }
    
    public void moveTo(int x, int y) {
        if (locationPinned) {
            return;
        }
        location.setLocation(--x, ++y);
        if (jbPopup != null) {
            Rectangle bounds = new Rectangle(location, size);
            Rectangle frameBounds = ScreenUtil.getScreenRectangle(descriptionPanel);
            ScreenUtil.moveToFit(bounds, frameBounds, null);
            location.setLocation(bounds.getLocation());
            jbPopup.setLocation(location);
        }
    }
    
    public void moveBy(int incrX, int incrY) {
        if (locationPinned) {
            return;
        }
        relative.setLocation(incrX, incrY);
        if (jbPopup != null) {
            jbPopup.setLocation(new Point(location.x + relative.x, location.y + relative.y));
        }
    }
    
    public void stopMoving() {
        if (locationPinned) {
            return;
        }
        location.x += relative.x;
        location.y += relative.y;
        relative.setLocation(0, 0);
    }
    
}
