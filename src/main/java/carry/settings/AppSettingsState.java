package carry.settings;

import carry.common.AppConstants;
import carry.common.data.StorableState;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Supports storing the application settings in a persistent way.
 * The State and Storage annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 *
 * @author Carry
 * @date 2020/7/26
 */
@NoArgsConstructor
@Getter
@Setter
@State(name = AppConstants.SETTING_STATE_ID, storages = { @Storage(AppConstants.SETTING_STATE_STORAGE) })
public class AppSettingsState extends StorableState<AppSettingsState> {
    private boolean refreshOnStartup;
    @Getter
    private int separatorTopPosition;
    
}
