package carry.common.data;

import carry.common.AppConstants;
import carry.common.data.converters.DimensionConverter;
import carry.common.data.converters.PointConverter;
import carry.common.entity.Bag;
import carry.common.entity.BaseContent;
import carry.common.entity.ConfigWrapper;
import carry.common.entity.Detail;
import carry.common.visitors.SettingsStateAccessor;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.ui.ScreenUtil;
import com.intellij.util.xmlb.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jdom.JDOMException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Carry
 * @date 2020/8/6
 */
@NoArgsConstructor
@State(name = AppConstants.PERSISTENCE_ID, storages = @Storage(AppConstants.PERSISTENCE_STORAGE))
public class AppPersistence extends StorableState<AppPersistence> implements SettingsStateAccessor {
    @Tag
    private String version = "";
    
    /* these states affect by all projects */
    
    @Getter
    @OptionTag(tag = "catalog-popup-size", converter = DimensionConverter.class)
    private final Dimension catalogPopupSize =
            new Dimension(AppConstants.CATALOG_POPUP_WIDTH, AppConstants.CATALOG_POPUP_HEIGHT);
    @Getter
    @OptionTag(tag = "description-popup-location", converter = PointConverter.class)
    private final Point descriptionPopupLocation = new Point();
    @Getter
    @OptionTag(tag = "view-mode-popup-size", converter = DimensionConverter.class)
    private final Dimension viewModePopupSize = ScreenUtil.getMainScreenBounds().getSize();
    
    public boolean pinLocation;
    public boolean activateCatalogNonViewMode;
    public boolean pinLocationNonViewMode;
    
    public int viewModeSearchTextFieldHeight;
    public String toolbarLocation = ToolbarLocation.TOP;
    public boolean toolbarVisible = true;
    
    public static class ToolbarLocation {
        public static final String TOP = BorderLayout.NORTH;
        public static final String BOTTOM = BorderLayout.SOUTH;
        
    }
    
    /* datum */
    
    @XMap(keyAttributeName = "configId")
    private Map<String, Detail> configMap;
    @XMap(keyAttributeName = "configId")
    private Map<String, ContentsWrapper> contentMap;
    @Tag
    private final ConfigWrapper COMPOSITES_TEMPLATE = null;
    
    @Transient
    public boolean refresh = SETTINGS_STATE.isRefreshOnStartup();
    
    private void reloadData() {
        try {
            switch (version) {
                case "0.0.1":
                case "0.0.2":
                case "0.0.3":
                case "0.0.4":
                case "0.1.0":
                case "0.1.1":
                case "0.2.0":
                default:
                    AppSourceData sourceData = new AppSourceData();
                    configMap = sourceData.getConfigMap();
                    contentMap = sourceData.getContentMap();
            }
            version = "0.2.0";
            refresh = false;
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Detail> getConfigMap() {
        if (refresh || configMap == null) {
            reloadData();
        }
        return configMap;
    }
    
    public Map<String, ContentsWrapper> getContentMap() {
        if (refresh || contentMap == null) {
            reloadData();
        }
        return contentMap;
    }
    
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Tag(value = "contents")
    public static class ContentsWrapper {
        @Builder.Default
        @Property(surroundWithTag = false)
        @XCollection(elementTypes = { Bag.class, carry.common.entity.Point.class })
        private final List<BaseContent> contents = new ArrayList<>();
        
    }
    
}
