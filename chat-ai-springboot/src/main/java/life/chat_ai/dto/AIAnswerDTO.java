package life.chat_ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIAnswerDTO {
    /**
     * 对话id 例如：chatcmpl-c16bf13f-d55a-93c6-9f27-258cc24805e2
     */
    private String id;
    /**
     * 聊天object 例如：chat.completion.chunk
     */
    private String object;
    /**
     * 创建时间戳 例如：1739256224
     */
    private int created;
    /**
     * 使用的模型 例如：deepseek-r1
     */
    private String model;
    /**
     * 系统指纹
     */
    public String system_fingerprint;
    /***
     * 消耗的token相关信息
     */
    private AIAnswerUsageDTO usage;
    /**
     * 回答内容
     */
    private List<ChoicesDTO> choices = new ArrayList<>();
    /**
     * 回答的内容
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChoicesDTO {
        /**
         * 数据索引
         */
        public int index;
        /**
         * 角色以及回答的内容
         */
        public Delta delta;
        /**
         * 完成原因
         */
        private String finish_reason;
        /**
         * probs
         */
        private String logprobs;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Delta {
        /**
         * 角色
         */
        public String role;
        /**
         * 回复内容
         */
        public String content = "";
        /**
         * 思考过程
         */
        public String reasoning_content = "";
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AIAnswerUsageDTO {
        /**
         * 提示词消耗的token数
         */
        public int prompt_tokens;
        /**
         * 对话消耗的token数
         */
        public int completion_tokens;
        /**
         * 总token数
         */
        public int total_tokens;
    }
}
