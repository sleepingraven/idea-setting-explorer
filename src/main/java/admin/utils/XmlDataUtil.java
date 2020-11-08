package admin.utils;

import carry.common.data.AppPersistence;
import carry.common.entity.ConfigWrapper;
import carry.common.visitors.ApplicationServiceAccessor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Carry
 * @date 2020/9/4
 */
public class XmlDataUtil implements ApplicationServiceAccessor {
    
    /**
     * @return true if field's value is null before populate
     */
    @SneakyThrows
    public static boolean populateXmlData(ConfigWrapper myRoot) {
        return set(myRoot);
    }
    
    @SneakyThrows
    public static void unPopulateXmlData() {
        set(null);
    }
    
    /**
     * still support static field.
     * now static fields cant be serialized.
     *
     * @return true if field's value is null before populate
     */
    private static boolean set(ConfigWrapper myRoot) throws NoSuchFieldException, IllegalAccessException {
        // get field
        Field compositesTemplate = AppPersistence.class.getDeclaredField("COMPOSITES_TEMPLATE");
        compositesTemplate.setAccessible(true);
        
        // set modifiers
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(compositesTemplate, compositesTemplate.getModifiers() & ~Modifier.FINAL);
        
        // set val
        boolean isNullVal = compositesTemplate.get(PERSISTENCE) == null;
        compositesTemplate.set(PERSISTENCE, myRoot);
        
        // reset modifiers
        modifiers.setInt(compositesTemplate, compositesTemplate.getModifiers() | Modifier.FINAL);
        return isNullVal;
    }
    
}
