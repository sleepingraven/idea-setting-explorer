package carry.common.data.converters;

import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/9
 */
public class DimensionConverter extends BaseConverter<Dimension> {
    
    @Override
    public Dimension fromString(String value) {
        int i = value.indexOf(DEFAULT_DELIMITER);
        return new Dimension(intVal(value.substring(0, i)), intVal(value.substring(i + 1)));
    }
    
    @Override
    public String toString(Dimension dimension) {
        return join(DEFAULT_DELIMITER, dimension.width, dimension.height);
    }
    
}
