package carry.ui.tree.expansion;

import carry.common.entity.BaseComposite;
import carry.common.entity.ConfigWrapper;
import com.intellij.ui.tree.TreeVisitor;
import lombok.experimental.SuperBuilder;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/**
 * not finished
 *
 * @author Carry
 * @date 2020/8/17
 */
@Deprecated
@SuperBuilder
public class ByListener extends ExpansionRestore {
    
    @Override
    public void addRestorerListener() {
        final TreePath[] expandedPath = { null };
        catalogPanel.tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                TreePath expansionPath = event.getPath();
                BaseComposite exem = catalogPanel.getTreeElement(expansionPath);
                ((ConfigWrapper) exem).setCollapsed(false);
                
                // recover sub nodes
                final boolean[] pathMatched = { false };
                catalogPanel.asyncTreeModel.accept(path -> {
                    BaseComposite elem = catalogPanel.getTreeElement(path);
                    if (path.getPathCount() <= expansionPath.getPathCount()) {
                        if (path.isDescendant(expansionPath)) {
                            return TreeVisitor.Action.CONTINUE;
                        } else {
                            if (pathMatched[0]) {
                                // if (myLoadingDecorator.isLoading()) {
                                //     myLoadingDecorator.stopLoading();
                                // }
                                return TreeVisitor.Action.INTERRUPT;
                            } else {
                                return TreeVisitor.Action.SKIP_CHILDREN;
                            }
                        }
                    } else {
                        pathMatched[0] = true;
                        if (path.getPathCount() > expansionPath.getPathCount() + 1) {
                            return TreeVisitor.Action.SKIP_SIBLINGS;
                        }
                        boolean skip = !(elem instanceof ConfigWrapper) || ((ConfigWrapper) elem).isCollapsed();
                        if (skip) {
                            return TreeVisitor.Action.SKIP_CHILDREN;
                        } else {
                            catalogPanel.tree.expandPath(path);
                            return TreeVisitor.Action.CONTINUE;
                        }
                    }
                }, true);
                // TreeUtil.promiseExpand(tree, Stream.of());
            }
            
            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                TreePath path = event.getPath();
                if (path.equals(expandedPath[0])) {
                    ((ConfigWrapper) catalogPanel.getTreeElement(path)).setCollapsed(true);
                }
            }
        });
        // prevent sub nodes collapse after a node collapse
        // there is a bug when collapsed two nodes manually
        catalogPanel.tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
            }
            
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                expandedPath[0] = event.getPath();
            }
        });
    }
    
    /**
     * only recover top level
     */
    @Override
    protected TreeVisitor.Action doRecover(BaseComposite element) {
        if (element.getLevel() > 1) {
            return TreeVisitor.Action.SKIP_SIBLINGS;
        }
        return super.doRecover(element);
    }
    
}
