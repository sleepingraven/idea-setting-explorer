package carry.common;

import com.intellij.openapi.util.text.StringUtil;
import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Carry
 * @date 2020/10/29
 */
public class CommonUtil {
    private static final String TO_ESCAPE;
    private static final List<String> FROM;
    private static final List<String> TO;
    
    static {
        TO_ESCAPE = "/\\:*?\"<>|+";
        FROM = TO_ESCAPE.chars().mapToObj(ch -> Character.toString((char) ch)).collect(Collectors.toList());
        TO = FROM.stream().map(CommonUtil::encodeInAscii).collect(Collectors.toList());
    }
    
    /**
     * encode file name to escape "/" and "\\"
     */
    public static String encode(String fileName) {
        return StringUtil.replace(fileName, FROM, TO);
    }
    
    /**
     * &#32; → [!32];. Or the page will be empty.
     */
    private static String encodeInAscii(String toEscape) {
        return String.format("[!%2d];", (int) toEscape.charAt(0));
    }
    
    /**
     * This causes the method "setPage" not effect. Support "/\:*?"<>|+" now.
     */
    @SneakyThrows
    private static String encodeInUrl(String toEscape) {
        if ("*".equals(toEscape)) {
            return "%2A";
        }
        return URLEncoder.encode(toEscape, "UTF-8");
    }
    
}
