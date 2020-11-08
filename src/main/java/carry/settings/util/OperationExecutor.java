package carry.settings.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/9/1
 */
public interface OperationExecutor {
    
    void operate();
    
    static <T> OperationExecutor consumeExecutor(Consumer<T> consumer, Supplier<T> item) {
        return () -> consumer.accept(item.get());
    }
    
}
