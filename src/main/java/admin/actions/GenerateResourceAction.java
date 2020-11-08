package admin.actions;

import admin.utils.ResourceUtil;
import carry.actions.base.MyAnAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import icons.PluginIcons;

import java.util.function.BiConsumer;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class GenerateResourceAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public GenerateResourceAction() {
        super("Generate Resources", "Generate document directories from configs' structure",
              PluginIcons.GENERATE_RESOURCE);
    }
    
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        // without checkbox
        // String basePath =
        //         Messages.showInputDialog(project, "What is the base directory to generate?", "Generate Resources",
        //                                  Messages.getQuestionIcon(), ResourceUtil.BASE_PATH, null,
        //                                  TextRange.allOf(ResourceUtil.BASE_PATH),
        //                                  "You can use both \"\\\" and \"/\" on Windows platform");
        
        Pair<String, Boolean> exitData =
                Messages.showInputDialogWithCheckBox("What is the base directory to generate?", "Generate Resources",
                                                     "Generate .html desc document", false, true,
                                                     Messages.getQuestionIcon(), ResourceUtil.BASE_PATH, null);
        String basePath = exitData.first;
        if (basePath != null) {
            BiConsumer<String, Boolean> callback = (finalPath, state) -> {
                if (state) {
                    Messages.showInfoMessage(project, getCallbackMessage(true, finalPath), getCallbackTitle(true));
                } else {
                    Messages.showErrorDialog(project, getCallbackMessage(false, finalPath), getCallbackTitle(false));
                }
            };
            ResourceUtil.generateResource(CONFIGS_UTIL.collectConfigurables(project), basePath, exitData.second,
                                          callback);
        }
    }
    
    private String getCallbackMessage(boolean state, String basePath) {
        return String.format("Generating %s to directory \"%s\"", getStatus(state), basePath);
    }
    
    private String getCallbackTitle(boolean state) {
        return String.format("Generating %s", getStatus(state));
    }
    
    private String getStatus(boolean state) {
        return state ? "successfully" : "failed";
    }
    
}
