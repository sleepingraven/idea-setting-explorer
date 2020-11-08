package icons;

import com.intellij.icons.AllIcons.Actions;
import com.intellij.icons.AllIcons.FileTypes;
import com.intellij.icons.AllIcons.General;
import com.intellij.icons.AllIcons.Ide.Notification;
import com.intellij.icons.AllIcons.Nodes;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.IconUtil;

import javax.swing.*;

/**
 * @author Carry
 * @date 2020/8/11
 */
public interface PluginIcons {
    Icon MINI_LOGO = IconLoader.getIcon("/META-INF/miniLogo.svg");
    
    /* plugin common */
    
    Icon GEAR_PLAIN = General.GearPlain;
    Icon GEAR_HOVER = Notification.GearHover;
    Icon GEAR = Notification.Gear;
    Icon SETTINGS = General.Settings;
    Icon EDITORCONFIG = Nodes.Editorconfig;
    
    Icon PROJECT_CONFIGURABLE = General.ProjectConfigurable;
    
    /* actions */
    
    Icon GO_TO_SETTING = Actions.Preview;
    
    Icon MOVE_TO_BOTTOM_RIGHT = Actions.MoveToBottomRight;
    Icon MOVE_TO_TOP_RIGHT = Actions.MoveToTopRight;
    
    Icon HIDE_TOOL_WINDOW = IconUtil.brighter(General.HideToolWindow, 8);
    Icon HIDE_TOOL_WINDOW_dark = IconUtil.darker(General.HideToolWindow, 8);
    Icon SHOW_TOOL_WINDOW = General.HideToolWindow;
    
    Icon PREVIEW_DETAILS = Actions.PreviewDetails;
    Icon PREVIEW_DETAILS_VERTICALLY = Actions.PreviewDetailsVertically;
    Icon SHOW = Actions.Show;
    
    Icon ACTIVATE_CATALOG = General.LayoutEditorOnly;
    Icon VIEW_MODE = General.LayoutEditorPreview;
    Icon PIN_LOCATION = General.Pin_tab;
    
    Icon COLLAPSE_ALL = Actions.Collapseall;
    Icon REFRESH = Actions.Refresh;
    
    /* admin actions */
    
    Icon POPULATE_XML_DATA = FileTypes.Xml;
    Icon GENERATE_RESOURCE = Actions.ShowAsTree;
    
}
