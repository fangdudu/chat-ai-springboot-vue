package life.chat_ai.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.chat_ai.dto.AIAnswerDTO;
import life.chat_ai.dto.ChatRequestDTO;
import life.chat_ai.dto.PicChatRequestDTO;
import life.chat_ai.dto.PicParamsDTO;
import life.chat_ai.util.PicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;


@Service
public class GptServiceImpl {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String REDIS_CHAT_KEY_PREFIX = "CHAT_KEY:";

    // 阿里云
    @Value("${aliyun.bailian.ds.model}")
    public String ALIYUN_BAILIAN_MODEL;
    @Value("${aliyun.bailian.ds.api-url}")
    public String ALIYUN_BAILIAN_URL;
    @Value("${aliyun.bailian.ds.api-key}")
    public String ALIYUN_BAILIAN_API_KEY;

    // 字节火山
    @Value("${bytedance.volcengine.model}")
    public String BYTEDANCE_VOLCENGINE_MODEL;
    @Value("${bytedance.volcengine.api-url}")
    public String BYTEDANCE_VOLCENGINE_API_URL;
    @Value("${bytedance.volcengine.api-key}")
    public String BYTEDANCE_VOLCENGINE_API_KEY;

    // 硅基流动
    @Value("${siliconflow.model}")
    public String SILICONFLOW_MODEL;
    @Value("${siliconflow.api-url}")
    public String SILICONFLOW_API_URL;
    @Value("${siliconflow.api-key}")
    public String SILICONFLOW_API_KEY;

    @Value("${aliyun.bailian.qwen25.model}")
    public String QWEN_MODEL;
    @Value("${aliyun.bailian.qwen25.api-url}")
    public String QWEN_API_URL;
    @Value("${aliyun.bailian.qwen25.api-key}")
    public String QWEN_API_KEY;


    //webflux的client
    private WebClient webClient;

    //用于读取第三方的返回结果
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void postConstruct() {
        this.webClient = WebClient.builder()//创建webflux的client
                .defaultHeader("Content-Type", "application/json")//设置默认请求类型
                .build();
    }

    //请求stream的主题
    public Flux<AIAnswerDTO> doChatGPTStream(String requestQuestion) {
        //构建请求对象
        ChatRequestDTO chatRequestDTO = new ChatRequestDTO();
        //设置模型
        chatRequestDTO.setModel(SILICONFLOW_MODEL);
        //设置流式返回
        chatRequestDTO.setStream(true);
        ChatRequestDTO.StreamOptions streamOptions = new ChatRequestDTO.StreamOptions();
        streamOptions.setInclude_usage(true);
        chatRequestDTO.setStream_options(streamOptions);
        //设置请求消息，在此可以加入自己的prompt
        ChatRequestDTO.ReqMessage message = new ChatRequestDTO.ReqMessage();
        //用户消息
        message.setRole("user");
        //用户请求内容
        message.setContent(requestQuestion);
        ArrayList<ChatRequestDTO.ReqMessage> messages = new ArrayList<>();
        messages.add(message);
        //设置请求消息
        chatRequestDTO.setMessages(messages);


        //构建请求json
        String paramJson = JSONUtil.toJsonStr(chatRequestDTO);
        //使用webClient发送消息
        return this.webClient.post()
                //请求uri
                .uri(SILICONFLOW_API_URL)
                //设置成自己的key，获得key的方式可以在下文查看
                .header("Authorization", "Bearer " + SILICONFLOW_API_KEY)
                //.header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)//设置流式响应
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paramJson))
                .retrieve()
                .bodyToFlux(String.class)
                //接收到消息的处理方法
                .flatMap(result -> handleWebClientResponse(result));
    }

    private Flux<AIAnswerDTO> handleWebClientResponse(String resp) {
        //[DONE]是消息结束标识
        if (StrUtil.equals("[DONE]", resp)) {
            return Flux.empty();
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(resp);
            //将获得的结果转成对象
            AIAnswerDTO result = objectMapper.treeToValue(jsonNode, AIAnswerDTO.class);
/*            if (CollUtil.size(result.getChoices()) > 0 && !Objects.isNull(result.getChoices().get(0)) &&
                    //判断是否有异常
                    !StrUtil.isBlank(result.getChoices().get(0).delta.getError())) {
                throw new RuntimeException(result.getChoices().get(0).delta.getError());
            }*/
            //返回获得的结果
            return Flux.just(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getChatKeyByPicParams(PicParamsDTO picParamsDTO) throws IOException {
        List<MultipartFile> files = picParamsDTO.getFiles();
        LinkedList<String> base64List = new LinkedList<>();
        for (MultipartFile file : files) {
            if (PicUtil.isJpgFile(file)) {
                base64List.add(PicUtil.convertToBase64(file));
            }
        }
        picParamsDTO.setBase64List(base64List);
        picParamsDTO.setFiles(null);
        String paramJson = JSONUtil.toJsonStr(picParamsDTO);

        String chatKey = new Date().getTime() + ":" + UUID.randomUUID();
        stringRedisTemplate.opsForValue().set(REDIS_CHAT_KEY_PREFIX + chatKey, paramJson);
        return chatKey;
    }

    public Flux<AIAnswerDTO> doPicChatGPTStream(String chatKey) throws JsonProcessingException {

        String chatMsg = stringRedisTemplate.opsForValue().get(REDIS_CHAT_KEY_PREFIX + chatKey);

        PicParamsDTO param = objectMapper.readValue(chatMsg, PicParamsDTO.class);

        //构建请求对象
        PicChatRequestDTO picChatRequestDTO = new PicChatRequestDTO();
        //设置模型
        picChatRequestDTO.setModel(QWEN_MODEL);
        //设置流式返回
        picChatRequestDTO.setStream(true);
        PicChatRequestDTO.StreamOptions streamOptions = new PicChatRequestDTO.StreamOptions();
        streamOptions.setInclude_usage(true);
        picChatRequestDTO.setStream_options(streamOptions);
        //设置请求消息，在此可以加入自己的prompt
        PicChatRequestDTO.ReqMessage message = new PicChatRequestDTO.ReqMessage();
        //用户消息
        message.setRole("user");
        //用户请求内容
        ArrayList<PicChatRequestDTO.Content> contents = new ArrayList<>();

        // 图片信息
        for (String base64Str : param.getBase64List()) {
            PicChatRequestDTO.Content content_image = new PicChatRequestDTO.Content();
            content_image.setType("image_url");
            PicChatRequestDTO.ImageURL imageURL = new PicChatRequestDTO.ImageURL();
            imageURL.setUrl(base64Str);
            content_image.setImage_url(imageURL);
            contents.add(content_image);
        }
        // 对话信息
        PicChatRequestDTO.Content content_text = new PicChatRequestDTO.Content();
        content_text.setType("text");
        content_text.setText(param.getMessages());
        contents.add(content_text);
        message.setContent(contents);
        ArrayList<PicChatRequestDTO.ReqMessage> messages = new ArrayList<>();
        messages.add(message);
        //设置请求消息
        picChatRequestDTO.setMessages(messages);
        //构建请求json
        String paramJson = JSONUtil.toJsonStr(picChatRequestDTO);
        //使用webClient发送消息
        return this.webClient.post()
                //请求uri
                .uri(QWEN_API_URL)
                //设置成自己的key，获得key的方式可以在下文查看
                .header("Authorization", "Bearer " + QWEN_API_KEY)
                //.header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)//设置流式响应
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paramJson))
                .retrieve()
                .bodyToFlux(String.class)
                //接收到消息的处理方法
                .flatMap(result -> handleWebClientResponse(result));
    }

}
