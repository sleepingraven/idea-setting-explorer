package carry.ui.menus.andtoolbars;

import admin.actions.AdminActionCenter;
import carry.ui.base.BaseToolbarWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class AdminToolbarWrapper extends BaseToolbarWrapper {
    private final AdminActionCenter adminActionCenter;
    
    public AdminToolbarWrapper(AdminActionCenter adminActionCenter) {
        super(new AnAction[] {
                adminActionCenter.populateXmlData, adminActionCenter.generateResource,
        }, "idea-setting-explorer-group:Admin-Toolbar");
        this.adminActionCenter = adminActionCenter;
        
        ((ActionToolbarImpl) myToolbar).setForceMinimumSize(true);
        // getComponent().setOpaque(false);
    }
    
}
