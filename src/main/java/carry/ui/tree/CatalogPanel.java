package carry.ui.tree;

import carry.common.entity.Bag;
import carry.common.entity.BaseComposite;
import carry.common.entity.BaseContent;
import carry.common.entity.ConfigWrapper;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.listeners.MyLafListener;
import carry.ui.base.BasePopupWrapper;
import carry.ui.tree.matcher.ConfigsFilteredCache;
import com.intellij.util.ui.tree.TreeUtil;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Carry
 * @date 2020/8/28
 */
public class CatalogPanel extends SearchTreeMainPanel<BaseComposite> implements ApplicationServiceAccessor {
    private final ConfigsTreeController controller;
    
    int lastSelectedRow;
    
    public CatalogPanel(ConfigsTreeController controller, BooleanSupplier caseSensitive) {
        super(new ConfigTreeStructure(CONFIGS_UTIL.collectConfigurables(controller.getProject())),
              new ConfigsFilteredCache(() -> controller.getCatalogPanel().term.get(), caseSensitive), caseSensitive);
        this.controller = controller;
        
        tree.setCellRenderer(new ConfigsTreeCellRender(tree.getRenderer(), (ConfigsFilteredCache) filteredCache, term));
        tree.getEmptyText().setText("");
        
        tree.setRootVisible(false);
        tree.setScrollsOnExpand(false);
        tree.setToggleClickCount(1);
        tree.setHorizontalAutoScrollingEnabled(false);
        
        addAdditionalListeners();
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (e.getComponent().isShowing()) {
                    if (MyLafListener.textFieldBackground != null) {
                        jbTextField.setBackground(MyLafListener.textFieldBackground);
                    }
                }
            }
        });
    }
    
    @Override
    protected void ok(BaseComposite element, String text) {
        BasePopupWrapper currentPopup;
        if (controller.nonViewMode()) {
            currentPopup = controller.getCatalogPopup();
        } else {
            currentPopup = controller.getViewModePopup();
        }
        currentPopup.closeOk(null);
        
        ConfigWrapper preselected = null;
        String item = null;
        for (BaseComposite c = element; c != null; c = c.getParent()) {
            if (c instanceof ConfigWrapper) {
                preselected = ((ConfigWrapper) c);
                if (c != element) {
                    item = element.getDisplayName();
                }
                break;
            }
        }
        CONFIGS_UTIL.showSettingsDialog(controller.getProject(), preselected, item);
    }
    
    @Override
    protected void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON3:
                break;
            case MouseEvent.BUTTON1:
                if (e.getClickCount() == 2) {
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
                okIfSelected();
                break;
            default:
        }
    }
    
    @Override
    protected void onSelected(BaseComposite element, TreePath path) {
        if (controller.nonViewMode()) {
            int x = controller.getCatalogPopup().getLocationOnScreen().x +
                    controller.getCatalogPopup().getContent().getSize().width;
            int y = tree.getLocationOnScreen().y + tree.getPathBounds(path).y;
            controller.getDescriptionPopup().moveToWith(x, y, element);
        } else {
            controller.getDescriptionPanel().setComposite(element);
        }
    }
    
    @Override
    protected String getDescription(BaseComposite element) {
        return CONFIGS_UTIL.getFullPath(element);
    }
    
    public void reload() {
        ((ConfigTreeStructure) treeStructure).root = CONFIGS_UTIL.collectConfigurables(controller.getProject());
        doRebuild();
    }
    
    private static class ConfigTreeStructure extends TreeStructureAdapter<BaseComposite> {
        static final BaseComposite[] EMPTY_ARRAY = new BaseComposite[0];
        ConfigWrapper root;
        
        public ConfigTreeStructure(ConfigWrapper root) {
            super(ConfigTreeStructure::getIcon, BaseComposite::getDisplayName);
            this.root = root;
        }
        
        static Icon getIcon(BaseComposite composite) {
            if (composite instanceof ConfigWrapper) {
                return ((ConfigWrapper) composite).isCollapsed() ? PluginIcons.GEAR_PLAIN : PluginIcons.GEAR_HOVER;
            } else if (composite instanceof Bag) {
                return null;
            } else {
                return PluginIcons.SETTINGS;
            }
        }
        
        @Override
        public @NotNull BaseComposite getRoot() {
            return root;
        }
        
        @Override
        public @NotNull BaseComposite[] getChildren(@NotNull BaseComposite element) {
            if (element instanceof ConfigWrapper) {
                ConfigWrapper wrapper = (ConfigWrapper) element;
                
                // 1.1
                BiConsumer<ArrayList<BaseComposite>, BaseContent> collectUnderBag = (theList, item) -> {
                    theList.add(item);
                    if (item instanceof Bag) {
                        theList.addAll(((Bag) item).getPoints());
                    }
                };
                // List<BaseComposite> list =
                //         wrapper.getContents().stream().collect(ArrayList::new, collectUnderBag, ArrayList::addAll);
                // list.addAll(wrapper.getKids());
                // return list.toArray(EMPTY_ARRAY);
                
                // 1.2
                Function<BaseContent, Stream<BaseContent>> mapUnderBag = item -> {
                    Stream<BaseContent> contentStream = Stream.of(item);
                    if (item instanceof Bag) {
                        contentStream = Stream.concat(contentStream, ((Bag) item).getPoints().stream());
                    }
                    return contentStream;
                };
                Stream<BaseContent> contentStream = wrapper.getContents().stream().flatMap(mapUnderBag);
                return Stream.concat(contentStream, wrapper.getKids().stream()).toArray(BaseComposite[]::new);
                
                // 2
                // return mergeListToArray(wrapper.getContents(), wrapper.getKids(), BaseComposite[].class);
            }
            if (element instanceof Bag) {
                // 1
                return ((Bag) element).getBags().toArray(EMPTY_ARRAY);
                
                // 2
                // return mergeListToArray(((Bag) element).getPoints(), ((Bag) element).getBags(), BaseComposite[].class);
            }
            return EMPTY_ARRAY;
        }
        
        @Override
        public @Nullable BaseComposite getParent(@NotNull BaseComposite element) {
            return element.getParent();
        }
        
        private static <E> E[] mergeListToArray(List<? extends E> list1, List<? extends E> list2, Class<E[]> newType) {
            Stream<E> concat = Stream.concat(list1.stream(), list2.stream());
            return streamToArray(concat, newType, list1.size() + list2.size());
        }
        
        private static <E> E[] streamToArray(Stream<E> stream, Class<E[]> newType, int length) {
            return stream.toArray(value -> (E[]) Array.newInstance(newType.getComponentType(), length));
        }
        
    }
    
    /**
     * map not effect at the popup level
     */
    private void addAdditionalListeners() {
        jbTextField.addKeyListener(new KeyAdapter() {
            /**
             * @see TreeUtil#installActions(JTree)
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isAltDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_PAGE_UP:
                            TreeUtil.movePageUp(tree);
                            break;
                        case KeyEvent.VK_PAGE_DOWN:
                            TreeUtil.movePageDown(tree);
                            break;
                        case KeyEvent.VK_LEFT:
                            for (TreePath each : tree.getSelectionPaths()) {
                                tree.collapsePath(each);
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            for (TreePath each : tree.getSelectionPaths()) {
                                tree.expandPath(each);
                            }
                            break;
                        default:
                            return;
                    }
                }
                e.consume();
            }
        });
    }
    
    public void reselect() {
        if (lastSelectedRow == -1) {
            TreeUtil.selectFirstNode(tree);
        } else {
            // TreePath isn't effect
            TreeUtil.selectRow(tree, lastSelectedRow);
        }
    }
    
}
