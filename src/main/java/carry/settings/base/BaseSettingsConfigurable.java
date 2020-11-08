package carry.settings.base;

import carry.settings.util.OperationExecutor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.Producer;
import lombok.Getter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * cannot use method reference of {@link BaseSettingsConfigurable#mySettingsComponent}
 * note that sub classes should have a test after been used
 *
 * @author Carry
 * @date 2020/9/1
 */
public abstract class BaseSettingsConfigurable<C extends BaseSettingsComponent> implements Configurable {
    @Getter
    @Nls(capitalization = Nls.Capitalization.Title)
    protected final String displayName;
    
    protected final Producer<C> creator;
    protected C mySettingsComponent;
    protected final Supplier<C> myCompSupplier = () -> mySettingsComponent;
    
    private final Queue<BooleanSupplier> whetherUnmodified = new ArrayDeque<>();
    private final Queue<OperationExecutor> applyingQueue = new ArrayDeque<>();
    private final Queue<OperationExecutor> resettingQueue = new ArrayDeque<>();
    
    public BaseSettingsConfigurable(String displayName, Producer<C> creator) {
        this.displayName = displayName;
        this.creator = creator;
    }
    
    @Override
    public final boolean isModified() {
        for (BooleanSupplier condition : whetherUnmodified) {
            if (!condition.getAsBoolean()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final void apply() throws ConfigurationException {
        applyingQueue.forEach(OperationExecutor::operate);
        onApplied();
    }
    
    protected void onApplied() {
    }
    
    @Override
    public final void reset() {
        resettingQueue.forEach(OperationExecutor::operate);
    }
    
    /* assistance */
    
    /**
     * addChecking
     */
    protected void addChecking(Supplier<?> first, Supplier<?> second) {
        addChecking(first, second, Object::equals);
    }
    
    protected <F, S> void addChecking(Supplier<F> first, Supplier<S> second, BiPredicate<F, S> comparator) {
        addChecking(() -> comparator.test(first.get(), second.get()));
    }
    
    protected void addChecking(BooleanSupplier condition) {
        whetherUnmodified.offer(condition);
    }
    
    /**
     * addStoring
     */
    protected <T> void addStoring(Consumer<T> apply, Supplier<T> item) {
        addStoring(OperationExecutor.consumeExecutor(apply, item));
    }
    
    protected void addStoring(OperationExecutor applier) {
        applyingQueue.offer(applier);
    }
    
    /**
     * addRestoring
     */
    protected <T> void addRestoring(Consumer<T> reset, Supplier<T> item) {
        addRestoring(OperationExecutor.consumeExecutor(reset, item));
    }
    
    protected void addRestoring(OperationExecutor resetter) {
        resettingQueue.offer(resetter);
    }
    
    /* components */
    
    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocused();
    }
    
    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = creator.produce();
        return mySettingsComponent.getPanel();
    }
    
    @Override
    public void disposeUIResources() {
        mySettingsComponent.dispose();
        mySettingsComponent = null;
    }
    
}
