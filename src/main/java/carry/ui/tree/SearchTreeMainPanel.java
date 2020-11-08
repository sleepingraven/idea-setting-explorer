package carry.ui.tree;

import carry.common.AppConstants;
import carry.ui.tree.matcher.FilteredCache;
import com.intellij.ide.structureView.newStructureView.StructureViewComponent;
import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.impl.ActionMenu;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.speedSearch.ElementFilter;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.filtered.FilteringTreeStructure;
import com.intellij.ui.treeStructure.filtered.FilteringTreeStructure.FilteringNode;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.JBUI.Borders;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.AsyncPromise;
import org.jetbrains.concurrency.Promise;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * To reuse this panel with performance optimizing, you should call {@link SearchTreeMainPanel#prepareToShow(Disposable)}
 * before showing at Disposable.
 *
 * @author Carry
 * @date 2020/8/13
 * @see com.intellij.ide.util.FileStructurePopup
 */
public abstract class SearchTreeMainPanel<E> extends JBPanel<SearchTreeMainPanel<E>> {
    final SearchTextField searchTextField;
    public final JBTextField jbTextField;
    final JScrollPane jScrollPane;
    public final SimpleTree tree;
    
    final TreeStructureAdapter<E> treeStructure;
    final FilteringTreeStructure filteringTreeStructure;
    private StructureTreeModel<FilteringTreeStructure> structureTreeModel;
    public AsyncTreeModel asyncTreeModel;
    
    final TermProducer term;
    final FilteredCache<E> filteredCache;
    
    public SearchTreeMainPanel(TreeStructureAdapter<E> treeStructure, FilteredCache<E> filteredCache,
            BooleanSupplier caseSensitive) {
        searchTextField = new SearchTextField(AppConstants.SEARCH_HISTORY_PROPERTY_NAME);
        jbTextField = searchTextField.getTextEditor();
        tree = new SimpleTree() {
            @Override
            public void collapsePath(TreePath path) {
                setExpandedState(path, false);
            }
        };
        jScrollPane = ScrollPaneFactory.createScrollPane(tree, true);
        
        this.treeStructure = treeStructure;
        filteringTreeStructure = new FilteringTreeStructure((ElementFilter<E>) filteredCache::match, treeStructure);
        term = new TermProducer(caseSensitive);
        this.filteredCache = filteredCache;
        initComponents();
    }
    
    private void initComponents() {
        // 1.adjust styles
        jbTextField.setBorder(Borders.empty());
        // prevent a behavior when scroll to bottom and make wide
        ComponentUtil.putClientProperty(jScrollPane.getHorizontalScrollBar(), JBScrollPane.IGNORE_SCROLLBAR_IN_INSETS,
                                        true);
        ComponentUtil.putClientProperty(jScrollPane.getVerticalScrollBar(), JBScrollPane.IGNORE_SCROLLBAR_IN_INSETS,
                                        true);
        // with a top border
        jScrollPane.setBorder(JBUI.Borders.customLine(JBUI.CurrentTheme.BigPopup.searchFieldBorderColor(), 1, 0, 0, 0));
        
        // 2.add components
        setLayout(new BorderLayout());
        add(jbTextField, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
        
        // 3.perform other listeners
        SearchMouseAdapter mouseAdapter = new SearchMouseAdapter();
        tree.addMouseListener(mouseAdapter);
        tree.addMouseMotionListener(mouseAdapter);
        tree.addTreeSelectionListener(e -> {
            // may same as e.getNewLeadSelectionPath()
            // don't use tree.getLeadSelectionPath()
            TreePath leadSelectionPath = tree.getSelectionModel().getLeadSelectionPath();
            if (leadSelectionPath != null) {
                E element = getTreeElement(leadSelectionPath);
                if (element != null) {
                    String description = getDescription(element);
                    if (description != null) {
                        ActionMenu.showDescriptionInStatusBar(true, tree, description);
                    }
                    onSelected(element, leadSelectionPath);
                }
            }
        });
        jbTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                structureTreeModel.getInvoker().invoke(() -> {
                    term.pattern = jbTextField.getText();
                    term.lowerPattern = term.pattern.toLowerCase();
                    filteredCache.clear();
                    
                    filteringTreeStructure.refilter();
                    structureTreeModel.invalidate().onSuccess(res -> {
                        UIUtil.invokeLaterIfNeeded(() -> {
                            TreeUtil.selectFirstNode(tree);
                            TreeUtil.expandAll(tree);
                        });
                    });
                });
            }
        });
        SearchKeyAdapter searchKeyAdapter = new SearchKeyAdapter();
        jbTextField.addKeyListener(searchKeyAdapter);
        tree.addKeyListener(searchKeyAdapter);
        
        // 4.perform other behaviors
        TreeUtil.installActions(tree);
        searchTextField.setHistorySize(50);
    }
    
    /**
     * {@link com.intellij.ide.util.FileStructurePopup}'s method "rebuildAndSelect"
     */
    public Promise<Object> prepareToShow(Disposable parent) {
        structureTreeModel = new StructureTreeModel<>(filteringTreeStructure, parent);
        asyncTreeModel = new AsyncTreeModel(structureTreeModel, true, parent);
        tree.setModel(asyncTreeModel);
        
        AsyncPromise<Object> result = new AsyncPromise<>();
        structureTreeModel.getInvoker().invoke(() -> {
            structureTreeModel.invalidate().onSuccess(result::setResult);
        });
        return result;
    }
    
    protected void doRebuild() {
        filteringTreeStructure.getRootElement().setDelegate(treeStructure.getRootElement());
        filteringTreeStructure.rebuild();
        structureTreeModel.getInvoker().invoke(() -> {
            structureTreeModel.invalidate();
        });
    }
    
    @RequiredArgsConstructor
    static class TermProducer implements Supplier<String> {
        private final BooleanSupplier caseSensitive;
        private String pattern = "";
        private String lowerPattern = "";
        
        @Override
        public String get() {
            return caseSensitive.getAsBoolean() ? pattern : lowerPattern;
        }
        
    }
    
    /* customizing part */
    
    @AllArgsConstructor
    public static abstract class TreeStructureAdapter<E> extends AbstractTreeStructure {
        final Function<E, Icon> iconChooser;
        final Function<E, String> namer;
        
        public abstract @NotNull E getRoot();
        
        public abstract @NotNull E[] getChildren(@NotNull E element);
        
        public abstract @Nullable E getParent(@NotNull E element);
        
        @Override
        public @NotNull Object getRootElement() {
            return getRoot();
        }
        
        @Override
        public @NotNull Object[] getChildElements(@NotNull Object element) {
            return getChildren((E) element);
        }
        
        @Override
        public @Nullable Object getParentElement(@NotNull Object element) {
            return getParent((E) element);
        }
        
        /**
         * myName don't effect, using "toString", see {@link FilteringNode}'s method "doUpdate"
         */
        @Override
        public @NotNull NodeDescriptor<?> createDescriptor(@NotNull Object element,
                @Nullable NodeDescriptor parentDescriptor) {
            E elem = (E) element;
            return new NodeDescriptor<E>(null, parentDescriptor) {
                {
                    myClosedIcon = iconChooser.apply(elem);
                    myName = namer.apply(elem);
                }
                
                @Override
                public boolean update() {
                    return false;
                }
                
                @Override
                public int getWeight() {
                    return super.getWeight();
                }
                
                @Override
                public E getElement() {
                    return elem;
                }
                
                @Override
                public String toString() {
                    return myName;
                }
            };
        }
        
        @Override
        public void commit() {
        }
        
        @Override
        public boolean hasSomethingToCommit() {
            return false;
        }
        
    }
    
    /**
     * defines ok action
     */
    protected void ok(E element, String text) {
    }
    
    public void okIfSelected() {
        E selectedValue = getTreeElement(tree.getLeadSelectionPath());
        if (selectedValue != null) {
            ok(selectedValue, jbTextField.getText());
        }
    }
    
    protected abstract void mouseClicked(MouseEvent e);
    
    protected abstract void keyPressed(KeyEvent e);
    
    protected void onSelected(E element, TreePath path) {
    }
    
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
    
    private class SearchMouseAdapter extends MouseAdapter {
        private int currentDescriptionIndex;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            // or use listener with a flag instead
            if (TreeUtil.isLocationInExpandControl(tree, e.getX(), e.getY())) {
                return;
            }
            SearchTreeMainPanel.this.mouseClicked(e);
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            Point point = e.getPoint();
            int closest = tree.getClosestRowForLocation(point.x, point.y);
            Rectangle rowBounds = tree.getRowBounds(closest);
            if (point.y >= rowBounds.y && point.y <= rowBounds.y + rowBounds.height) {
                indexChanged(closest);
            } else {
                mouseExited(e);
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            mouseMoved(e);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            int index = tree.getLeadSelectionRow();
            indexChanged(index);
        }
        
        private void indexChanged(int index) {
            if (index != -1 && index != currentDescriptionIndex) {
                currentDescriptionIndex = index;
                String description = getDescription(getTreeElement(tree.getPathForRow(index)));
                if (description != null) {
                    ActionMenu.showDescriptionInStatusBar(true, tree, description);
                }
            }
        }
        
    }
    
    private class SearchKeyAdapter extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {
            SearchTreeMainPanel.this.keyPressed(e);
        }
        
    }
    
    /* additional methods */
    
    public E getTreeElement(TreePath path) {
        if (path == null) {
            return null;
        }
        return (E) StructureViewComponent.unwrapNavigatable(path.getLastPathComponent());
    }
    
}
