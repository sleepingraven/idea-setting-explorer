package carry.actions.base;

import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions.ActionDescription;
import com.intellij.openapi.util.NlsActions.ActionText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/8/25
 */
public abstract class MyToggleAction extends ToggleAction {
    protected boolean selected;
    
    public MyToggleAction(@Nullable @ActionText String text, @Nullable @ActionDescription String description,
            @Nullable final Icon icon, boolean selected) {
        super(text, description, icon);
        this.selected = selected;
    }
    
    @Override
    public boolean isSelected(@Nullable AnActionEvent e) {
        return selected;
    }
    
    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        setSelected(e.getProject(), state);
    }
    
    public void setSelected(@NotNull Project project, boolean state) {
        if (selected == state) {
            return;
        }
        
        selected = state;
        ConfigsTreeController configsTreeController = ConfigsTreeController.getInstance(project);
        if (state) {
            onSelect(project, configsTreeController);
        } else {
            onUnselect(project, configsTreeController);
        }
    }
    
    public abstract void onSelect(Project project, ConfigsTreeController configsTreeController);
    
    public abstract void onUnselect(Project project, ConfigsTreeController configsTreeController);
    
}
