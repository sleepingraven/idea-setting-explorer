package carry.common.entity;

import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.XCollection;
import com.intellij.util.xmlb.annotations.XCollection.Style;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carry
 * @date 2020/8/7
 */
@SuperBuilder
@NoArgsConstructor
@Getter
public class Bag extends BaseContent {
    @Property(surroundWithTag = false)
    @XCollection(propertyElementName = "points", style = Style.v2)
    private final List<Point> points = new ArrayList<>();
    @Property(surroundWithTag = false)
    @XCollection(propertyElementName = "bags", style = Style.v2)
    private final List<Bag> bags = new ArrayList<>();
    
    @Override
    public boolean isLeaf() {
        return bags.isEmpty();
    }
    
}
