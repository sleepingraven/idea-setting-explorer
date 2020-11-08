package carry.common.data.converters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/28
 */
public class PointConverter extends BaseConverter<Point> {
    
    @Override
    public @Nullable Point fromString(@NotNull String value) {
        int i = value.indexOf(DEFAULT_DELIMITER);
        return new Point(intVal(value.substring(0, i)), intVal(value.substring(i + 1)));
    }
    
    @Override
    public @Nullable String toString(@NotNull Point point) {
        return join(DEFAULT_DELIMITER, point.x, point.y);
    }
    
}
