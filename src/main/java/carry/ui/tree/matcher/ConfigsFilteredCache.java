package carry.ui.tree.matcher;

import carry.common.AppConstants;
import carry.common.entity.BaseComposite;
import carry.common.entity.Detail;
import com.intellij.openapi.util.text.StringUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/8/18
 */
@RequiredArgsConstructor
public class ConfigsFilteredCache implements FilteredCache<BaseComposite> {
    public final Map<BaseComposite, Filtered> cache = new HashMap<>(AppConstants.ALL_ITEMS_MAP_SIZE);
    private final Supplier<String> term;
    private final BooleanSupplier caseSensitive;
    
    @Override
    public boolean match(BaseComposite composite) {
        String pattern = this.term.get();
        if (StringUtil.isEmpty(pattern)) {
            return true;
        }
        List<Integer> indexs = matchDisplayName(composite, pattern);
        if (indexs.size() == pattern.length()) {
            cache.put(composite, new DisplayNameFiltered(indexs));
            return true;
        }
        TagFiltered tagFiltered = matchTag(composite, pattern);
        if (tagFiltered != null) {
            cache.put(composite, tagFiltered);
            return true;
        }
        return false;
    }
    
    private List<Integer> matchDisplayName(BaseComposite composite, String pattern) {
        String text = composite.getDisplayName();
        if (!caseSensitive.getAsBoolean()) {
            text = text.toLowerCase();
        }
        
        List<Integer> indexs = new ArrayList<>(pattern.length());
        char[] cs = text.toCharArray();
        for (int i = 0, ip = 0; i < cs.length && ip < pattern.length(); i++) {
            if (cs[i] == pattern.charAt(ip)) {
                indexs.add(i);
                ip++;
            }
        }
        return indexs;
    }
    
    private TagFiltered matchTag(BaseComposite composite, String pattern) {
        Detail detail = composite.getDetail();
        if (detail != null) {
            for (String tag : detail.getTags()) {
                if (!caseSensitive.getAsBoolean()) {
                    tag = tag.toLowerCase();
                }
                int indexOf = tag.indexOf(pattern);
                if (indexOf != -1) {
                    return new TagFiltered(tag, indexOf);
                }
            }
        }
        return null;
    }
    
    @Override
    public void clear() {
        cache.clear();
    }
    
}
