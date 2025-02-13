package life.chat_ai.dto;

import lombok.Data;

import java.util.List;


@Data
public class ChatRequestDTO {
    private String model;

    private List<ReqMessage> messages;

    private Boolean stream;

    private StreamOptions stream_options;

    @Data
    public static class ReqMessage {
        private String role;
        private String content;
    }

    @Data
    public static class StreamOptions {
        private Boolean include_usage;
    }

}
