package carry.ui.tree.expansion;

import carry.common.entity.ConfigWrapper;
import lombok.experimental.SuperBuilder;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;

/**
 * restore all on showing
 *
 * @author Carry
 * @date 2020/8/18
 */
@SuperBuilder
public class OnShowing extends ExpansionRestore {
    
    @Override
    public void addRestorerListener() {
        catalogPanel.tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                ((ConfigWrapper) catalogPanel.getTreeElement(event.getPath())).setCollapsed(false);
            }
            
            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                ((ConfigWrapper) catalogPanel.getTreeElement(event.getPath())).setCollapsed(true);
            }
        });
    }
    
}
