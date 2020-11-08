package demo.ui.components;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.SearchTopHitProvider;
import com.intellij.ui.RelativeFont;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.TextIcon;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.util.ui.JBUI.CurrentTheme.BigPopup;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Carry
 * @date 2020/7/28
 */
public class TextFieldDemo extends JPanel {
    private final JPanel control;
    private final JPanel center;
    private final List<JTextField> fields;
    
    public TextFieldDemo() {
        super(new BorderLayout(0, 0));
        this.control = new JPanel(new HorizontalLayout(0));
        this.center = new JPanel(new GridBagLayout());
        this.fields = Arrays.asList(new ExtendableTextField() {{
            addExtension(new Extension() {
                final TextIcon icon;
                
                {
                    String message = IdeBundle.message("searcheverywhere.textfield.hint",
                                                       SearchTopHitProvider.getTopHitAccelerator());
                    Color color = BigPopup.searchFieldGrayForeground();
                    this.icon = new TextIcon(message, color, null, 0);
                    this.icon.setFont(RelativeFont.SMALL.derive(getFont()));
                }
                
                @Override
                public Icon getIcon(boolean hovered) {
                    return this.icon;
                }
            });
        }}, (new SearchTextField(true)).getTextEditor());
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add("North", this.control);
        this.add("Center", this.center);
        
        this.updateFill();
    }
    
    private void updateFill() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.weightx = 1.0D;
        gbc.weighty = 1.0D;
        gbc.anchor = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        this.center.removeAll();
        this.update((field) -> this.center.add(field, gbc));
    }
    
    private void update(Consumer<? super JTextField> consumer) {
        this.fields.forEach(consumer);
        this.center.revalidate();
        this.center.repaint();
    }
    
}
