package life.chat_ai.dto;

import lombok.Data;



import org.springframework.http.codec.multipart.FilePart;
import java.util.List;
@Data
public class PicParamsDTO {
    private String messages;
    private List<FilePart> images; // 使用 FilePart 接收文件流
}