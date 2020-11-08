package carry.ui.list;

import carry.common.AppConstants;
import carry.common.visitors.ApplicationServiceAccessor;
import com.intellij.openapi.actionSystem.impl.ActionMenu;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.speedSearch.FilteringListModel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.JBUI.Borders;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Carry
 * @date 2020/7/28
 */
public abstract class SearchListMainPanel<E> extends JBPanel<SearchListMainPanel<E>> implements ApplicationServiceAccessor {
    final SearchTextField searchTextField;
    final JBTextField jbTextField;
    final JBList<E> jbList;
    private final FilteringListModel<E> model;
    
    public SearchListMainPanel() {
        this.model = new FilteringListModel<>(new CollectionListModel<>());
        searchTextField = new SearchTextField(AppConstants.SEARCH_HISTORY_PROPERTY_NAME);
        jbTextField = searchTextField.getTextEditor();
        jbList = new JBList<>(model);
        initComponents(new FilterCondition());
    }
    
    /**
     * see field "myResultsList" in {@link com.intellij.ide.actions.BigPopupUI BigPopupUI} and {@link
     * com.intellij.ide.actions.searcheverywhere.SearchEverywhereUI SearchEverywhereUI}<br/>
     * see {@link com.intellij.ide.actions.BigPopupUI BigPopupUI}'s method "createSuggestionsPanel"
     *
     * @see com.intellij.ide.ui.laf.darcula.ui.DarculaTextBorder#paintDarculaSearchArea(Graphics2D, Rectangle,
     * JTextComponent, boolean)
     */
    private void initComponents(FilterCondition condition) {
        // 1.adjust styles
        // jbTextField.setBorder(Borders.customLine(JBColor.BLACK, 1));
        // 1) no border and with a padding
        // jbTextField.putClientProperty("JTextField.Search.noBorderRing", true);
        // 2) heavy borders
        // jbTextField.setBorder(Borders.customLine((DarculaUIUtil.getOutlineColor(true, false))));
        // 3) no border
        jbTextField.setBorder(Borders.empty());
        // with a top border
        JScrollPane jScrollPane = ScrollPaneFactory.createScrollPane(jbList, true);
        ComponentUtil.putClientProperty(jScrollPane.getVerticalScrollBar(), JBScrollPane.IGNORE_SCROLLBAR_IN_INSETS,
                                        true);
        jScrollPane.setBorder(JBUI.Borders.customLine(JBUI.CurrentTheme.BigPopup.searchFieldBorderColor(), 1, 0, 0, 0));
        
        // 2.add components
        // if use GridBayLayout, the contents will lose 1 pixel w&h after resizing
        // setLayout(UI_UTIL.createGridBagLayout(new boolean[] { false, true }, new boolean[] { true }));
        // add(jbTextField, UI_UTIL.newDefaultGbc(0, 0));
        // add(jScrollPane, UI_UTIL.newDefaultGbc(0, 1));
        setLayout(new BorderLayout());
        add(jbTextField, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
        
        // 3.perform other listeners
        SearchMouseAdapter mouseAdapter = new SearchMouseAdapter();
        jbList.addMouseListener(mouseAdapter);
        jbList.addMouseMotionListener(mouseAdapter);
        jbList.addListSelectionListener(e -> {
            // cause a exception on fold node
            int index = jbList.getLeadSelectionIndex();
            if (index != -1) {
                String description = getDescription(jbList.getModel().getElementAt(index));
                if (description != null) {
                    ActionMenu.showDescriptionInStatusBar(true, jbList, description);
                }
            }
        });
        jbTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                model.setFilter(condition.of(jbTextField.getText()));
                ScrollingUtil.ensureSelectionExists(jbList);
            }
        });
        jbTextField.addKeyListener(new SearchKeyAdapter());
        addFocusListener(new SearchFocusAdapter());
        
        // 4.perform other behaviors
        jbList.setFocusable(false);
        ScrollingUtil.installActions(jbList, jbTextField);
        searchTextField.setHistorySize(50);
    }
    
    public void setValues(List<E> values) {
        model.replaceAll(values);
        ScrollingUtil.ensureSelectionExists(jbList);
    }
    
    /* customizing part */
    
    public class FilterCondition implements Condition<E> {
        private String pattern = "";
        
        private FilterCondition of(String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        @Override
        public boolean value(E element) {
            return matches(element, pattern);
        }
        
    }
    
    /**
     * defines ok action
     */
    protected void ok(E element, String text) {
    }
    
    public void okIfSelected() {
        E selectedValue = jbList.getModel().getElementAt(jbList.getLeadSelectionIndex());
        if (selectedValue != null) {
            ok(selectedValue, jbTextField.getText());
        }
    }
    
    protected abstract void mouseClicked(MouseEvent e);
    
    protected abstract void keyPressed(KeyEvent e);
    
    /**
     * match to do filter
     *
     * @param element
     *         element to match
     * @param pattern
     *         the text in the text field
     *
     * @return whether matches
     */
    protected abstract boolean matches(E element, String pattern);
    
    /**
     * define description of element selecting
     */
    protected String getDescription(E element) {
        return null;
    }
    
    /**
     * call this to reset the description when hide the panel
     */
    public void clearStatusBar(Project project) {
        WindowManager.getInstance().getStatusBar(project).setInfo(null);
    }
    
    /* listeners */
    
    private class SearchFocusAdapter extends FocusAdapter {
        
        @Override
        public void focusGained(FocusEvent e) {
            IdeFocusManager.getInstance(null).requestFocus(jbTextField, true);
        }
        
    }
    
    /**
     * {@link com.intellij.ide.actions.searcheverywhere.SearchEverywhereUI SearchEverywhereUI}'s method
     * "initSearchActions"
     */
    private class SearchMouseAdapter extends MouseAdapter {
        private int currentDescriptionIndex;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            SearchListMainPanel.this.mouseClicked(e);
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            int index = jbList.locationToIndex(e.getPoint());
            indexChanged(index);
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            int index = jbList.locationToIndex(e.getPoint());
            indexChanged(index);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            int index = jbList.getLeadSelectionIndex();
            indexChanged(index);
        }
        
        private void indexChanged(int index) {
            if (index != -1 && index != currentDescriptionIndex) {
                currentDescriptionIndex = index;
                String description = getDescription(jbList.getModel().getElementAt(index));
                if (description != null) {
                    ActionMenu.showDescriptionInStatusBar(true, jbList, description);
                }
            }
        }
        
    }
    
    private class SearchKeyAdapter extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {
            SearchListMainPanel.this.keyPressed(e);
        }
        
    }
    
}
