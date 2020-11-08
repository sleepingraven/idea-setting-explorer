package carry.settings.util;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/9/2
 */
public class MethodAccessor implements MemberAccessor {
    final Supplier<?> supplier;
    final Method getter;
    final Method setter;
    
    public MethodAccessor(Supplier<?> supplier, Method getter, Method setter) {
        this.supplier = supplier;
        this.getter = getter;
        this.setter = setter;
        getter.setAccessible(true);
        setter.setAccessible(true);
    }
    
    public static MethodAccessor from(Supplier<?> supplier, Method getter, Method setter) {
        return new MethodAccessor(supplier, getter, setter);
    }
    
    @SneakyThrows
    @Override
    public Object get() {
        return getter.invoke(supplier.get());
    }
    
    @SneakyThrows
    @Override
    public void set(Object val) {
        setter.invoke(supplier.get(), val);
    }
    
}
