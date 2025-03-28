package life.chat_ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class OcrResponseDTO {
    private OcrResult result;

    @Data
    public static class OcrResult {
        private int errcode;
        private int height;
        private String imgpath;
        private List<OcrItem> ocr_response;
        private int width;

        @Data
        public static class OcrItem {
            private double bottom;
            private double left;
            private double rate;
            private double right;
            private String text;
            private double top;
        }
    }
}

