package life.chat_ai.util;

import io.github.whitemagic2014.tts.TTS;
import io.github.whitemagic2014.tts.TTSVoice;
import io.github.whitemagic2014.tts.bean.Voice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TTSUtil {
    private static String TTS_STORAGE_PATH;
    @Value("${tts.path}")
    public void setTtsStoragePath(String path) {
        TTSUtil.TTS_STORAGE_PATH = path;
    }
    public static String getTtsStoragePath() {
        return TTS_STORAGE_PATH;
    }

    /**
     * 返回TTS文件名
     * @param voiceContent 语音内容
     * @param voiceShortName 音频格式
     * @return
     */
    public static String createTTSFile(String voiceContent, String voiceShortName) {
        Voice voice = TTSVoice.provides().stream().filter(v -> v.getShortName().equals(voiceShortName)).collect(Collectors.toList()).get(0);
        String fileName = new TTS(voice, voiceContent)
                .findHeadHook()
                .isRateLimited(true)
                .fileName(UUID.randomUUID().toString())
                .overwrite(false)
                .storage(TTS_STORAGE_PATH)
                .formatMp3()
                .trans();
        return fileName;
    }

    /**
     * 返回TTS文件名
     * @param voiceContent 语音内容
     * @return
     */
    public static String createTTSFile(String voiceContent) {
        Voice voice = TTSVoice.provides().stream().filter(v -> v.getShortName().equals("zh-CN-XiaoyiNeural")).collect(Collectors.toList()).get(0);
        String fileName = new TTS(voice, voiceContent)
                .findHeadHook()
                .isRateLimited(true)
                .fileName(UUID.randomUUID().toString())
                .overwrite(false)
                .storage(TTS_STORAGE_PATH)
                .formatMp3()
                .trans();
        return fileName;
    }

    public static void main(String[] args) {
        // Voice can be found in file "voicesList.json"
        Voice voice = TTSVoice.provides().stream().filter(v -> v.getShortName().equals("zh-CN-XiaoyiNeural")).collect(Collectors.toList()).get(0);
        String content = "你好，有什么可以帮助你的吗";
        String fileName = new TTS(voice, content)
                .findHeadHook()
                .isRateLimited(true) // Set to true to resolve the rate limiting issue in certain regions..
                .fileName(UUID.randomUUID().toString())// You can customize the file name; if omitted, a random file name will be generated.
                .overwrite(false)
                .storage("./mp3")// When the specified file name is the same, it will either overwrite or append to the file.
                .formatMp3()  // default mp3.
//                .formatOpus() // or opus
//                .voicePitch()
//                .voiceRate()
//                .voiceVolume()
//                .storage()  // the output file storage ,default is ./storage
//                .connectTimeout(0) // set connect timeout
                .trans();
        // you can find the voice file in storage folder
    }
}
