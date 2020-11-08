package carry.settings.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/9/1
 */
public class FieldAccessor implements MemberAccessor {
    final Supplier<?> supplier;
    final Field field;
    
    public FieldAccessor(Supplier<?> supplier, Field field) {
        this.supplier = supplier;
        this.field = field;
        field.setAccessible(true);
    }
    
    public static FieldAccessor from(Supplier<?> supplier, Field field) {
        return new FieldAccessor(supplier, field);
    }
    
    @SneakyThrows
    @Override
    public Object get() {
        return field.get(supplier.get());
    }
    
    @SneakyThrows
    @Override
    public void set(Object val) {
        field.set(supplier.get(), val);
    }
    
}
