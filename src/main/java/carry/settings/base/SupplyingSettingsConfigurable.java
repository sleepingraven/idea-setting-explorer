package carry.settings.base;

import com.intellij.util.Producer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/9/2
 */
public abstract class SupplyingSettingsConfigurable<C extends BaseSettingsComponent> extends BaseSettingsConfigurable<C> {
    
    public SupplyingSettingsConfigurable(String displayName, Producer<C> creator) {
        super(displayName, creator);
    }
    
    /**
     * addChecking
     */
    protected void addChecking(CompGetterAdapter<C, ?> first, Supplier<?> second) {
        addChecking(first, myCompSupplier, second);
    }
    
    protected <F> void addChecking(Function<F, ?> item, Supplier<F> first, Supplier<?> second) {
        addChecking(() -> item.apply(first.get()), second);
    }
    
    /**
     * addStoring
     */
    protected <T> void addStoring(Consumer<T> apply, CompGetterAdapter<C, T> compApply) {
        addStoring(apply, compApply, myCompSupplier);
    }
    
    protected <T, S> void addStoring(Consumer<T> apply, Function<S, T> item, Supplier<S> second) {
        addStoring(() -> apply.accept(item.apply(second.get())));
    }
    
    /**
     * addRestoring
     */
    protected <T> void addRestoring(CompSetterAdapter<C, T> compReset, Supplier<T> item) {
        addRestoring(myCompSupplier, compReset, item);
    }
    
    protected <F, S> void addRestoring(Supplier<F> first, BiConsumer<F, S> reset, Supplier<S> second) {
        addRestoring(() -> reset.accept(first.get(), second.get()));
    }
    
    protected <T> void addReference(CompGetterAdapter<C, T> compGetter, CompSetterAdapter<C, T> compSetter,
            Supplier<T> stateGetter, Consumer<T> stateSetter) {
        addChecking(compGetter, stateGetter);
        addStoring(stateSetter, compGetter);
        addRestoring(compSetter, stateGetter);
    }
    
    public interface CompGetterAdapter<C, T> extends Function<C, T> {
    }
    
    public interface CompSetterAdapter<C, T> extends BiConsumer<C, T> {
    }
    
}
