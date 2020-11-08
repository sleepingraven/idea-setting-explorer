package carry.common.data.source;

import carry.common.data.AppPersistence.ContentsWrapper;
import carry.common.data.AppSourceData;
import carry.common.entity.Detail;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;

/**
 * @author Carry
 * @date 2020/8/22
 */
public class FlattenParser extends SourceDataParser {
    
    public FlattenParser() throws JDOMException, IOException {
    }
    
    @Override
    public void loadSourceData() throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = saxBuilder.build(
                AppSourceData.class.getClassLoader().getResourceAsStream("/META-INF/app-source-data.xml"));
        Element root = doc.getRootElement();
        elemNs = root.getNamespace();
        Element component = root.getChild("component", elemNs);
        
        Element configMap = component.getChild("configMap", elemNs);
        for (Element entry : configMap.getChildren("entry", elemNs)) {
            String key = entry.getAttributeValue("configId", attrNs);
            Detail detail = parseDetail(entry.getChild("Detail", elemNs));
            getConfigMap().put(key, detail);
        }
        
        Element contentMap = component.getChild("contentMap", elemNs);
        for (Element entry : contentMap.getChildren("entry", elemNs)) {
            String key = entry.getAttributeValue("configId", attrNs);
            ContentsWrapper wrapper =
                    ContentsWrapper.builder().contents(parseContents(entry.getChild("contents", elemNs))).build();
            getContentMap().put(key, wrapper);
        }
    }
    
}
