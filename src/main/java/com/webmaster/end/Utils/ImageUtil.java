package com.webmaster.end.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.webmaster.end.Entity.ResultMap;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;

@Component
public class ImageUtil {
    public static final int WIDTH = 120;//生成的图片的宽度
    public static final int HEIGHT = 30;//生成的图片的高度

    /**
     * 设置图片的背景色
     * @param g
     */
    public void setBackGround(Graphics g) {
        // 设置颜色
        g.setColor(Color.WHITE);
        // 填充区域
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * 设置图片的边框
     * @param g
     */
    public void setBorder(Graphics g) {
        // 设置边框颜色
        g.setColor(Color.WHITE);
        // 边框区域
        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
    }

    /**
     * 在图片上画随机线条
     * @param g
     */
    public void drawRandomLine(Graphics g) {
        // 设置颜色
        g.setColor(new Color(4,121,238,255));
        // 设置线条个数并画线
        for (int i = 0; i < 5; i++) {
            int x1 = new Random().nextInt(WIDTH);
            int y1 = new Random().nextInt(HEIGHT);
            int x2 = new Random().nextInt(WIDTH);
            int y2 = new Random().nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 创建随机字符
     * @param g
     * @param baseChar
     * @return 随机字符
     */
    public String createRandomChar(Graphics2D g, String baseChar) {
        StringBuffer sb = new StringBuffer();
        int x = 5;
        String ch ="";
        // 控制字数
        for (int i = 0; i < 4; i++) {
            // 设置随机字体旋转角度
            int degree = new Random().nextInt() % 30;
            //从范围内随机找一个字符
            ch = baseChar.charAt(new Random().nextInt(baseChar.length())) + "";
            //添加到字符串中
            sb.append(ch);
            // 将笔正向角度，以及旋转原点的x轴和y轴
            g.rotate(degree * Math.PI / 180, x, 20);
            g.setFont(new Font("Arial",Font.PLAIN,18));
            //在指定的位置绘制字符串
            g.drawString(ch, x, 20);
            // 反向角度，将笔转回来
            g.rotate(-degree * Math.PI / 180, x, 20);
            x += 30;
        }
        return sb.toString();
    }

    /**
     * 画随机字符
     * @param g
     * @return
     */
    public String drawRandomNum(Graphics2D g) {
        // 设置颜色
        g.setColor(Color.RED);
        //数字和字母的组合
        String baseNumLetter = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
        // 截取数字和字母的组合
        return createRandomChar(g, baseNumLetter);
    }

    //------------------------------------------------二维码功能-----------------------------------------

    /**
     * 生成二维码
     * @param  serial 书籍的编号
     * @return String
     * @throws WriterException
     * @throws IOException
     */
    public static Map<String,Object> createQRCode(String serial){
        try {
            String QRCodeImagePath = "/home/image/QR" + MyDateUtil.getCurrentTime() + ".png";
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String text = "userId:" + serial;
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
            Path path = FileSystems.getDefault().getPath(QRCodeImagePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return ResultMap.getResultMap("http://123.56.3.135:8081"+QRCodeImagePath.substring(5),"生成二维码成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"生成二维码失败");
        }
    }

}
