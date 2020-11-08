package carry.ui.list;

import carry.common.entity.Bag;
import carry.common.entity.BaseComposite;
import carry.common.entity.ConfigWrapper;
import carry.common.visitors.ApplicationServiceAccessor;
import com.intellij.icons.AllIcons.General;
import com.intellij.icons.AllIcons.Ide.Notification;
import com.intellij.ui.CellRendererPanel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI.Borders;

import javax.swing.*;
import java.awt.*;

/**
 * @author Carry
 * @date 2020/7/31
 */
public class ConfigsListCellRender extends DefaultListCellRenderer implements ApplicationServiceAccessor {
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        BaseComposite val = (BaseComposite) value;
        
        Icon icon;
        if (val instanceof ConfigWrapper) {
            icon = ((ConfigWrapper) val).isCollapsed() ? General.GearPlain : Notification.GearHover;
        } else if (val instanceof Bag) {
            icon = Notification.Gear;
        } else {
            icon = General.Settings;
        }
        setIcon(icon);
        setText(val.getDisplayName());
        setBorder(Borders.empty(0, 20 * (val.getLevel() - 1), 0, 8));
        
        // add tags
        JBPanel<JBPanel<?>> tagPanel = UI_UTIL.getTagPanel(val, getFont());
        
        CellRendererPanel panel = new CellRendererPanel();
        // add components
        panel.setLayout(new BorderLayout());
        panel.add(this, BorderLayout.CENTER);
        panel.add(tagPanel, BorderLayout.EAST);
        
        // panel style
        if (isSelected) {
            panel.setForcedBackground(getBackground());
        }
        panel.setBorder(Borders.empty(0, 8));
        
        return panel;
    }
    
}
