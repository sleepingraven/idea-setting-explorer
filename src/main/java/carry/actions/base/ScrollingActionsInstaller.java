package carry.actions.base;

import com.intellij.util.ui.ScrollUtil;

import javax.swing.*;

/**
 * an experimental util
 *
 * @author Carry
 * @date 2020/8/30
 */
public class ScrollingActionsInstaller {
    
    public static void install(JComponent source, JComponent target) {
        JScrollBar h = ScrollUtil.findHorizontalScrollBar(source);
        JScrollBar v = ScrollUtil.findVerticalScrollBar(source);
        MyShortcuts.register(MyShortcuts.UP, target, (e) -> scrollBy(v, -v.getUnitIncrement(-1)), false);
        MyShortcuts.register(MyShortcuts.DOWN, target, (e) -> scrollBy(v, v.getUnitIncrement(1)), false);
        MyShortcuts.register(MyShortcuts.LEFT, target, (e) -> scrollBy(h, -h.getUnitIncrement(-1)), false);
        MyShortcuts.register(MyShortcuts.RIGHT, target, (e) -> scrollBy(h, h.getUnitIncrement(1)), false);
        MyShortcuts.register(MyShortcuts.PAGE_UP, target, (e) -> scrollBy(v, -v.getBlockIncrement(-1)), false);
        MyShortcuts.register(MyShortcuts.PAGE_DOWN, target, (e) -> scrollBy(v, v.getBlockIncrement(1)), false);
    }
    
    public static void scrollBy(JScrollBar scrollBar, int incr) {
        scrollBar.setValue(scrollBar.getValue() + incr);
    }
    
}
