package carry.common;

import carry.common.data.AppPersistence;
import com.intellij.openapi.util.SystemInfo;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * to do list:<br/>
 * color scheme
 * code style...
 * build...
 * todo the lost of scrollbar when switched to View Mode
 * todo a status bar on first start<br/>
 * todo sign of "No document yet"<br/>
 * todo in specified order, see "Insert pair %> on Enter in JSP"<br/>
 * todo action: just go to config from config-content's selection<br/>
 * todo expand all, restore after searching<br/>
 * todo Box, see Editor | General | Code Folding | Fold by default<br/>
 * todo address <a> support
 * todo scroll horizontally by hand
 * <p>
 * todo custom tags<br/>
 * todo favorite configs<br/>
 * todo performance optimization-reduce new<br/>
 * todo just retain body part in html file<br/>
 * -<p>
 * document to be completed list:
 * Scopes
 * Keymap
 * code style
 * file types
 * Formatting
 * Inlay Hints
 * Language injections
 * TextMate Bundles
 * Version Control: something lost
 * Deployment
 * Application Servers
 * SQL Dialects
 * Command Line Tool Support
 * -<p>
 * to do probability:<br/>
 * memory optimization-dispose ui components and creates each time opened<br/>
 * memory optimization-unload persistence from memory<br/>
 * json app data<br/>
 * brackets validate in xsd<br/>
 * Tab instead Bag, see "Code Style"<br/>
 * find what happened before Settings highlighting<br/>
 * -<p>
 * to do reference links:<br/>
 * {@link com.intellij.util.ui.JBHtmlEditorKit JBHtmlEditorKit}<br/>
 * {@link com.intellij.util.ui.UIUtil.JBWordWrapHtmlEditorKit JBWordWrapHtmlEditorKit}<br/>
 * <br/>
 * useful links:<br/>
 * {@link com.intellij.ide.ui.customization.CustomActionsSchema#fillActionGroups(DefaultMutableTreeNode)
 * CustomActionsSchema.fillActionGroups()}<br/>
 * {@link com.intellij.openapi.actionSystem.ex.DefaultCustomComponentAction DefaultCustomComponentAction}<br/>
 * {@link com.intellij.openapi.ui.Messages#installHyperlinkSupport(JTextPane) Messages.installHyperlinkSupport()}
 * <p>
 * previous links:<br/>
 * CustomizableActionsPanel<br/>
 * SpeedSearchUtil TreeTableSpeedSearch<br/>
 * TreeUIHelper<br/>
 * DefaultTreeCellEditor BooleanTableCellEditor ComboBoxCellEditor<br/>
 * PluginStateListener AppLifecycleListener ApplicationLoadListener<br/>
 *
 * @author Carry
 * @date 2020/8/6
 */
public interface AppConstants {
    String PLUGIN_NAME = "Idea Setting Explorer";
    String VIEW_TITLE = !SystemInfo.isMac ? "View Settings" : "View Preferences";
    String VIEW_DESCRIPTION = !SystemInfo.isMac ? "View and search settings" : "View and search preferences";
    String SEARCH_TITLE = !SystemInfo.isMac ? "Search Settings" : "Search Preferences";
    
    /* versions and controls */
    
    boolean ADMIN_MODE = false;
    
    /* state constants */
    
    String PERSISTENCE_ID_0 = getCommonId(AppPersistence.class);
    String PERSISTENCE_ID = "carry.common.data.AppPersistence";
    String PERSISTENCE_STORAGE = "IdeaSettingExplorerPluginData.xml";
    String SETTING_STATE_ID = "carry.settings.AppSettingsState";
    String SETTING_STATE_STORAGE = "IdeaSettingExplorerPluginConfig.xml";
    
    /* data constants */
    
    int FLAT_CONFIGS_MAP_SIZE = 512;
    // todo
    int ALL_ITEMS_MAP_SIZE = 1024;
    
    String ID_SEPARATOR_CONFIG = "@@@";
    String ID_SEPARATOR_BAG = "###";
    String ID_SEPARATOR_POINT = "$$$";
    String PATH_SEPARATOR_CONFIG = "  ›  ";
    String PATH_SEPARATOR_BAG = "  |  ";
    String PATH_SEPARATOR_POINT = "  -  ";
    
    String DESC_DOC_NAME = "desc.html";
    
    /* ui constants */
    
    String APP_PROP_PREFIX = "com.github.sleepingraven.idea-setting-explorer";
    String SEARCH_HISTORY_PROPERTY_NAME = APP_PROP_PREFIX + ".My.Search.History";
    String VIEW_MODE_PROPORTION = APP_PROP_PREFIX + ".My.View.Mode.Proportion";
    int CATALOG_POPUP_WIDTH = 900;
    int CATALOG_POPUP_HEIGHT = 556;
    int DESCRIPTION_POPUP_WIDTH = 250;
    int DESCRIPTION_POPUP_HEIGHT = 405;
    float DEFAULT_PROPORTION = .3F;
    
    static String getCommonId(Class<?> clazz) {
        return clazz.getCanonicalName();
    }
    
}
