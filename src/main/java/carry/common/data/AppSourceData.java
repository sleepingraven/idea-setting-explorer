package carry.common.data;

import carry.common.data.AppPersistence.ContentsWrapper;
import carry.common.data.source.HierarchyAdapterParser;
import carry.common.data.source.SourceDataParser;
import carry.common.entity.Detail;
import lombok.Getter;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Map;

/**
 * @author Carry
 * @date 2020/8/9
 */
@Getter
public class AppSourceData {
    private final Map<String, Detail> configMap;
    private final Map<String, ContentsWrapper> contentMap;
    
    private final SourceDataParser parser;
    
    public AppSourceData() throws JDOMException, IOException {
        // parser = new FlattenParser();
        parser = new HierarchyAdapterParser();
        configMap = parser.getConfigMap();
        contentMap = parser.getContentMap();
    }
    
}
