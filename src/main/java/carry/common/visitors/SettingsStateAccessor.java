package carry.common.visitors;

import carry.settings.AppSettingsState;
import com.intellij.openapi.components.ServiceManager;

/**
 * @author Carry
 * @date 2020/9/1
 */
public interface SettingsStateAccessor {
    AppSettingsState SETTINGS_STATE = ServiceManager.getService(AppSettingsState.class);
    
}
