package carry.common.visitors;

import carry.common.ConfigsUtil;
import com.intellij.openapi.components.ServiceManager;

/**
 * @author Carry
 * @date 2020/8/27
 */
public interface ConfigsUtilAccessor {
    ConfigsUtil CONFIGS_UTIL = ServiceManager.getService(ConfigsUtil.class);
    
}
