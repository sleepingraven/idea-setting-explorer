package carry.ui.base;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.util.stream.Stream;

/**
 * @author Carry
 * @date 2020/8/26
 */
public interface HierarchyContainer {
    
    default void addMouseAdapter(MouseAdapter mouseAdapter) {
        getAppearances().forEach(component -> {
            component.addMouseListener(mouseAdapter);
            component.addMouseMotionListener(mouseAdapter);
        });
    }
    
    default void removeMouseAdapter(MouseAdapter mouseAdapter) {
        getAppearances().forEach(component -> {
            component.removeMouseListener(mouseAdapter);
            component.removeMouseMotionListener(mouseAdapter);
        });
    }
    
    @NotNull Stream<JComponent> getAppearances();
    
    default Stream<JComponent> piece(Stream<JComponent> appearances, HierarchyContainer... hierarchyContainers) {
        return Stream.concat(appearances, Stream.of(hierarchyContainers).flatMap(HierarchyContainer::getAppearances));
    }
    
}
