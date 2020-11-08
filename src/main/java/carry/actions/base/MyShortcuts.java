package carry.actions.base;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.ShortcutSet;
import com.intellij.openapi.project.DumbAwareAction;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * @author Carry
 * @date 2020/8/15
 */
public class MyShortcuts {
    static final CustomShortcutSet UP = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
    static final CustomShortcutSet DOWN = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
    static final CustomShortcutSet PAGE_UP = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));
    static final CustomShortcutSet PAGE_DOWN = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
    static final CustomShortcutSet LEFT = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
    static final CustomShortcutSet RIGHT = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
    
    public static final ShortcutSet SWITCH_SHORTCUT = new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
    
    final AnAction action;
    
    private MyShortcuts(ShortcutSet shortcut, JComponent component, Consumer<? super AnActionEvent> consumer,
            boolean once) {
        action = DumbAwareAction.create((e) -> {
            if (once) {
                MyShortcuts.this.action.unregisterCustomShortcutSet(component);
            }
            consumer.accept(e);
        });
        action.registerCustomShortcutSet(shortcut, component);
    }
    
    public static void register(ShortcutSet shortcut, JComponent component, Consumer<? super AnActionEvent> consumer,
            boolean once) {
        new MyShortcuts(shortcut, component, consumer, once);
    }
    
}
