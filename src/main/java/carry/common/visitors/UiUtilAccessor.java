package carry.common.visitors;

import carry.ui.AppUiUtil;
import com.intellij.openapi.components.ServiceManager;

/**
 * @author Carry
 * @date 2020/8/27
 */
public interface UiUtilAccessor {
    AppUiUtil UI_UTIL = ServiceManager.getService(AppUiUtil.class);
    
}
