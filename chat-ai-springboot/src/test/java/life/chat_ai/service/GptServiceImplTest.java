package life.chat_ai.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.chat_ai.dto.AIAnswerDTO;
import life.chat_ai.dto.ChatRequestDTO;
import life.chat_ai.dto.PicChatRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class GptServiceImplTest {

    @Autowired
    GptServiceImpl gptService;

    @Test
    void contextLoads() {
        //实现类发送消息并获取返回结果
        gptService.doChatGPTStream("输出123")
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());//发生异常时发送空对象
    }

    //用于读取第三方的返回结果
    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void doChatGPTStream() {
        //构建请求对象
        ChatRequestDTO chatRequestDTO = new ChatRequestDTO();
        //设置模型
        chatRequestDTO.setModel("deepseek-r1");
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
        message.setContent("输出123");
        ArrayList<ChatRequestDTO.ReqMessage> messages = new ArrayList<>();
        messages.add(message);
        //设置请求消息
        chatRequestDTO.setMessages(messages);


        //构建请求json
        String paramJson = JSONUtil.toJsonStr(chatRequestDTO);
        ;
        WebClient build = WebClient.builder()//创建webflux的client
//                .baseUrl("")//填写对应的api地址
                .defaultHeader("Content-Type", "application/json")//设置默认请求类型
                .build();
        //使用webClient发送消息
        Flux<AIAnswerDTO> aiAnswerDTOFlux = build.post()
                .uri("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions")//请求uri
                .header("Authorization", "Bearer sk-d5543abc68eb47e79fa44be2ae04dd5d")//设置成自己的key，获得key的方式可以在下文查看
                //.header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)//设置流式响应
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paramJson))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(result -> handleWebClientResponse(result));

        System.out.println(aiAnswerDTOFlux);
    }

    private Flux<AIAnswerDTO> handleWebClientResponse(String resp) {
        if (StrUtil.equals("[DONE]", resp)) {//[DONE]是消息结束标识
            return Flux.empty();
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(resp);
            System.out.println(jsonNode);
            AIAnswerDTO result = objectMapper.treeToValue(jsonNode, AIAnswerDTO.class);//将获得的结果转成对象
            return Flux.just(result);//返回获得的结果
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Value("${bytedance.volcengine.api-url}")
    public String BYTEDANCE_VOLCENGINE_API_URL;
    @Value("${bytedance.volcengine.api-key}")
    public String BYTEDANCE_VOLCENGINE_API_KEY;
    @Value("${bytedance.volcengine.model}")
    public String BYTEDANCE_VOLCENGINE_MODEL;


    /**
     * 字节火山引擎
     */
    @Test
    void doVolcengineStream() {
        //构建请求对象
        ChatRequestDTO chatRequestDTO = new ChatRequestDTO();
        //设置模型
        chatRequestDTO.setModel("ep-20250213171939-g6vxd");
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
        message.setContent("输出123");
        ArrayList<ChatRequestDTO.ReqMessage> messages = new ArrayList<>();
        messages.add(message);
        //设置请求消息
        chatRequestDTO.setMessages(messages);


        //构建请求json
        String paramJson = JSONUtil.toJsonStr(chatRequestDTO);
        ;
        WebClient build = WebClient.builder()//创建webflux的client
//                .baseUrl("")//填写对应的api地址
                .defaultHeader("Content-Type", "application/json")//设置默认请求类型
                .build();
        //使用webClient发送消息
        Flux<AIAnswerDTO> aiAnswerDTOFlux = build.post()
                .uri(BYTEDANCE_VOLCENGINE_API_URL)//请求uri
                .header("Authorization", "Bearer "+BYTEDANCE_VOLCENGINE_API_KEY)//设置成自己的key，获得key的方式可以在下文查看
                //.header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)//设置流式响应
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paramJson))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(result -> handleWebClientResponse(result));

        System.out.println(aiAnswerDTOFlux);
        System.out.println(111);
    }


    @Value("${siliconflow.model}")
    public String SILICONFLOW_MODEL;
    @Value("${siliconflow.api-url}")
    public String SILICONFLOW_API_URL;
    @Value("${siliconflow.api-key}")
    public String SILICONFLOW_API_KEY;

    @Test
    void doSiliconflowStream() {
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
        message.setContent("输出123");
        ArrayList<ChatRequestDTO.ReqMessage> messages = new ArrayList<>();
        messages.add(message);
        //设置请求消息
        chatRequestDTO.setMessages(messages);

        //构建请求json
        String paramJson = JSONUtil.toJsonStr(chatRequestDTO);
        WebClient build = WebClient.builder()//创建webflux的client
//                .baseUrl("")//填写对应的api地址
                .defaultHeader("Content-Type", "application/json")//设置默认请求类型
                .build();
        //使用webClient发送消息
        Flux<AIAnswerDTO> aiAnswerDTOFlux = build.post()
                .uri(SILICONFLOW_API_URL)//请求uri
                .header("Authorization", "Bearer "+SILICONFLOW_API_KEY)//设置成自己的key，获得key的方式可以在下文查看
                //.header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)//设置流式响应
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paramJson))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(result -> handleWebClientResponse(result));

        List<AIAnswerDTO> allMessages = aiAnswerDTOFlux.collectList().block();
        System.out.println("全量结果: " + allMessages);
    }

    @Value("${aliyun.bailian.qwen25.model}")
    public String QWEN_MODEL;
    @Value("${aliyun.bailian.qwen25.api-url}")
    public String QWEN_API_URL;
    @Value("${aliyun.bailian.qwen25.api-key}")
    public String QWEN_API_KEY;
    @Test
    void doQwenStream() {
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
        PicChatRequestDTO.Content content_image = new PicChatRequestDTO.Content();
        content_image.setType("image_url");
        PicChatRequestDTO.ImageURL imageURL = new PicChatRequestDTO.ImageURL();
        imageURL.setUrl("https://help-static-aliyun-doc.aliyuncs.com/file-manage-files/zh-CN/20241022/emyrja/dog_and_girl.jpeg");
        content_image.setImage_url(imageURL);
        contents.add(content_image);

        PicChatRequestDTO.Content content_text = new PicChatRequestDTO.Content();
        content_text.setType("text");
        content_text.setText("图中描绘的是什么景象？");
        contents.add(content_text);

        message.setContent(contents);

        ArrayList<PicChatRequestDTO.ReqMessage> messages = new ArrayList<>();
        messages.add(message);

        //设置请求消息
        picChatRequestDTO.setMessages(messages);

        //构建请求json
        String paramJson = JSONUtil.toJsonStr(picChatRequestDTO);

        WebClient build = WebClient.builder()//创建webflux的client
//                .baseUrl("")//填写对应的api地址
                .defaultHeader("Content-Type", "application/json")//设置默认请求类型
                .build();
        //使用webClient发送消息
        Flux<AIAnswerDTO> aiAnswerDTOFlux = build.post()
                .uri(QWEN_API_URL)//请求uri
                .header("Authorization", "Bearer "+QWEN_API_KEY)//设置成自己的key，获得key的方式可以在下文查看
                //.header(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)//设置流式响应
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paramJson))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(result -> handleWebClientResponse(result));

        List<AIAnswerDTO> allMessages = aiAnswerDTOFlux.collectList().block();
        System.out.println("全量结果: " + allMessages);
    }


}