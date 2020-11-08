package carry.listeners;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author Carry
 * @date 2020/8/25
 */
public class MyLafListener implements LafManagerListener {
    public static Color textFieldBackground;
    public static Color panelBackground;
    
    @Override
    public void lookAndFeelChanged(@NotNull LafManager source) {
        textFieldBackground = UIUtil.getTextFieldBackground();
        panelBackground = UIUtil.getPanelBackground();
    }
    
}
