package carry.ui.base;

import carry.common.visitors.ApplicationServiceAccessor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.TitlePanel;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.util.ui.JBInsets;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.lang.reflect.Field;

/**
 * @author Carry
 * @date 2020/8/28
 */
public abstract class BasePopupWrapper implements Disposable, ApplicationServiceAccessor {
    public static Field myAdComponent;
    
    static {
        initReflections();
    }
    
    @SneakyThrows
    private static void initReflections() {
        myAdComponent = AbstractPopup.class.getDeclaredField("myAdComponent");
        myAdComponent.setAccessible(true);
    }
    
    /**/
    
    protected JBPopup jbPopup;
    
    public void create() {
        doCreate();
        // make sure actions reference to this triggers by calling dispose()
        Disposer.register(jbPopup, this);
    }
    
    /**
     * create logic
     */
    public abstract void doCreate();
    
    /**
     * default show logic
     */
    public abstract void show();
    
    public void toFront() {
        UI_UTIL.popupToFront(jbPopup);
    }
    
    public void toBack() {
        UI_UTIL.popupToBack(jbPopup);
    }
    
    /* overridden and expansion methods */
    
    @SneakyThrows
    public void calcSize(Dimension size) {
        // header not considered now
        if (myAdComponent.get(jbPopup) != null) {
            throw new UnsupportedOperationException("Calc size with ad should implement by sub classes.");
        }
        Insets borderInsets = jbPopup.getContent().getBorder().getBorderInsets(null);
        size.setSize(jbPopup.getContent().getSize());
        JBInsets.removeFrom(size, borderInsets);
    }
    
    public Dimension calcSize() {
        Dimension size = new Dimension();
        calcSize(size);
        return size;
    }
    
    public void resizeTo(Dimension size) {
        jbPopup.setSize(size);
    }
    
    /**
     * calls after popup created
     */
    protected TitlePanel getHeader() {
        Component topPanel = ((BorderLayout) jbPopup.getContent().getLayout()).getLayoutComponent(BorderLayout.NORTH);
        return (TitlePanel) ((JPanel) topPanel).getComponent(0);
    }
    
    /* wrapped methods */
    
    public JComponent getContent() {
        return jbPopup.getContent();
    }
    
    public Point getLocationOnScreen() {
        return jbPopup.getLocationOnScreen();
    }
    
    public void closeOk(InputEvent e) {
        jbPopup.closeOk(e);
    }
    
    public void cancel() {
        jbPopup.cancel();
    }
    
    public boolean isDisposed() {
        return jbPopup.isDisposed();
    }
    
    @Override
    public void dispose() {
        jbPopup.dispose();
    }
    
}
