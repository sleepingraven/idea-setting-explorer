package carry.actions.base;

import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions.ActionDescription;
import com.intellij.openapi.util.NlsActions.ActionText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/8/26
 */
public abstract class MyAnAction extends AnAction {
    
    public MyAnAction(@Nullable @ActionText String text, @Nullable @ActionDescription String description,
            @Nullable Icon icon) {
        super(text, description, icon);
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        ConfigsTreeController configsTreeController = ConfigsTreeController.getInstance(project);
        actionPerformed(project, configsTreeController);
    }
    
    public abstract void actionPerformed(Project project, ConfigsTreeController configsTreeController);
    
}
