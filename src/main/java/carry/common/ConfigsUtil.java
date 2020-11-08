package carry.common;

import carry.common.data.AppPersistence.ContentsWrapper;
import carry.common.entity.*;
import carry.common.visitors.ApplicationServiceAccessor;
import com.intellij.ide.actions.ShowSettingsAction;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereManager;
import com.intellij.ide.ui.search.SearchUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableGroup;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.options.ex.ConfigurableExtensionPointUtil;
import com.intellij.openapi.options.newEditor.SettingsDialogFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Carry
 * @date 2020/8/2
 */
@Getter
public class ConfigsUtil implements ApplicationServiceAccessor {
    
    /* setting display */
    
    /**
     * cannot reuse the group, must create a new group
     */
    public void showSettingsDialog(@NotNull Project project, @NotNull BaseComposite composite, @Nullable String text) {
        ConfigurableGroup group = ConfigurableExtensionPointUtil.getConfigurableGroup(project, true);
        List<ConfigurableGroup> groups =
                group.getConfigurables().length == 0 ? Collections.emptyList() : Collections.singletonList(group);
        Configurable preselectedConfigurable = findPreselected(composite, group);
        SettingsDialogFactory.getInstance().create(project, groups, preselectedConfigurable, text).show();
    }
    
    /**
     * combines several methods of ShowSettingsUtilImpl
     *
     * @see ShowSettingsAction#perform(Project)
     * @see com.intellij.ide.actions.ShowSettingsUtilImpl#showSettingsDialog(Project, String)
     * @see com.intellij.ide.actions.ShowSettingsUtilImpl#showSettingsDialog(Project, String, String)
     */
    public void showSettingsDialog(@NotNull Project project, @NotNull String preselected, @Nullable String text) {
        ConfigurableGroup group = ConfigurableExtensionPointUtil.getConfigurableGroup(project, true);
        List<ConfigurableGroup> groups =
                group.getConfigurables().length == 0 ? Collections.emptyList() : Collections.singletonList(group);
        Configurable preselectedConfigurable = findPreselected(preselected, groups);
        SettingsDialogFactory.getInstance().create(project, groups, preselectedConfigurable, text).show();
    }
    
    /**
     * without tree, but isn't appropriate for those like "Color Scheme Font"'s "default"
     */
    public void editConfigurable(@Nullable Project project, @NotNull String preselected) {
        ConfigurableGroup group = ConfigurableExtensionPointUtil.getConfigurableGroup(project, true);
        List<ConfigurableGroup> groups =
                group.getConfigurables().length == 0 ? Collections.emptyList() : Collections.singletonList(group);
        Configurable preselectedConfigurable = findPreselected(preselected, groups);
        ShowSettingsUtil.getInstance().editConfigurable(project, preselectedConfigurable);
    }
    
    /**
     * to be finished
     * <p>
     * from debugging {@link com.intellij.ide.util.gotoByName.GotoActionModel}
     *
     * @see com.intellij.ide.actions.searcheverywhere.ActionSearchEverywhereContributor
     * @see com.intellij.ide.actions.searcheverywhere.SearchEverywhereManagerImpl
     * @see com.intellij.ide.actions.SearchEverywhereAction
     */
    public void toSearchEverywhereActions(@Nullable Project project, @Nullable String text, @NotNull AnActionEvent e) {
        SearchEverywhereManager.getInstance(project).show("ActionSearchEverywhereContributor", text, e);
    }
    
    /**
     * comes from {@link com.intellij.ide.actions.ShowSettingsUtilImpl}'s method "findPreselectedByDisplayName"
     */
    private @Nullable Configurable findPreselected(@NotNull String preselected,
            @NotNull List<? extends ConfigurableGroup> groups) {
        for (ConfigurableGroup eachGroup : groups) {
            for (Configurable configurable : SearchUtil.expandGroup(eachGroup)) {
                if (preselected.equals(configurable.getDisplayName())) {
                    return configurable;
                }
            }
        }
        
        return null;
    }
    
    private @NotNull Configurable findPreselected(@NotNull BaseComposite composite, @NotNull ConfigurableGroup group) {
        Stack<BaseComposite> stack = new Stack<>();
        for (BaseComposite p = composite; p.getParent() != null; p = p.getParent()) {
            stack.push(p);
        }
        
        Configurable preselected = null;
        Object g = group;
        while (!stack.isEmpty()) {
            if (!(stack.peek() instanceof ConfigWrapper)) {
                break;
            }
            ConfigWrapper peek = (ConfigWrapper) stack.pop();
            for (Configurable configurable : ((Configurable.Composite) g).getConfigurables()) {
                if (configurable.getDisplayName().equals(peek.getDisplayName())) {
                    preselected = configurable;
                    g = configurable;
                    break;
                }
            }
        }
        
        return preselected;
    }
    
    /* collect configurables and reload memory */
    
    private final List<BaseComposite> composites = new ArrayList<>();
    
    public ConfigsUtil reloadConfigurables(Project project) {
        ConfigWrapper myRoot = collectConfigurables(project);
        composites.clear();
        expandInPreorder(myRoot, composites);
        return this;
    }
    
    /**
     * @see SearchUtil#expandGroup(ConfigurableGroup)
     */
    public ConfigWrapper collectConfigurables(Project project) {
        ConfigurableGroup group = ConfigurableExtensionPointUtil.getConfigurableGroup(project, true);
        ConfigWrapper myRoot = ConfigWrapper.builder()
                                            .configurable(null)
                                            .id("")
                                            .displayName("My Root")
                                            .level(0)
                                            .collapsed(false)
                                            .build();
        for (Configurable each : group.getConfigurables()) {
            buildInPreorder(each, myRoot);
        }
        return myRoot;
    }
    
    private void buildInPreorder(Configurable configurable, ConfigWrapper parent) {
        String displayName = configurable.getDisplayName();
        String id = concatConfigId(parent.getId(), displayName);
        Detail detail = PERSISTENCE.getConfigMap().get(id);
        ConfigWrapper wrapper = ConfigWrapper.builder()
                                             .configurable(configurable)
                                             .id(id)
                                             .displayName(displayName)
                                             .detail(detail)
                                             .level(parent.getLevel() + 1)
                                             .parent(parent)
                                             .build();
        parent.addKid(wrapper.addContents(getConfigContents(wrapper, id)));
        
        // leaf node
        if (!(configurable instanceof Configurable.Composite)) {
            return;
        }
        
        for (Configurable eachKid : ((Configurable.Composite) configurable).getConfigurables()) {
            buildInPreorder(eachKid, wrapper);
        }
    }
    
    public void expandInPreorder(ConfigWrapper myRoot, List<BaseComposite> composites) {
        for (ConfigWrapper kid : myRoot.getKids()) {
            composites.add(kid);
            for (BaseContent content : kid.getContents()) {
                preorderContent(content, composites::add);
            }
            expandInPreorder(kid, composites);
        }
    }
    
    public void preorderContent(BaseContent content, Consumer<BaseContent> consumer) {
        consumer.accept(content);
        if (content instanceof Bag) {
            for (Point point : ((Bag) content).getPoints()) {
                consumer.accept(point);
            }
            for (Bag bag : ((Bag) content).getBags()) {
                preorderContent(bag, consumer);
            }
        }
    }
    
    /* common */
    
    private List<BaseContent> getConfigContents(ConfigWrapper wrapper, String id) {
        ContentsWrapper contentsWrapper = PERSISTENCE.getContentMap().get(id);
        if (contentsWrapper == null) {
            return null;
        }
        
        List<BaseContent> contents = contentsWrapper.getContents();
        for (BaseContent content : contents) {
            // relocation
            preorderContent(content, wrapper, (con, par) -> {
                con.setId(concatContentId(par.getId(), con));
                con.setLevel((con instanceof Point && par instanceof Bag) ? par.getLevel() : par.getLevel() + 1);
                con.setParent(par);
            });
        }
        return contents;
    }
    
    private void preorderContent(BaseContent content, BaseComposite parent,
            BiConsumer<BaseContent, BaseComposite> consumer) {
        consumer.accept(content, parent);
        if (content instanceof Bag) {
            for (Point point : ((Bag) content).getPoints()) {
                consumer.accept(point, content);
            }
            for (Bag bag : ((Bag) content).getBags()) {
                preorderContent(bag, content, consumer);
            }
        }
    }
    
    /* ids */
    
    public static String concatConfigId(String path, String name) {
        return path.isEmpty() ? name : path + AppConstants.ID_SEPARATOR_CONFIG + name;
    }
    
    private static String concatContentId(String path, BaseContent content) {
        String delimiter = content instanceof Bag ? AppConstants.ID_SEPARATOR_BAG : AppConstants.ID_SEPARATOR_POINT;
        return path + delimiter + content.getDisplayName();
    }
    
    public static final List<String> ID_SEPARATORS =
            Arrays.asList(AppConstants.ID_SEPARATOR_CONFIG, AppConstants.ID_SEPARATOR_BAG,
                          AppConstants.ID_SEPARATOR_POINT);
    private static final List<String> PATH_SEPARATORS =
            Arrays.asList(AppConstants.PATH_SEPARATOR_CONFIG, AppConstants.PATH_SEPARATOR_BAG,
                          AppConstants.PATH_SEPARATOR_POINT);
    
    public String getFullPath(BaseComposite composite) {
        return StringUtil.replace(composite.getId(), ID_SEPARATORS, PATH_SEPARATORS);
    }
    
}
