package carry.ui.list;

import carry.actions.base.MyShortcuts;
import carry.common.entity.BaseComposite;
import carry.common.entity.ConfigWrapper;
import carry.common.visitors.ApplicationServiceAccessor;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.popup.AbstractPopup;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Carry
 * @date 2020/7/29
 */
public class ConfigsListSearchPopup implements ApplicationServiceAccessor {
    private final Project project;
    private final SearchListMainPanel<BaseComposite> mainPanel;
    private final JBPanel<JBPanel<?>> popupPanel;
    
    private JBPopup jbPopup;
    
    public static ConfigsListSearchPopup getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ConfigsListSearchPopup.class);
    }
    
    public ConfigsListSearchPopup(Project project) {
        this.project = project;
        mainPanel = new SearchListMainPanel<BaseComposite>() {
            @Override
            protected void ok(BaseComposite element, String text) {
                jbPopup.closeOk(null);
                String config = "";
                String item = null;
                for (int i = mainPanel.jbList.getSelectedIndex(); i >= 0; i--) {
                    BaseComposite composite = mainPanel.jbList.getModel().getElementAt(i);
                    if (composite instanceof ConfigWrapper) {
                        config = composite.getDisplayName();
                        if (i != mainPanel.jbList.getSelectedIndex()) {
                            item = element.getDisplayName();
                        }
                        break;
                    }
                }
                CONFIGS_UTIL.showSettingsDialog(project, config, item);
            }
            
            @Override
            protected void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON3:
                        break;
                    case MouseEvent.BUTTON1:
                        if (e.isAltDown()) {
                            UI_UTIL.doExpandOrCollapseConfig(jbList);
                        } else {
                            okIfSelected();
                        }
                        break;
                    default:
                }
            }
            
            @Override
            protected void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        if (e.isAltDown()) {
                            UI_UTIL.doExpandOrCollapseConfig(jbList);
                        } else {
                            okIfSelected();
                        }
                        break;
                    default:
                }
            }
            
            @Override
            protected boolean matches(BaseComposite element, String pattern) {
                return element.getDisplayName().toLowerCase().contains(pattern.toLowerCase());
            }
            
            @Override
            protected String getDescription(BaseComposite element) {
                return CONFIGS_UTIL.getFullPath(element);
            }
        };
        
        popupPanel = new JBPanel<>(new BorderLayout());
        popupPanel.add(mainPanel, BorderLayout.CENTER);
        
        mainPanel.jbList.setCellRenderer(new ConfigsListCellRender());
        mainPanel.jbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.jbList.setEmptyText(IdeBundle.message("label.no.actions.found"));
        
        reload();
    }
    
    private JBPopup create() {
        // if reuse the builder, it will be a npe on the second time
        ComponentPopupBuilder builder = JBPopupFactory.getInstance()
                                                      .createComponentPopupBuilder(popupPanel, mainPanel)
                                                      .setCancelOnClickOutside(true)
                                                      .setCancelOnOtherWindowOpen(false)
                                                      .setCancelOnWindowDeactivation(false)
                                                      .setCancelKeyEnabled(false)
                                                      .setAdText("Press " + KeymapUtil.getShortcutsText(
                                                              CommonShortcuts.ALT_ENTER.getShortcuts()) +
                                                                 " to collapse/expand")
                                                      .setTitle("Search Settings")
                                                      .setMovable(true)
                                                      .setResizable(true)
                                                      .setRequestFocus(true)
                                                      .setMayBeParent(true);
        JBPopup jbPopup = builder.createPopup();
        JComponent content = jbPopup.getContent();
        
        final Dimension popupDimension = PERSISTENCE.getCatalogPopupSize();
        // apply size
        jbPopup.setSize(popupDimension);
        jbPopup.addListener(new JBPopupListener() {
            int headerH, footerH;
            
            @Override
            public void beforeShown(@NotNull LightweightWindowEvent event) {
                // cannot effect outside when resized to minimum size
                // it dose not show in center if remove the code outside
                jbPopup.setSize(popupDimension);
                // h:30
                Dimension h = ((AbstractPopup) jbPopup).getHeaderPreferredSize();
                // h:20
                Dimension f = ((AbstractPopup) jbPopup).getFooterPreferredSize();
                // h:30
                Dimension tf = mainPanel.jbTextField.getPreferredSize();
                headerH = h.height;
                footerH = f.height;
                jbPopup.setMinimumSize(new Dimension(tf.width, headerH + tf.height + footerH));
            }
            
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                Dimension size1 = ((AbstractPopup) jbPopup).getComponent().getSize();
                // w+2, h+52
                Dimension size2 = jbPopup.getContent().getSize();
                
                int width = size1.width;
                int height = size1.height + headerH - 1;
                popupDimension.setSize(width, height);
                
                mainPanel.clearStatusBar(project);
            }
        });
        
        // apply shortcuts
        MyShortcuts.register(CommonShortcuts.ESCAPE, content, (e) -> jbPopup.cancel(), true);
        
        return jbPopup;
    }
    
    public void show() {
        jbPopup = create();
        ApplicationManager.getApplication().invokeLater(() -> {
            jbPopup.showInFocusCenter();
            
            mainPanel.jbTextField.selectAll();
        }, ModalityState.NON_MODAL, project.getDisposed());
    }
    
    private void reload() {
        List<BaseComposite> composites = CONFIGS_UTIL.reloadConfigurables(project).getComposites();
        mainPanel.setValues(composites);
    }
    
}
