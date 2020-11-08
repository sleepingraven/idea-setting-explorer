package carry.common.data.converters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/27
 */
public class RectangleConverter extends BaseConverter<Rectangle> {
    
    @Override
    public @Nullable Rectangle fromString(@NotNull String value) {
        String[] split = value.split(DEFAULT_DELIMITER);
        return new Rectangle(intVal(split[0]), intVal(split[1]), intVal(split[2]), intVal(split[3]));
    }
    
    @Override
    public @Nullable String toString(@NotNull Rectangle rectangle) {
        return join(DEFAULT_DELIMITER, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
}
