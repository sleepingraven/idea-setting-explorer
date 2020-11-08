package carry.actions.base;

import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;

import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * @author Carry
 * @date 2020/8/28
 */
public class TreeActionsInstaller {
    
    public static void install(Tree tree, JComponent component, boolean moveSelection, boolean moveSelectionThroughPage,
            boolean expansion) {
        if (moveSelection) {
            MyShortcuts.register(MyShortcuts.UP, component, (e) -> TreeUtil.moveUp(tree), false);
            MyShortcuts.register(MyShortcuts.DOWN, component, (e) -> TreeUtil.moveDown(tree), false);
        }
        if (moveSelectionThroughPage) {
            MyShortcuts.register(MyShortcuts.PAGE_UP, component, (e) -> TreeUtil.movePageUp(tree), false);
            MyShortcuts.register(MyShortcuts.PAGE_DOWN, component, (e) -> TreeUtil.movePageDown(tree), false);
        }
        if (expansion) {
            MyShortcuts.register(MyShortcuts.LEFT, component, (e) -> collapseSelection(tree), false);
            MyShortcuts.register(MyShortcuts.RIGHT, component, (e) -> expandSelection(tree), false);
        }
    }
    
    private static void collapseSelection(Tree tree) {
        for (TreePath each : tree.getSelectionPaths()) {
            tree.collapsePath(each);
        }
    }
    
    private static void expandSelection(Tree tree) {
        for (TreePath each : tree.getSelectionPaths()) {
            tree.expandPath(each);
        }
    }
    
}
