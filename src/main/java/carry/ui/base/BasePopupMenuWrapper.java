package carry.ui.base;

import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * @author Carry
 * @date 2020/9/3
 */
public abstract class BasePopupMenuWrapper {
    private static Method containsAction;
    
    static {
        initReflection();
    }
    
    @SneakyThrows
    protected static void initReflection() {
        containsAction = DefaultActionGroup.class.getDeclaredMethod("containsAction", AnAction.class);
        containsAction.setAccessible(true);
    }
    
    protected final DefaultActionGroup myActionGroup;
    
    public BasePopupMenuWrapper(AnAction[] anActions) {
        this.myActionGroup = new DefaultActionGroup(anActions);
    }
    
    protected abstract ActionPopupMenu createPopupMenu();
    
    public JPopupMenu getComponent() {
        return createPopupMenu().getComponent();
    }
    
    @SneakyThrows
    public boolean containsAction(@NotNull AnAction action) {
        return (boolean) containsAction.invoke(myActionGroup, action);
    }
    
}
