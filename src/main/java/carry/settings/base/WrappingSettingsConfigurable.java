package carry.settings.base;

import carry.settings.util.MyWrapperProxy;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.Producer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Support method reference of {@link WrappingSettingsConfigurable#mySettingsComponent}.
 * <p>
 * There is a new problem that mySettingsComponent only used as a wrapper with a lot of useless fields but never be
 * GC
 * <p>
 * known bugs:<br/>
 * toString throws a exception
 *
 * @author Carry
 * @date 2020/9/1
 */
public abstract class WrappingSettingsConfigurable<C extends BaseSettingsComponent> extends BaseSettingsConfigurable<C> {
    private final MyWrapperProxy<C> proxy;
    
    public WrappingSettingsConfigurable(String displayName, Producer<C> creator) {
        super(displayName, creator);
        Class<C> cClazz = (Class<C>) getActualTypeArgument(0);
        this.proxy = new MyWrapperProxy<>(cClazz);
        // never change
        mySettingsComponent = proxy.createProxy();
    }
    
    private Type getActualTypeArgument(int i) {
        Class<?> clazz = getClass();
        Class<?> superclass;
        while (!WrappingSettingsConfigurable.class.equals(superclass = clazz.getSuperclass())) {
            clazz = superclass;
        }
        Type[] actualTypeArguments = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
        return actualTypeArguments[i];
    }
    
    /**
     * isn't effect
     */
    @SneakyThrows
    private void fix() {
        Field mySettingsComponent = BaseSettingsConfigurable.class.getDeclaredField("mySettingsComponent");
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(mySettingsComponent, mySettingsComponent.getModifiers() | Modifier.FINAL);
        super.mySettingsComponent = proxy.createProxy();
    }
    
    protected <T> void addReference(Supplier<T> compGetter, Consumer<T> compSetter, Supplier<T> stateGetter,
            Consumer<T> stateSetter) {
        addChecking(compGetter, stateGetter);
        addStoring(stateSetter, compGetter);
        addRestoring(compSetter, stateGetter);
    }
    
    /* components */
    
    @Nullable
    @Override
    public JComponent createComponent() {
        proxy.setWrapped(creator.produce());
        return mySettingsComponent.getPanel();
    }
    
    @Override
    public void disposeUIResources() {
        Disposer.dispose(mySettingsComponent);
        proxy.setWrapped(null);
    }
    
}
