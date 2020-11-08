package admin.actions;

import admin.utils.XmlDataUtil;
import carry.actions.base.MyAnAction;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import icons.PluginIcons;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/9/4
 */
public class PopulateXmlDataAction extends MyAnAction implements ApplicationServiceAccessor {
    
    public PopulateXmlDataAction() {
        super("Populate Xml Data", "Populate persistent app data template", PluginIcons.POPULATE_XML_DATA);
    }
    
    /**
     * @see Messages#showTwoStepConfirmationDialog(String, String, String, Icon)
     */
    @Override
    public void actionPerformed(Project project, ConfigsTreeController configsTreeController) {
        // without checkbox
        // Messages.showIdeaMessageDialog(project, "Mes", "Tit", new String[] { Messages.getOkButton() }, 0,
        //                                Messages.getInformationIcon(), null);
        // Messages.showDialog(project, "Mes", "Tit", new String[] { Messages.getOkButton() }, 0,
        //                     Messages.getInformationIcon());
        
        boolean clear = !XmlDataUtil.populateXmlData(CONFIGS_UTIL.collectConfigurables(project));
        int exitCode = Messages.showCheckboxMessageDialog("Xml data populated completed", "Completed",
                                                          new String[] { Messages.getOkButton() }, "Clear", clear, 0, 0,
                                                          Messages.getInformationIcon(), null);
        if (exitCode == DialogWrapper.OK_EXIT_CODE) {
            XmlDataUtil.unPopulateXmlData();
        }
    }
    
}
