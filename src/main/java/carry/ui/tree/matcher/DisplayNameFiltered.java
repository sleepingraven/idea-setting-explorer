package carry.ui.tree.matcher;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Carry
 * @date 2020/8/18
 */
@AllArgsConstructor
@Getter
public class DisplayNameFiltered implements Filtered {
    private final List<Integer> indexs;
    
}
