package carry.ui.tree.matcher;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Carry
 * @date 2020/8/18
 */
@AllArgsConstructor
@Getter
public class TagFiltered implements Filtered {
    private final String tag;
    private final int indexOf;
    
}
