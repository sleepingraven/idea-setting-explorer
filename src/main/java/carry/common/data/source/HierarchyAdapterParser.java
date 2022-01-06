package carry.common.data.source;

import carry.common.ConfigsUtil;
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
public class HierarchyAdapterParser extends SourceDataParser {
    
    public HierarchyAdapterParser() throws JDOMException, IOException {
    }
    
    @Override
    public void loadSourceData() throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = saxBuilder.build(
                AppSourceData.class.getResourceAsStream("/META-INF/app-source-data.xml"));
        Element root = doc.getRootElement();
        elemNs = root.getNamespace();
        Element component = root.getChild("component", elemNs);
        
        Element composites = component.getChild("composites", elemNs);
        Element rootConfig = composites.getChild("Config", elemNs);
        ofConfig(rootConfig, "");
    }
    
    private void ofConfig(Element element, String configPath) {
        ContentsWrapper wrapper = ContentsWrapper.builder().contents(parseContents(element)).build();
        if (!wrapper.getContents().isEmpty()) {
            getContentMap().put(configPath, wrapper);
        }
        for (Element each : element.getChildren("Config", elemNs)) {
            String displayName = each.getAttributeValue("name", attrNs);
            String id = ConfigsUtil.concatConfigId(configPath, displayName);
            Detail detail = parseDetail(each);
            // field "description" is not used
            // if (StringUtil.isNotEmpty(detail.getDescription()) && !detail.getTags().isEmpty()) {
            //     getConfigMap().put(id, detail);
            // }
            if (!detail.getTags().isEmpty()) {
                getConfigMap().put(id, detail);
            }
            ofConfig(each, id);
        }
    }
    
}
