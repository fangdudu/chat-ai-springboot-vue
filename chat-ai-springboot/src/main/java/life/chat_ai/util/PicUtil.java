package life.chat_ai.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PicUtil {
    /**
     * 文件转换为 Base64 字符串
     * @param file
     * @return
     * @throws IOException
     */
    public static String convertToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        return "data:image/jpeg;base64,"+Base64.encodeBase64String(fileContent);
    }

    /**
     * 判断文件是否为 jpg 格式
     * @param file
     * @return
     */
    public static boolean isJpgFile(MultipartFile file) {
        return file.getContentType() != null && file.getContentType().equalsIgnoreCase("image/jpeg");
    }
}
