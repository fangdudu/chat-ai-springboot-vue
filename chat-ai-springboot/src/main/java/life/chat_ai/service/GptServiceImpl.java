package life.chat_ai.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.chat_ai.dto.*;
import life.chat_ai.util.ImageDrawUtil;
import life.chat_ai.util.PicUtil;
import life.chat_ai.util.TTSUtil;
import net.sourceforge.tess4j.Word;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;


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
    @Value("${tts.path}")
    public String TTS_PATH;

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

    public Resource returnTTSFile(String messages) {
        String ttsStorageFileName = TTSUtil.createTTSFile(messages);
        String filePath = TTS_PATH + File.separator + ttsStorageFileName;
        Resource resource = new FileSystemResource(filePath);
        // 检查文件是否存在
        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    // 添加 ocrRequest 方法
    public Map<String, Object> ocrRequest(PicParamsDTO picParamsDTO) throws IOException {
        List<String> imageBase64List = new ArrayList<>();
        String url = "http://103.246.245.13:5000/ocr";

        List<MultipartFile> files = picParamsDTO.getFiles();
        for (MultipartFile file : files) {
            String base64Str = Base64.encodeBase64String(file.getBytes());

            Map<String, String> map = new HashMap<>();
            map.put("image", base64Str);
            String paramJson = JSONUtil.toJsonStr(map);

            Mono<OcrResponseDTO> ocrResponseDTOMono = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(paramJson)
                    .retrieve()
                    .bodyToMono(OcrResponseDTO.class);

            // 阻塞获取结果
            OcrResponseDTO response = ocrResponseDTOMono.block();  // 注意：这会阻塞线程
            if (response != null) {
                List<Word> wordList = new ArrayList<>();
                OcrResponseDTO.OcrResult result = response.getResult();
                System.out.println("Error Code: " + result.getErrcode());
                System.out.println("Image Height: " + result.getHeight());
                System.out.println("Image Width: " + result.getWidth());
                System.out.println("Image Path: " + result.getImgpath());
                System.out.println("OCR Results:");
                result.getOcr_response().forEach(item -> {
                    System.out.printf("Text: %s, Position: (%.2f, %.2f, %.2f, %.2f), Rate: %.4f%n", item.getText(), item.getLeft(), item.getTop(), item.getRight(), item.getBottom(), item.getRate());

                    // 计算 Rectangle 参数
                    int x = (int) Math.round(item.getLeft());
                    int y = (int) Math.round(item.getTop());
                    int width = (int) Math.round(item.getRight() - item.getLeft());
                    int height = (int) Math.round(item.getBottom() - item.getTop());

                    // 创建 Rectangle
                    Rectangle rect = new Rectangle(x, y, width, height);

                    // 创建 Word 对象
                    Word word = new Word(
                            item.getText(),
                            (float) item.getRate(),  // confidence 使用 rate
                            rect
                    );
                    wordList.add(word);
                });
                try {
                    OutputStream outputStream = ImageDrawUtil.imgDraw(file.getInputStream(), wordList);
                    // 转为base64字符串 返回给前端
                    ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    String base64 = Base64.encodeBase64String(imageBytes);
                    imageBase64List.add("data:image/jpeg;base64," + base64);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("text", "test");  // 所有识别的文字
        response.put("images", imageBase64List);  // Base64 编码的图片数据

        return response;
    }
}

