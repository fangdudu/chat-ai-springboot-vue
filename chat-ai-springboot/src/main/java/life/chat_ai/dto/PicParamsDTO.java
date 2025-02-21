package life.chat_ai.dto;

import lombok.Data;



import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class PicParamsDTO {
    private String messages;
    private List<MultipartFile> files;
    private List<String> base64List;
}