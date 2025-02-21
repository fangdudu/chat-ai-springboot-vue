package life.chat_ai.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import life.chat_ai.dto.AIAnswerDTO;
import life.chat_ai.dto.PicParamsDTO;
import life.chat_ai.service.GptServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ChatController {

    //用于流式请求第三方的实现类
    @Resource
    GptServiceImpl gptService;

    //通过stream返回流式数据
    @GetMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AIAnswerDTO>> getStream(@RequestParam("messages") String messages) {
        //实现类发送消息并获取返回结果
        return gptService.doChatGPTStream(messages)
                //进行结果的封装，再返回给前端
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()
                        .data(aiAnswerDTO)
                        .build()
                )
                //发生异常时发送空对象
                .onErrorResume(e -> Flux.empty());
    }



    @PostMapping(value = "/completions/getChatKey", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Map getChatKey(@RequestPart("file") List<MultipartFile> files,@RequestPart("messages") String messages) throws IOException {
        PicParamsDTO picParamsDTO = new PicParamsDTO();
        picParamsDTO.setMessages(messages);
        picParamsDTO.setFiles(files);
        String chatKey = gptService.getChatKeyByPicParams(picParamsDTO);

        Map<String, String> response  = new HashMap<>();
        response.put("chatKey", chatKey);
        return response;
    }

    @GetMapping(value = "/completions/pic", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AIAnswerDTO>> getPicStream(@RequestParam("chatKey") String chatKey) throws JsonProcessingException {
        //实现类发送消息并获取返回结果
        return gptService.doPicChatGPTStream(chatKey)
                //进行结果的封装，再返回给前端
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()
                        .data(aiAnswerDTO)
                        .build()
                )
                //发生异常时发送空对象
                .onErrorResume(e -> Flux.empty());
    }
}
