package carry.settings;

import carry.common.AppConstants;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.settings.base.SupplyingSettingsConfigurable;
import carry.ui.tree.ConfigsTreeCellRender.MyTitledSeparator;

/**
 * Provides controller functionality for application settings.
 *
 * @author Carry
 * @date 2020/7/26
 */
public class AppSettingsConfigurable extends SupplyingSettingsConfigurable<AppSettingsComponent> implements ApplicationServiceAccessor {
    
    public AppSettingsConfigurable() {
        super(AppConstants.PLUGIN_NAME, AppSettingsComponent::new);
        addReference(AppSettingsComponent::getRefreshOnStartup, AppSettingsComponent::setRefreshOnStartup,
                     SETTINGS_STATE::isRefreshOnStartup, SETTINGS_STATE::setRefreshOnStartup);
        addReference(AppSettingsComponent::getSeparatorAdjustValue, AppSettingsComponent::setSeparatorAdjustValue,
                     SETTINGS_STATE::getSeparatorTopPosition, SETTINGS_STATE::setSeparatorTopPosition);
    }
    
    @Override
    protected void onApplied() {
        MyTitledSeparator.fireAdjustAll();
    }
    
}
