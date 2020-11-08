package demo;

import com.intellij.icons.AllIcons.General;
import com.intellij.icons.AllIcons.Ide.Notification;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.ToggleActionButton;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.OnOffButton;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.UIUtil;
import demo.ui.components.TextFieldDemo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Carry
 * @date 2020/7/27
 */
public class DemoDispatcher {
    private static final String[] valArr =
            new String[] { "Appearance", "Menus and Toolbars", "File Colors", "Color Scheme Font" };
    
    public static final JBList<String> jbList = new JBList<>(valArr);
    private static final TableModel myTableModel = new TableModel() {
        private final int[][] model = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 6, 7, 8 } };
        
        @Override
        public int getRowCount() {
            return 3;
        }
        
        @Override
        public int getColumnCount() {
            return 3;
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            return Character.toString((char) ('a' + columnIndex));
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return String.valueOf(model[rowIndex][columnIndex]);
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
        }
        
        @Override
        public void addTableModelListener(TableModelListener l) {
        
        }
        
        @Override
        public void removeTableModelListener(TableModelListener l) {
        
        }
    };
    public static final JBTable jbTable = new JBTable(myTableModel);
    public static final JTable jTable = new JTable(myTableModel);
    
    public void doDemo(Project project, AnActionEvent event) {
        // Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
        // String str =
        //         Messages.showInputDialog(project, "What is your name?", "Input Your Name", Messages.getQuestionIcon());
        
        // 1.【Dialog】 with panels and few buttons
        // DemoDialog df = new DemoDialog(project);
        // df.show();
        
        /**/
        
        // 2.【PopupChooserBuilder】 with several options
        // IPopupChooserBuilder<String> pcbL =
        //         JBPopupFactory.getInstance().createPopupChooserBuilder(Arrays.asList(valArr));
        // JBPopup popup = pcbL.createPopup();
        // popup.showInFocusCenter();
        // PopupChooserBuilder<?> pcbT = JBPopupFactory.getInstance().createPopupChooserBuilder(jTable);
        // pcbT.createPopup().showInFocusCenter();
        
        // 3.【ListPopup】
        // ListPopup listPopup = JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<>("Test", valArr));
        // listPopup.showInFocusCenter();
        
        // 4.by 【ComponentPopupBuilder】 with 【buttons】
        // showNotePopup(project);
        
        // 5.【Balloon】
        // showWrongJavaVersionBalloon();
        
        // 6.【ListSpeedSearch】
        // showUsersPopup();
        
        /**/
        
        // 7.【Text Field】
        // testTextField();
        
    }
    
    private void showNotePopup(Project project) {
        JTextField myTextField = new JTextField("hah");
        myTextField.setFont(UIUtil.getTreeFont());
        
        JBPanel<JBPanel<?>> jbPanel = new JBPanel<>();
        OnOffButton comp = new OnOffButton();
        jbPanel.add(comp);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(myTextField, BorderLayout.CENTER);
        panel.add(jbPanel, BorderLayout.EAST);
        panel.add(TOOLBAR.getComponent(), BorderLayout.SOUTH);
        
        ComponentPopupBuilder builder = JBPopupFactory.getInstance()
                                                      .createComponentPopupBuilder(panel, myTextField)
                                                      .setCancelOnClickOutside(true)
                                                      .setAdText("Press " + KeymapUtil.getShortcutsText(
                                                              CommonShortcuts.CTRL_ENTER.getShortcuts()) + " to finish")
                                                      .setTitle("Search Settings")
                                                      .setMovable(true)
                                                      .setRequestFocus(true)
                                                      .setResizable(true)
                                                      .setMayBeParent(true);
        JBPopup popup = builder.createPopup();
        popup.setMinimumSize(new Dimension(200, 90));
        JComponent content = popup.getContent();
        
        // AnAction action = new DumbAwareAction() {
        AnAction action = new AnAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                unregisterCustomShortcutSet(content);
                popup.closeOk(e.getInputEvent());
                // after.consume(textArea.getText());
            }
        };
        
        action.registerCustomShortcutSet(CommonShortcuts.CTRL_ENTER, content);
        ApplicationManager.getApplication()
                          .invokeLater(popup::showInFocusCenter, ModalityState.NON_MODAL, project.getDisposed());
    }
    
    private void showWrongJavaVersionBalloon() {
        // Just a copy paste from ChangesViewBaloonProblemNotifier
        final JFrame frame = WindowManager.getInstance().findVisibleFrame();
        final JComponent component = frame.getRootPane();
        if (component == null) {
            return;
        }
        final Rectangle rect = component.getVisibleRect();
        final Point p = new Point(rect.x + 30, rect.y + rect.height - 10);
        final RelativePoint point = new RelativePoint(component, p);
        
        final MessageType messageType = MessageType.ERROR;
        final BalloonBuilder builder = JBPopupFactory.getInstance()
                                                     .createHtmlTextBalloonBuilder(
                                                             IdeBundle.message("title.no.jdk.specified"),
                                                             messageType.getDefaultIcon(),
                                                             messageType.getPopupBackground(), null);
        builder.setFadeoutTime(-1);
        builder.createBalloon().show(point, Balloon.Position.above);
    }
    
    private void showUsersPopup() {
        final JBList<String> jbList = new JBList<>();
        jbList.setListData(ArrayUtil.toStringArray(Arrays.asList(valArr)));
        new ListSpeedSearch<>(jbList);
        
        ListPopup listPopup = JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<>("Test", valArr));
        // listPopup.showInFocusCenter();
        PopupChooserBuilder<?> builder = JBPopupFactory.getInstance()
                                                       .createListPopupBuilder(jbList)
                                                       .setTitle("Select Author or Committer")
                                                       .setResizable(true)
                                                       .setCancelOnWindowDeactivation(false)
                                                       //.setDimensionServiceKey("Git.Select user")
                                                       .setItemChoosenCallback(() -> {
                                                           if (jbList.getSelectedIndices().length > 0) {
                                                               // continuation.consume((String) jbList.getSelectedValue());
                                                           }
                                                       })
                                                       .setSouthComponent(new JBTextField())
                                                       .setMovable(true);
        //.setSouthComponent(new JBTextField())//.setSelectionMode()
        JBPopup popup = builder.createPopup();
        ((AbstractPopup) popup).getComponent().add(new JBTextField(), BorderLayout.NORTH);
        popup.showInFocusCenter();
    }
    
    private static JFrame frame;
    
    private void testTextField() {
        if (frame != null && frame.isVisible()) {
            frame.dispose();
            frame = null;
        } else {
            if (frame == null) {
                frame = new JFrame("Test Text Fields");
                // frame.add(new SeveralTextFieldsDemo());
                frame.add(new TextFieldDemo());
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
            
            frame.setVisible(true);
            frame.toFront();
        }
    }
    
    private static AnAction createAction(Icon icon) {
        return new AnAction("Hah", "Search idea settings", icon) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
            }
        };
    }
    
    private static final AnAction[] AN_ACTIONS = {
            createAction(Notification.Collapse), createAction(Notification.CollapseHover),
            createAction(Notification.Expand), createAction(Notification.ExpandHover), createAction(General.ArrowDown),
            createAction(General.ArrowRight),
            // createAction(Nodes.TreeDownArrow), createAction(Nodes.TreeRightArrow),
            new ToggleActionButton("Test", null) {
                boolean selected;
                
                @Override
                public boolean isSelected(AnActionEvent e) {
                    return selected;
                }
                
                @Override
                public void setSelected(AnActionEvent e, boolean state) {
                    selected = state;
                }
            }
    };
    private static final ActionGroup GROUP = new ActionGroup() {
        @Override
        public @NotNull AnAction[] getChildren(@Nullable AnActionEvent e) {
            return AN_ACTIONS;
        }
    };
    public static final ActionToolbar TOOLBAR =
            ActionManager.getInstance().createActionToolbar("test-my-toolbar-group", GROUP, true);
    
}
