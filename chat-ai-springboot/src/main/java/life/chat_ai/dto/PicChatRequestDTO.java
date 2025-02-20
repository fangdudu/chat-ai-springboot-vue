package life.chat_ai.dto;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Data
public class PicChatRequestDTO {
    private String model;

    private List<ReqMessage> messages;

    private Boolean stream;

    private StreamOptions stream_options;

    @Data
    public static class ReqMessage {
        private String role;
        private List<Content> content;
    }

    @Data
    public static class StreamOptions {
        private Boolean include_usage;
    }

    @Data
    public static class Content {
        private String type;
        private String text;
        private ImageURL image_url;
    }

    @Data
    public static class ImageURL {
        private String url;
    }
}
