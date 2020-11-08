package carry.settings.template;

import carry.common.AppConstants;
import carry.settings.base.BaseSettingsConfigurable;
import carry.settings.base.MappingSettingsConfigurable;
import carry.settings.base.SupplyingSettingsConfigurable;
import carry.settings.base.WrappingSettingsConfigurable;
import com.intellij.openapi.components.ServiceManager;
import lombok.SneakyThrows;

import java.util.function.Supplier;

/**
 * Provides controller functionality for application settings.
 *
 * @author Carry
 * @date 2020/7/26
 */
public class TemplateSettingsConfigurable extends SupplyingSettingsConfigurable<TemplateSettingsComponent> {
    private static final String DISPLAY_NAME = AppConstants.PLUGIN_NAME + " TEMPLATE";
    private static final TemplateSettingsState SETTINGS_STATE = ServiceManager.getService(TemplateSettingsState.class);
    
    public TemplateSettingsConfigurable() {
        super(DISPLAY_NAME, TemplateSettingsComponent::new);
        initDependencies5();
    }
    
    /**
     * from {@link SupplyingSettingsConfigurable}
     */
    private void initDependencies5() {
        addReference(TemplateSettingsComponent::getUserNameText, TemplateSettingsComponent::setUserNameText,
                     SETTINGS_STATE::getUserId, SETTINGS_STATE::setUserId);
        addReference(TemplateSettingsComponent::getIdeaUserStatus, TemplateSettingsComponent::setIdeaUserStatus,
                     SETTINGS_STATE::isIdeaStatus, SETTINGS_STATE::setIdeaStatus);
    }
    
    /**
     * from {@link SupplyingSettingsConfigurable}
     */
    private void initDependencies4() {
        addChecking(TemplateSettingsComponent::getUserNameText, SETTINGS_STATE::getUserId);
        addChecking(TemplateSettingsComponent::getIdeaUserStatus, SETTINGS_STATE::isIdeaStatus);
        addStoring(SETTINGS_STATE::setUserId, TemplateSettingsComponent::getUserNameText);
        addStoring(SETTINGS_STATE::setIdeaStatus, TemplateSettingsComponent::getIdeaUserStatus);
        addRestoring(TemplateSettingsComponent::setUserNameText, SETTINGS_STATE::getUserId);
        addRestoring(TemplateSettingsComponent::setIdeaUserStatus, SETTINGS_STATE::isIdeaStatus);
    }
    
    /**
     * from class {@link WrappingSettingsConfigurable}
     */
    private void initDependencies3() {
        // addReference(mySettingsComponent::getUserNameText, mySettingsComponent::setUserNameText,
        //              SETTINGS_STATE::getUserId, SETTINGS_STATE::setUserId);
        // addReference(mySettingsComponent::getIdeaUserStatus, mySettingsComponent::setIdeaUserStatus,
        //              SETTINGS_STATE::isIdeaStatus, SETTINGS_STATE::setIdeaStatus);
    }
    
    /**
     * from class {@link MappingSettingsConfigurable}
     */
    @SneakyThrows
    private void initDependencies2() {
        // works with a bug if use field
        Class<TemplateSettingsComponent> compClazz = TemplateSettingsComponent.class;
        Class<TemplateSettingsState> stateClazz = TemplateSettingsState.class;
        Supplier<Object> state = () -> SETTINGS_STATE;
        // addReference(compClazz.getDeclaredField("myUserNameText"), state, stateClazz.getDeclaredField("userId"));
        // addReference(compClazz.getDeclaredField("myIdeaUserStatus"), state, stateClazz.getDeclaredField("ideaStatus"));
    }
    
    /**
     * from class {@link WrappingSettingsConfigurable}
     */
    private void initDependencies1() {
        addChecking(mySettingsComponent::getUserNameText, SETTINGS_STATE::getUserId);
        addChecking(mySettingsComponent::getIdeaUserStatus, SETTINGS_STATE::isIdeaStatus);
        addStoring(SETTINGS_STATE::setUserId, mySettingsComponent::getUserNameText);
        addStoring(SETTINGS_STATE::setIdeaStatus, mySettingsComponent::getIdeaUserStatus);
        addRestoring(mySettingsComponent::setUserNameText, SETTINGS_STATE::getUserId);
        addRestoring(mySettingsComponent::setIdeaUserStatus, SETTINGS_STATE::isIdeaStatus);
    }
    
    /**
     * from base class{@link BaseSettingsConfigurable}
     */
    @SuppressWarnings("Convert2MethodRef")
    private void initDependencies0() {
        addChecking(() -> mySettingsComponent.getUserNameText(), SETTINGS_STATE::getUserId);
        addChecking(() -> mySettingsComponent.getIdeaUserStatus(), SETTINGS_STATE::isIdeaStatus);
        addStoring(SETTINGS_STATE::setUserId, () -> mySettingsComponent.getUserNameText());
        addStoring(SETTINGS_STATE::setIdeaStatus, () -> mySettingsComponent.getIdeaUserStatus());
        addRestoring(newText -> mySettingsComponent.setUserNameText(newText), SETTINGS_STATE::getUserId);
        addRestoring(newStatus -> mySettingsComponent.setIdeaUserStatus(newStatus), SETTINGS_STATE::isIdeaStatus);
    }
    
}
