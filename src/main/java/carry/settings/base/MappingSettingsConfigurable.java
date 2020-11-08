package carry.settings.base;

import carry.settings.util.FieldAccessor;
import carry.settings.util.MemberAccessor;
import carry.settings.util.MethodAccessor;
import com.intellij.util.Producer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * a common implementation of mapping fields in reflection
 * but if must access a field from method because of additional processes, it will lost compactness.
 *
 * @author Carry
 * @date 2020/9/1
 */
public abstract class MappingSettingsConfigurable<C extends BaseSettingsComponent> extends BaseSettingsConfigurable<C> {
    
    public MappingSettingsConfigurable(String displayName, Producer<C> creator) {
        super(displayName, creator);
    }
    
    /**
     * for field
     */
    protected void addReference(Field compField, Supplier<?> state, Field stateField) {
        addReference(myCompSupplier, compField, state, stateField);
    }
    
    protected void addReference(Supplier<?> comp, Field compField, Supplier<?> state, Field stateField) {
        addReference(FieldAccessor.from(comp, compField), FieldAccessor.from(state, stateField));
    }
    
    /**
     * for method
     */
    protected void addReference(Method compGetter, Method compSetter, Supplier<?> state, Method stateGetter,
            Method stateSetter) {
        addReference(myCompSupplier, compGetter, compSetter, state, stateGetter, stateSetter);
    }
    
    protected void addReference(Supplier<?> comp, Method compGetter, Method compSetter, Supplier<?> state,
            Method stateGetter, Method stateSetter) {
        addReference(MethodAccessor.from(comp, compGetter, compSetter),
                     MethodAccessor.from(state, stateGetter, stateSetter));
    }
    
    protected void addReference(MemberAccessor compAccessor, MemberAccessor stateAccessor) {
        addChecking(compAccessor::get, stateAccessor::get);
        addStoring(stateAccessor::set, compAccessor::get);
        addRestoring(compAccessor::set, stateAccessor::get);
    }
    
}
