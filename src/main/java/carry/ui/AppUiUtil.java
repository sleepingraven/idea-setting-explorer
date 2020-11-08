package carry.ui;

import carry.common.ConfigsUtil;
import carry.common.entity.BaseComposite;
import carry.common.entity.BaseContent;
import carry.common.entity.ConfigWrapper;
import carry.common.visitors.ApplicationServiceAccessor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.JBColor;
import com.intellij.ui.RelativeFont;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.speedSearch.FilteringListModel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.JBUI.Borders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Carry
 * @date 2020/7/30
 */
public class AppUiUtil implements ApplicationServiceAccessor {
    /* ui helper */
    
    final Function<String, String> directText = tag -> tag;
    final BiFunction<String, Font, Component> labelTagComponent = (text, font) -> {
        // TextIcon textIcon = new TextIcon(tag, JBUI.CurrentTheme.Label.foreground(), JBColor.green, 1);
        // textIcon.setFont(RelativeFont.SMALL.derive(getFont()));
        // return new JLabel(textIcon);
        
        JBLabel jbLabel = new JBLabel(text);
        jbLabel.setOpaque(true);
        jbLabel.setBackground(JBColor.green);
        jbLabel.setBorder(Borders.customLine(jbLabel.getBackground(), 0, 2, 0, 2));
        jbLabel.setFont(RelativeFont.SMALL.derive(font));
        return jbLabel;
    };
    
    public JBPanel<JBPanel<?>> getTagPanel(BaseComposite val, Font font) {
        return getTagPanel(val, font, null, null);
    }
    
    public JBPanel<JBPanel<?>> getTagPanel(BaseComposite val, Font font, @Nullable Function<String, String> text,
            @Nullable BiFunction<String, Font, Component> comp) {
        if (text == null) {
            text = directText;
        }
        if (comp == null) {
            comp = labelTagComponent;
        }
        
        JBPanel<JBPanel<?>> tagPanel = new JBPanel<>(new FlowLayout());
        tagPanel.setOpaque(false);
        if (val.getDetail() != null) {
            for (String tag : val.getDetail().getTags()) {
                Component component = comp.apply(text.apply(tag), font);
                tagPanel.add(component);
            }
        }
        return tagPanel;
    }
    
    public void doExpandOrCollapseConfig(JBList<BaseComposite> jbList) {
        BaseComposite selectedValue = jbList.getSelectedValue();
        if (selectedValue instanceof ConfigWrapper) {
            FilteringListModel<BaseComposite> model = (FilteringListModel<BaseComposite>) jbList.getModel();
            CollectionListModel<BaseComposite> originalModel =
                    (CollectionListModel<BaseComposite>) model.getOriginalModel();
            
            ConfigWrapper wrapper = (ConfigWrapper) selectedValue;
            final int postBegin = jbList.getSelectedIndex() + 1;
            if (!wrapper.isCollapsed()) {
                // do collapse
                int j = postBegin;
                while (j < originalModel.getSize() &&
                       originalModel.getElementAt(j).getLevel() > selectedValue.getLevel()) {
                    j++;
                }
                if (--j >= postBegin) {
                    originalModel.removeRange(postBegin, j);
                }
                wrapper.setCollapsed(true);
            } else {
                // do expand
                List<BaseComposite> composites = new ArrayList<>();
                for (BaseContent content : wrapper.getContents()) {
                    CONFIGS_UTIL.preorderContent(content, composites::add);
                }
                expandInPreorder(wrapper, composites);
                (originalModel).addAll(postBegin, composites);
                wrapper.setCollapsed(false);
            }
            jbList.setSelectedIndex(postBegin - 1);
        }
    }
    
    /**
     * @see ConfigsUtil#expandInPreorder(ConfigWrapper, List)
     */
    private void expandInPreorder(ConfigWrapper myRoot, List<BaseComposite> composites) {
        for (ConfigWrapper kid : myRoot.getKids()) {
            composites.add(kid);
            if (!kid.isCollapsed()) {
                for (BaseContent content : kid.getContents()) {
                    CONFIGS_UTIL.preorderContent(content, composites::add);
                }
                expandInPreorder(kid, composites);
            }
        }
    }
    
    /* ui util */
    
    public void popupToFront(JBPopup jbPopup) {
        JWindow jWindow = getHeavyWeightWindow(jbPopup);
        jWindow.toFront();
    }
    
    public void popupToBack(JBPopup jbPopup) {
        JWindow jWindow = getHeavyWeightWindow(jbPopup);
        jWindow.toBack();
    }
    
    /**
     * the popup should showing
     */
    public JWindow getHeavyWeightWindow(JBPopup jbPopup) {
        Container parent = jbPopup.getContent().getParent();
        JLayeredPane jLayeredPane = (JLayeredPane) parent.getParent();
        return (JWindow) jLayeredPane.getRootPane().getParent();
    }
    
    /* layout helper */
    
    @NotNull
    public GridBagConstraints newDefaultGbc(int x, int y) {
        return newDefaultGbc(x, y, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }
    
    @NotNull
    public GridBagConstraints newDefaultGbc(int x, int y, int anchor, int fill) {
        return new GridBagConstraints(x, y, 1, 1, 0.0, 0.0, anchor, fill, JBUI.emptyInsets(), 0, 0);
    }
    
    public GridBagLayout createGridBagLayout(boolean[] vGrow, boolean[] hGrow) {
        return createGridBagLayout(vGrow, hGrow, new int[hGrow.length], new int[vGrow.length]);
    }
    
    public GridBagLayout createGridBagLayout(boolean[] vGrow, boolean[] hGrow, int[] columnWidths, int[] rowHeights) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = columnWidths;
        gridBagLayout.rowHeights = rowHeights;
        gridBagLayout.columnWeights = getGblWeights(hGrow);
        gridBagLayout.rowWeights = getGblWeights(vGrow);
        return gridBagLayout;
    }
    
    private double[] getGblWeights(boolean[] grow) {
        double[] w = new double[grow.length];
        for (int i = 0; i < grow.length; i++) {
            if (grow[i]) {
                w[i] = 1;
            }
        }
        return w;
    }
    
}
