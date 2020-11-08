package carry.common.data.converters;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carry
 * @date 2020/8/9
 */
public class ListWithStringConverter extends Converter<List<String>> {
    // todo issue when tag contains ","
    // todo issue when miss " " in xml
    static final String DELIMITER = ", ";
    
    @Nullable
    @Override
    public List<String> fromString(@NotNull String value) {
        // [a, b]
        return value.length() <= 2 ? new ArrayList<>() :
               StringUtil.split(value.substring(1, value.length() - 1), DELIMITER);
    }
    
    @Override
    public @Nullable String toString(@NotNull List<String> list) {
        // return StringUtil.join(list, DELIMITER);
        return list.toString();
    }
    
}
