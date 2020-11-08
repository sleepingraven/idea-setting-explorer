package carry.common.entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Carry
 * @date 2020/8/7
 */
@SuperBuilder
@NoArgsConstructor
public class Point extends BaseContent {
    
    @Override
    public boolean isLeaf() {
        return true;
    }
    
}
