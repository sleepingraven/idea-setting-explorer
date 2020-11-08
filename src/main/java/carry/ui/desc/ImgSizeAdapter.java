package carry.ui.desc;

import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTMLDocument.RunElement;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Carry
 * @date 2020/8/23
 */
public class ImgSizeAdapter {
    /* reflections */
    
    private static Method writeLock;
    private static Method writeUnlock;
    private static Method updateImageSize;
    private static Method refreshImage;
    private static Field imageObserver;
    
    private static Field sIsInc;
    
    static {
        initReflections();
    }
    
    @SneakyThrows
    private static void initReflections() {
        writeLock = AbstractDocument.class.getDeclaredMethod("writeLock");
        writeUnlock = AbstractDocument.class.getDeclaredMethod("writeUnlock");
        updateImageSize = ImageView.class.getDeclaredMethod("updateImageSize");
        refreshImage = ImageView.class.getDeclaredMethod("refreshImage");
        imageObserver = ImageView.class.getDeclaredField("imageObserver");
        sIsInc = ImageView.class.getDeclaredField("sIsInc");
        writeLock.setAccessible(true);
        writeUnlock.setAccessible(true);
        updateImageSize.setAccessible(true);
        refreshImage.setAccessible(true);
        imageObserver.setAccessible(true);
        sIsInc.setAccessible(true);
    }
    
    /* fields */
    
    private final Map<ImageView, Dimension> imageViews = new HashMap<>();
    private final Queue<ImageView> sizePatchQueue = new ArrayDeque<>();
    
    private final JScrollPane jScrollPane;
    private final JTextComponent textComponent;
    private int width;
    boolean refresh;
    
    @SneakyThrows
    public ImgSizeAdapter(JTextComponent textComponent, JScrollPane jScrollPane) {
        this.textComponent = textComponent;
        this.jScrollPane = jScrollPane;
        
        // ?
        sIsInc.set(null, true);
        
        textComponent.addPropertyChangeListener("page", e -> updateHScrollBarPolicy());
    }
    
    void resetContainerSize() {
        width = textComponent.getVisibleRect().width;
        Border border = textComponent.getBorder();
        if (border != null) {
            Insets borderInsets = border.getBorderInsets(textComponent);
            if (borderInsets != null) {
                width -= borderInsets.left + borderInsets.right;
            }
        }
        width -= 4;
    }
    
    void adjust(RunElement img) throws InvocationTargetException, IllegalAccessException {
        adjust(img, false, null);
    }
    
    boolean adjust(RunElement img, boolean requireLock, Dimension srcSize)
            throws InvocationTargetException, IllegalAccessException {
        try {
            Document document = textComponent.getDocument();
            // compare width
            String w = (String) img.getAttribute(Attribute.WIDTH);
            boolean remove = false;
            if (w != null) {
                if (String.valueOf(width).equals(w)) {
                    return false;
                } else {
                    remove = true;
                }
            }
            
            // lock
            if (requireLock) {
                writeLock.invoke(document);
            }
            // remove
            if (remove) {
                img.removeAttribute(Attribute.WIDTH);
                if (srcSize != null) {
                    img.removeAttribute(Attribute.HEIGHT);
                }
            }
            // add
            img.addAttribute(Attribute.WIDTH, String.valueOf(width));
            if (srcSize != null) {
                double r = (double) srcSize.height / srcSize.width;
                int height = (int) Math.round(width * r);
                img.addAttribute(Attribute.HEIGHT, String.valueOf(height));
            }
            // unlock
            if (requireLock) {
                writeUnlock.invoke(document);
            }
            return true;
        } catch (Error e) {
            // sometimes throws a StateInvariantError when switch between view mode suddenly
            e.printStackTrace();
            // now if throws, skip updating and continue other image views
            return false;
        }
    }
    
    /**
     * adjust all showing imgs
     * <p>
     * Support auto updating maximum size (hide scroll bar), which comes from debugging at
     * {@link JEditorPane#getScrollableTracksViewportWidth()} and find {@link javax.swing.text.BoxView}'s
     * field "majorReqValid" and "minorReqValid", and then
     * {@link javax.swing.text.BoxView#preferenceChanged(View, boolean, boolean)} discovered.
     * (failed by only hiding the scroll bar temporarily)
     */
    void adjustAll() {
        try {
            while (!sizePatchQueue.isEmpty()) {
                ImageView imageView = sizePatchQueue.poll();
                int width = imageView.getImage().getWidth((ImageObserver) imageObserver.get(imageView));
                int height = imageView.getImage().getHeight((ImageObserver) imageObserver.get(imageView));
                imageViews.get(imageView).setSize(width, height);
            }
            
            for (ImageView imageView : imageViews.keySet()) {
                if (adjust((RunElement) imageView.getElement(), true, imageViews.get(imageView))) {
                    if (refresh) {
                        refreshImage.invoke(imageView);
                    } else {
                        updateImageSize.invoke(imageView);
                        imageView.preferenceChanged(imageView, true, true);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (refresh) {
                // textComponent.setVisible(true);
                // Now can't call updateScrollBarPolicy() because min has not been updated.
                // For example, amount == 200, min == 1000. But size matches amount.
                // boolean eqs = jScrollPane.getHorizontalScrollBar().getVisibleAmount() == textComponent.getSize().width;
                
                // if (jScrollPane.getHorizontalScrollBar().getVisibleAmount() > textComponent.getSize().width) {
                //     setHVisible(true);
                // }
                // if (jScrollPane.getVerticalScrollBar().getVisibleAmount() > textComponent.getSize().height) {
                //     setVVisible(true);
                // }
                
                // or learn deciding by each time
                ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
                service.schedule(() -> {
                    setHVisible(true);
                    setVVisible(true);
                    adjustAll();
                    textComponent.revalidate();
                    textComponent.repaint();
                }, 200, TimeUnit.MILLISECONDS);
                refresh = false;
            } else {
                updateHScrollBarPolicy();
            }
        }
    }
    
    /**
     * calls before all resized
     */
    void fireReadjust() {
        // textComponent.setVisible(false);
        setHVisible(false);
        setVVisible(false);
        refresh = true;
    }
    
    private void updateHScrollBarPolicy() {
        // equals as normal
        boolean visible =
                jScrollPane.getHorizontalScrollBar().getVisibleAmount() < textComponent.getMinimumSize().width;
        setHVisible(visible);
    }
    
    private void setHVisible(boolean visible) {
        int policy = visible ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED :
                     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
        if (jScrollPane.getHorizontalScrollBarPolicy() != policy) {
            jScrollPane.setHorizontalScrollBarPolicy(policy);
        }
    }
    
    private void setVVisible(boolean visible) {
        int policy = visible ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED :
                     ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
        if (jScrollPane.getVerticalScrollBarPolicy() != policy) {
            jScrollPane.setVerticalScrollBarPolicy(policy);
        }
    }
    
    void add(ImageView imageView) {
        try {
            imageViews.put(imageView, new Dimension());
            adjust((RunElement) imageView.getElement());
            sizePatchQueue.offer(imageView);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    void clear() {
        imageViews.clear();
        sizePatchQueue.clear();
    }
    
}
