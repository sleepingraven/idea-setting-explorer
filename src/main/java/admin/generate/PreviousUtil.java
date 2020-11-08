package admin.generate;

import carry.common.entity.ConfigWrapper;

/**
 * @author Carry
 * @date 2020/8/8
 */
public class PreviousUtil {
    private static final String[] INDETS = new String[] {
            "",
            "## ",
            "* ### ",
            "  * #### ",
            "    * ##### ",
            "      * ###### "
    };
    
    public String groupToString(ConfigWrapper configWrapper) {
        StringBuffer buffer = new StringBuffer();
        preorder(configWrapper, buffer);
        return buffer.toString();
    }
    
    private void preorder(ConfigWrapper configWrapper, StringBuffer buffer) {
        buffer.append(INDETS[configWrapper.getLevel()]).append(configWrapper.getDisplayName());
        buffer.append("\n").append("\n");
        for (ConfigWrapper kid : configWrapper.getKids()) {
            preorder(kid, buffer);
        }
    }
    
}
