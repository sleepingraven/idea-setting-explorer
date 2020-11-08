package carry.ui.tree.expansion;

import carry.common.entity.BaseComposite;
import carry.common.entity.ConfigWrapper;
import carry.ui.tree.SearchTreeMainPanel;
import com.intellij.ui.tree.TreeVisitor;
import com.intellij.util.ui.tree.TreeUtil;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Carry
 * @date 2020/8/17
 */
@SuperBuilder
public abstract class ExpansionRestore {
    protected final SearchTreeMainPanel<BaseComposite> catalogPanel;
    
    public abstract void addRestorerListener();
    
    /**
     * recover all groups
     */
    public void recover(Consumer<Object> processed) {
        TreeUtil.promiseExpand(catalogPanel.tree, Stream.of(path -> {
            BaseComposite element = catalogPanel.getTreeElement(path);
            return doRecover(element);
        })).onProcessed(processed);
    }
    
    protected TreeVisitor.Action doRecover(BaseComposite element) {
        boolean skip = !(element instanceof ConfigWrapper) || ((ConfigWrapper) element).isCollapsed();
        return skip ? TreeVisitor.Action.SKIP_CHILDREN : TreeVisitor.Action.CONTINUE;
    }
    
}
