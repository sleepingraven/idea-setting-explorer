package carry.common.entity;

import com.intellij.openapi.options.Configurable;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import com.intellij.util.xmlb.annotations.XCollection;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carry
 * @date 2020/8/4
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Tag(value = "Config")
public class ConfigWrapper extends BaseComposite {
    @Builder.Default
    private final Configurable configurable = null;
    @Property(surroundWithTag = false)
    @XCollection
    private final List<ConfigWrapper> kids = new ArrayList<>();
    @Property(surroundWithTag = false)
    @XCollection(elementTypes = { Bag.class, Point.class })
    private final List<BaseContent> contents = new ArrayList<>();
    
    @Builder.Default
    @Getter(onMethod_ = @Transient)
    @Setter
    private boolean collapsed = true;
    
    public ConfigWrapper addKid(ConfigWrapper kid) {
        kids.add(kid);
        return this;
    }
    
    public ConfigWrapper addContents(List<BaseContent> contents) {
        if (contents != null) {
            this.contents.addAll(contents);
        }
        return this;
    }
    
    @Override
    public boolean isLeaf() {
        return kids.isEmpty();
    }
    
}
