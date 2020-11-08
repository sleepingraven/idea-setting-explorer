package admin.utils;

import carry.common.AppConstants;
import carry.common.CommonUtil;
import carry.common.entity.Bag;
import carry.common.entity.BaseComposite;
import carry.common.entity.ConfigWrapper;
import lombok.Cleanup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class ResourceUtil {
    private static final URL EMPTY = ResourceUtil.class.getClassLoader().getResource("/utils/empty.html");
    public static final String BASE_PATH = "F:\\idea-setting-explorer-temp-directory\\";
    
    public static void generateResource(ConfigWrapper myRoot, String basePath, boolean doc,
            BiConsumer<String, Boolean> callback) {
        boolean state = true;
        try {
            byte[] bs = doc ? read() : null;
            if (!basePath.endsWith("\\") && !basePath.endsWith("/")) {
                basePath += "\\";
            }
            write(myRoot, basePath, bs);
        } catch (IOException e) {
            e.printStackTrace();
            state = false;
        }
        callback.accept(basePath, state);
    }
    
    private static byte[] read() throws IOException {
        @Cleanup
        InputStream is = EMPTY.openStream();
        byte[] bs = new byte[is.available()];
        is.read(bs);
        return bs;
    }
    
    private static void write(BaseComposite myRoot, final String basePath, final byte[] bs) throws IOException {
        String displayName = CommonUtil.encode(myRoot.getDisplayName());
        String myDir = basePath + displayName + "\\";
        
        File file = new File(myDir);
        file.mkdirs();
        
        if (bs != null) {
            file = new File(myDir + "\\" + AppConstants.DESC_DOC_NAME);
            file.createNewFile();
            @Cleanup
            FileOutputStream os = new FileOutputStream(file);
            os.write(bs);
        }
        
        Consumer<BaseComposite> configWrapperConsumer = c -> {
            try {
                write(c, myDir, bs);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
        if (myRoot instanceof ConfigWrapper) {
            ((ConfigWrapper) myRoot).getKids().forEach(configWrapperConsumer);
            ((ConfigWrapper) myRoot).getContents().forEach(configWrapperConsumer);
        } else if (myRoot instanceof Bag) {
            ((Bag) myRoot).getBags().forEach(configWrapperConsumer);
            ((Bag) myRoot).getPoints().forEach(configWrapperConsumer);
        }
    }
    
}
