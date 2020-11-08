package carry.common.entity;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Transient;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Carry
 * @date 2020/8/1
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseComposite {
    @Getter(onMethod_ = @Transient)
    @Setter
    @EqualsAndHashCode.Include
    private String id;
    
    @Attribute(value = "name")
    private String displayName;
    @Property(surroundWithTag = false, flat = true)
    private Detail detail;
    
    @Getter(onMethod_ = @Transient)
    @Setter
    private int level;
    @Getter(onMethod_ = @Transient)
    @Setter
    private BaseComposite parent;
    
    public abstract boolean isLeaf();
    
    @Override
    public String toString() {
        return displayName;
    }
    
}
