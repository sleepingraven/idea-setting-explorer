package carry.common.data.source;

import carry.common.AppConstants;
import carry.common.data.AppPersistence.ContentsWrapper;
import carry.common.data.converters.ListWithStringConverter;
import carry.common.entity.Bag;
import carry.common.entity.BaseContent;
import carry.common.entity.Detail;
import carry.common.entity.Detail.DetailBuilder;
import carry.common.entity.Point;
import lombok.Getter;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Carry
 * @date 2020/8/22
 */

public abstract class SourceDataParser {
    @Getter
    private final Map<String, Detail> configMap = new HashMap<>(AppConstants.FLAT_CONFIGS_MAP_SIZE);
    @Getter
    private final Map<String, ContentsWrapper> contentMap = new HashMap<>(AppConstants.FLAT_CONFIGS_MAP_SIZE);
    
    private final ListWithStringConverter converter = new ListWithStringConverter();
    protected Namespace elemNs;
    protected Namespace attrNs;
    
    public SourceDataParser() throws JDOMException, IOException {
        loadSourceData();
    }
    
    public abstract void loadSourceData() throws JDOMException, IOException;
    
    protected Detail parseDetail(Element element) {
        DetailBuilder builder = Detail.builder();
        
        String description = element.getAttributeValue("description", attrNs);
        if (description != null) {
            builder.description(description);
        }
        String tags = element.getAttributeValue("tags", attrNs);
        if (tags != null) {
            builder.tags(converter.fromString(tags));
        }
        
        return builder.build();
    }
    
    protected List<BaseContent> parseContents(Element element) {
        List<BaseContent> contents = new ArrayList<>();
        preorderContents(element, contents, contents);
        return contents;
    }
    
    private void preorderContents(Element element, List<? super Point> points, List<? super Bag> bags) {
        Stream.concat(element.getChildren("Point", elemNs).stream(), element.getChildren("Bag", elemNs).stream())
              .forEach(content -> {
                  Detail detail = parseDetail(content);
                  String displayName = content.getAttributeValue("name", attrNs);
                  if ("Bag".equals(content.getName())) {
                      Bag bag = Bag.builder().displayName(displayName).detail(detail).build();
                      bags.add(bag);
                      preorderContents(content, bag.getPoints(), bag.getBags());
                  } else {
                      Point point = Point.builder().displayName(displayName).detail(detail).build();
                      points.add(point);
                  }
              });
    }
    
}
