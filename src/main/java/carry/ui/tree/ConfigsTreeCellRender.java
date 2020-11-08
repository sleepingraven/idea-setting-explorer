package carry.ui.tree;

import carry.common.entity.BaseComposite;
import carry.common.entity.ConfigWrapper;
import carry.common.entity.Point;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.tree.matcher.ConfigsFilteredCache;
import carry.ui.tree.matcher.DisplayNameFiltered;
import carry.ui.tree.matcher.Filtered;
import carry.ui.tree.matcher.TagFiltered;
import com.intellij.ide.structureView.newStructureView.StructureViewComponent;
import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.*;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI.Borders;
import icons.PluginIcons;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * If extends {@link CellRendererPanel}, the h-scroll and the complete tooltip will be incomplete.
 *
 * @author Carry
 * @date 2020/8/18
 */
public class ConfigsTreeCellRender extends JBPanel<ConfigsTreeCellRender> implements TreeCellRenderer, ApplicationServiceAccessor {
    private static final SimpleTextAttributes MATCHED =
            new SimpleTextAttributes(SimpleTextAttributes.STYLE_SEARCH_MATCH, null);
    private static final SimpleTextAttributes MATCHED_TAG =
            new SimpleTextAttributes(SimpleTextAttributes.STYLE_SEARCH_MATCH | SimpleTextAttributes.STYLE_SMALLER,
                                     null);
    private static final SimpleTextAttributes UNMATCH_TAG =
            new SimpleTextAttributes(SimpleTextAttributes.STYLE_SMALLER, null);
    
    private final NodeRenderer origin;
    private final JBInsets insetsNormal;
    private final JBInsets insetsEmpty;
    
    private final ConfigsFilteredCache filteredCache;
    private final Supplier<String> term;
    
    public ConfigsTreeCellRender(NodeRenderer origin, ConfigsFilteredCache filteredCache, Supplier<String> term) {
        this.origin = origin;
        this.filteredCache = filteredCache;
        this.term = term;
        
        insetsNormal = JBInsets.create(origin.getIpad());
        insetsNormal.right += 8;
        insetsEmpty = JBInsets.create(origin.getIpad());
        insetsEmpty.left = insetsEmpty.right = 0;
        
        setBorder(Borders.emptyRight(4));
        setLayout(new BorderLayout());
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        BaseComposite val = (BaseComposite) StructureViewComponent.unwrapNavigatable(value);
        
        Component comp = origin.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        Insets insets = insetsNormal;
        Icon icon = null;
        if (val instanceof ConfigWrapper) {
            icon = ((ConfigWrapper) val).isCollapsed() ? PluginIcons.GEAR_PLAIN : PluginIcons.GEAR_HOVER;
        } else if (val instanceof Point) {
            icon = PluginIcons.SETTINGS;
        } else {
            insets = insetsEmpty;
            comp = titleSeparator.set(comp);
        }
        JBPanel<JBPanel<?>> tagPanel = highlight(val, term.get());
        // set icon after clear
        origin.setIcon(icon);
        origin.setIpad(insets);
        
        removeAll();
        add(comp, BorderLayout.CENTER);
        add(tagPanel, BorderLayout.EAST);
        
        return this;
    }
    
    private JBPanel<JBPanel<?>> highlight(BaseComposite composite, String pattern) {
        Function<String, String> text = null;
        BiFunction<String, Font, Component> comp = null;
        if (filteredCache.cache.containsKey(composite)) {
            String displayName = composite.getDisplayName();
            Filtered filtered = filteredCache.cache.get(composite);
            if (filtered instanceof DisplayNameFiltered) {
                // for display name
                List<Integer> indexs = ((DisplayNameFiltered) filtered).getIndexs();
                origin.clear();
                
                int i = 0;
                int anchor = 0;
                while (i < indexs.size()) {
                    origin.append(displayName.substring(anchor, indexs.get(i)));
                    anchor = indexs.get(i);
                    while (i < indexs.size() - 1 && indexs.get(i) + 1 == indexs.get(i + 1)) {
                        i++;
                    }
                    origin.append(displayName.substring(anchor, indexs.get(i) + 1), MATCHED);
                    anchor = indexs.get(i) + 1;
                    i++;
                }
                origin.append(displayName.substring(anchor));
            } else {
                // for tag
                
                // 1.for label
                // int indexOf = ((TagFiltered) filtered).getIndexOf();
                // JBColor jbColor = JBColor.namedColor("SpeedSearch.foreground", UIUtil.getToolTipForeground());
                // text = tag -> {
                //     if (!tag.equals(((TagFiltered) filtered).getTag())) {
                //         return tag;
                //     }
                //     String head = tag.substring(0, indexOf);
                //     String tail = tag.substring(indexOf + pattern.length());
                //     return String.format("<HTML>%s<font color='#%s'>%s</font>%s</html>", head, jbColor.getRGB(),
                //                          pattern, tail);
                // };
                
                // 2.for colored
                comp = tagCreator.by((TagFiltered) filtered, pattern);
            }
        }
        return UI_UTIL.getTagPanel(composite, origin.getFont(), text, comp);
    }
    
    /**/
    
    private final MyTitledSeparator titleSeparator = new MyTitledSeparator();
    
    public static class MyTitledSeparator extends TitledSeparator implements Observer, Disposable {
        private final GridBagLayout myLayout;
        
        private final GridBagConstraints titleConstraints;
        private Component myTitle;
        
        private final GridBagConstraints sourceConstraints;
        private final GridBagConstraints overriddenConstraints;
        
        public MyTitledSeparator(String text, Disposable parent) {
            this();
            setText(text);
            Disposer.register(parent, this);
        }
        
        MyTitledSeparator() {
            myLayout = ((GridBagLayout) getLayout());
            titleConstraints = myLayout.getConstraints(myLabel);
            myTitle = myLabel;
            sourceConstraints = myLayout.getConstraints(mySeparator);
            overriddenConstraints = myLayout.getConstraints(mySeparator);
            overriddenConstraints.fill = GridBagConstraints.BOTH;
            
            setBorder(Borders.empty());
            adjust();
            MY_TITLED_SEPARATOR_OBSERVABLE.addObserver(this);
        }
        
        MyTitledSeparator set(Component title) {
            remove(myTitle);
            add(title, titleConstraints);
            myTitle = title;
            return this;
        }
        
        public static void fireAdjustAll() {
            MY_TITLED_SEPARATOR_OBSERVABLE.fireAdjustAll();
        }
        
        private void adjust() {
            adjust(SETTINGS_STATE.getSeparatorTopPosition());
        }
        
        public void adjust(int top) {
            if (top == 0) {
                myLayout.setConstraints(mySeparator, sourceConstraints);
            } else {
                overriddenConstraints.insets.top = top;
                myLayout.setConstraints(mySeparator, overriddenConstraints);
            }
            revalidate();
            repaint();
        }
        
        @Override
        public void dispose() {
            MY_TITLED_SEPARATOR_OBSERVABLE.deleteObserver(this);
        }
        
        @Override
        public void update(Observable o, Object top) {
            adjust();
        }
        
        private static final MyObservable MY_TITLED_SEPARATOR_OBSERVABLE = new MyObservable();
        
        private static class MyObservable extends Observable {
            
            public void fireAdjustAll() {
                setChanged();
                notifyObservers();
            }
            
        }
        
    }
    
    /**/
    
    private final MyTagCreator tagCreator = new MyTagCreator();
    
    private static class MyTagCreator implements BiFunction<String, Font, Component> {
        private final List<SimpleColoredComponent> sccs = new ArrayList<>();
        int i = 0;
        
        private TagFiltered tagFiltered;
        private String pattern;
        
        @Override
        public Component apply(String t, Font v) {
            if (i == sccs.size()) {
                SimpleColoredComponent scc = new SimpleColoredComponent();
                scc.setBackground(JBColor.GREEN);
                sccs.add(scc);
            }
            SimpleColoredComponent scc = sccs.get(i++);
            scc.clear();
            if (tagFiltered.getTag().equals(t)) {
                String head = t.substring(0, tagFiltered.getIndexOf());
                String tail = t.substring(tagFiltered.getIndexOf() + pattern.length());
                scc.append(head, UNMATCH_TAG);
                scc.append(pattern, MATCHED_TAG);
                scc.append(tail, UNMATCH_TAG);
            } else {
                scc.append(t, UNMATCH_TAG);
            }
            return scc;
        }
        
        public MyTagCreator by(TagFiltered tagFiltered, String pattern) {
            this.tagFiltered = tagFiltered;
            this.pattern = pattern;
            i = 0;
            return this;
        }
        
    }
    
}
