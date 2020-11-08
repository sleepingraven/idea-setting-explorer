package carry.common.data.converters;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.Converter;

/**
 * @author Carry
 * @date 2020/8/27
 */
public abstract class BaseConverter<T> extends Converter<T> {
    static final String DEFAULT_DELIMITER = ", ";
    
    protected int intVal(String value) {
        return Integer.parseInt(value.trim());
    }
    
    protected String join(String separator, String... strings) {
        return StringUtil.join(strings, separator);
    }
    
    protected String join(String separator, int... strings) {
        return StringUtil.join(strings, separator);
    }
    
}
