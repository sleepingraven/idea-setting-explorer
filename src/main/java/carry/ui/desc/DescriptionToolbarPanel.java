package carry.ui.desc;

import carry.actions.ActionCenter;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.listeners.MyLafListener;
import carry.ui.base.HierarchyContainer;
import carry.ui.menus.andtoolbars.MainToolbarWrapper;
import carry.ui.menus.andtoolbars.SideToolbarWrapper;
import com.intellij.ui.components.JBPanel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.stream.Stream;

/**
 * @author Carry
 * @date 2020/8/25
 */
@Getter
public class DescriptionToolbarPanel extends JBPanel<DescriptionToolbarPanel> implements HierarchyContainer, ApplicationServiceAccessor {
    private final SideToolbarWrapper sideToolBar;
    private final MainToolbarWrapper mainToolBar;
    
    public DescriptionToolbarPanel(ActionCenter actionCenter) {
        super();
        sideToolBar = new SideToolbarWrapper(actionCenter);
        mainToolBar = new MainToolbarWrapper(actionCenter);
        
        setLayout(UI_UTIL.createGridBagLayout(new boolean[] { false }, new boolean[] { false, true, false }));
        add(sideToolBar.getComponent(), UI_UTIL.newDefaultGbc(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH));
        add(mainToolBar.getComponent(), UI_UTIL.newDefaultGbc(2, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH));
        
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (e.getComponent().isShowing()) {
                    if (MyLafListener.panelBackground != null) {
                        setBackground(MyLafListener.panelBackground);
                        sideToolBar.getComponent().setBackground(MyLafListener.panelBackground);
                        mainToolBar.getComponent().setBackground(MyLafListener.panelBackground);
                    }
                }
            }
        });
    }
    
    @Override
    public @NotNull Stream<JComponent> getAppearances() {
        return Stream.of(this, sideToolBar.getComponent(), mainToolBar.getComponent());
    }
    
}
