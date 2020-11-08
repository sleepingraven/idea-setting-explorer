package carry.settings.template;

import carry.settings.base.BaseSettingsComponent;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a JPanel for the Settings Dialog.
 *
 * @author Carry
 * @date 2020/7/26
 */
public class TemplateSettingsComponent extends BaseSettingsComponent {
    private final JBTextField myUserNameText = new JBTextField();
    private final JBCheckBox myIdeaUserStatus = new JBCheckBox("Do you use intelliJ IDEA? ");
    
    @Override
    public FormBuilder createFormBuilder() {
        return FormBuilder.createFormBuilder()
                          .addLabeledComponent("Enter user name: ", myUserNameText, 1, false)
                          .addComponent(myIdeaUserStatus, 1)
                          .addComponentFillVertically(new JPanel(), 0);
    }
    
    @Override
    public JComponent getPreferredFocused() {
        return myUserNameText;
    }
    
    /* wrapped components */
    
    @NotNull
    public String getUserNameText() {
        return myUserNameText.getText();
    }
    
    public void setUserNameText(@NotNull String newText) {
        myUserNameText.setText(newText);
    }
    
    public boolean getIdeaUserStatus() {
        return myIdeaUserStatus.isSelected();
    }
    
    public void setIdeaUserStatus(boolean newStatus) {
        myIdeaUserStatus.setSelected(newStatus);
    }
    
}
