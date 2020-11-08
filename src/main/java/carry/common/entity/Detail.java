package carry.common.entity;

import carry.common.data.converters.ListWithStringConverter;
import com.intellij.util.xmlb.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Carry
 * @date 2020/8/7
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Detail {
    // not used now
    @Attribute
    private String description;
    @Builder.Default
    @Attribute(converter = ListWithStringConverter.class)
    private List<String> tags = new ArrayList<>();
    
    public Detail addTags(String... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }
    
}
