package carry.ui.base;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Carry
 * @date 2020/8/29
 */
public abstract class BoundKeepingPopupWrapper extends BasePopupWrapper {
    protected final Point location;
    protected final Dimension size;
    
    protected final MouseAdapter movetiveAdapter;
    protected final ComponentAdapter sizeRemember;
    
    public BoundKeepingPopupWrapper(Point location, Dimension size, Predicate<MouseEvent> startMovingCondition) {
        this(location, size, startMovingCondition, e -> {
        });
    }
    
    public BoundKeepingPopupWrapper(Point location, Dimension size, Predicate<MouseEvent> startMovingCondition,
            Consumer<MouseEvent> onStop) {
        this.location = location;
        this.size = size;
        movetiveAdapter = new MovetiveAdapter() {
            @Override
            protected boolean startMoving(MouseEvent e) {
                return startMovingCondition.test(e);
            }
            
            @Override
            protected void moveBy(int x, int y) {
                location.x += x;
                location.y += y;
                jbPopup.setLocation(location);
            }
            
            @Override
            protected void onStop(MouseEvent e) {
                onStop.accept(e);
            }
        };
        sizeRemember = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calcSize(size);
                location.setLocation(jbPopup.getLocationOnScreen());
            }
        };
    }
    
    @Override
    public void resizeTo(Dimension size) {
        this.size.setSize(size);
        super.resizeTo(size);
    }
    
}
