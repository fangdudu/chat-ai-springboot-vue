package life.chat_ai.util;

import net.sourceforge.tess4j.*;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;


@Component
public class ImageDrawUtil {
    /**
     * 在原图右侧拼接白板图片输出
     * @param inputStream  输入的原始图片文件流
     * @param wordList    识别的文字集合
     * @throws IOException
     */
    public static OutputStream imgDraw(InputStream inputStream, List<Word> wordList) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputStream);
        // 1.创建白板画布（与原图同尺寸）
        BufferedImage whiteBoard = new BufferedImage(
                inputImage.getWidth(),
                inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        // 初始化白板绘图
        Graphics2D boardPen = whiteBoard.createGraphics();
        try {
            boardPen.setBackground(Color.WHITE);
            boardPen.clearRect(0, 0, inputImage.getWidth(), inputImage.getHeight());
            configDrawing(boardPen); // 统一配置绘图参数

            // 2.在白板上绘制文字和边框
            drawWords(boardPen, wordList);
        } finally {
            boardPen.dispose();
        }

        // 3.创建左右拼接的合并画布
        BufferedImage combined = new BufferedImage(
                inputImage.getWidth()*2,
                inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D combinedPen = combined.createGraphics();
        try {
            // 绘制原始图片到左侧
            combinedPen.drawImage(inputImage, 0, 0, null);
            // 绘制白板图片到右侧
            combinedPen.drawImage(whiteBoard, inputImage.getWidth(), 0, null);
        } finally {
            combinedPen.dispose();
        }

        // 4.输出合并图片（保留原始图像质量） 返回输出文件
        return saveImageWithQuality(combined);
    }


    /**
     * 统一配置绘图参数
     */
    private static void configDrawing(Graphics2D pen) {
        pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        pen.setColor(new Color(200, 30, 30)); // 暗红色
        pen.setFont(new Font("SimSun", Font.ITALIC, 14));
    }

    /**
     * 执行文字绘制
     * @param pen
     * @param wordList
     */
    private static void drawWords(Graphics2D pen, List<Word> wordList) {
        for (Word word : wordList) {
            Rectangle rect = word.getBoundingBox();
            // 绘制文字框
            pen.drawRect(rect.x, rect.y, rect.width, rect.height);

            // 计算居中位置
            FontMetrics metrics = pen.getFontMetrics();
            int textX = rect.x + (rect.width - metrics.stringWidth(word.getText())) / 2;
            int textY = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

            // 绘制文字（带白色背景提升可读性）
            pen.setColor(Color.WHITE);
            pen.fillRect(textX-2, textY-metrics.getAscent(), metrics.stringWidth(word.getText())+4, metrics.getHeight());
            pen.setColor(new Color(200, 30, 30));
            pen.drawString(word.getText(), textX, textY);
        }
    }

    /**
     * 保存图片到输出流（保留原始图像质量）
     * @param image
     * @return
     * @throws IOException
     */
    private static OutputStream saveImageWithQuality(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new IllegalStateException("未找到JPEG编码器");

            ImageWriter writer = writers.next();
            try {
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.95f);
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }
        }
        return outputStream;
    }



}