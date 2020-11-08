package carry.common.visitors;

import carry.common.data.AppPersistence;
import com.intellij.openapi.components.ServiceManager;

/**
 * @author Carry
 * @date 2020/8/27
 */
public interface PersistenceAccessor {
    AppPersistence PERSISTENCE = ServiceManager.getService(AppPersistence.class);
    
}
