package life.chat_ai.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TTSUtilTest {

    @Test
    void createTTSFile() {
        String ttsFile = TTSUtil.createTTSFile("你好，有什么可以帮助你的吗", "zh-CN-XiaoyiNeural");
        System.out.println(ttsFile);
    }
    @Test
    void createTTSFile2() {
        String ttsFile = TTSUtil.createTTSFile("你好，有什么可以帮助你的吗");
        System.out.println(ttsFile);
    }
}