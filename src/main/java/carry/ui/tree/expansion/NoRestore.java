package carry.ui.tree.expansion;

import com.intellij.util.ui.UIUtil;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;

/**
 * @author Carry
 * @date 2020/8/18
 */
@SuperBuilder
public class NoRestore extends ExpansionRestore {
    
    @Override
    public void addRestorerListener() {
    }
    
    @Override
    public void recover(Consumer<Object> processed) {
        UIUtil.invokeLaterIfNeeded(() -> processed.accept(null));
    }
    
}
