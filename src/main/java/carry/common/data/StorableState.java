package carry.common.data;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T>
 *         the state class extends this class
 *
 * @author Carry
 * @date 2020/9/1
 */
public abstract class StorableState<T extends StorableState<T>> implements PersistentStateComponent<T> {
    
    @Override
    public T getState() {
        return (T) this;
    }
    
    @Override
    public void loadState(@NotNull T state) {
        XmlSerializerUtil.copyBean(state, this);
    }
    
}
