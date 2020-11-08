package carry.ui.base;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Carry
 * @date 2020/8/29
 */
public abstract class MovetiveAdapter extends MouseAdapter {
    private final Point anchor = new Point();
    private boolean moving;
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (startMoving(e)) {
            anchor.setLocation(e.getX(), e.getY());
            moving = true;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (moving) {
            moveBy(e.getX() - anchor.x, e.getY() - anchor.y);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (moving) {
            moving = false;
            onStop(e);
        }
    }
    
    protected abstract boolean startMoving(MouseEvent e);
    
    protected abstract void moveBy(int x, int y);
    
    protected void onStop(MouseEvent e) {
    }
    
}
