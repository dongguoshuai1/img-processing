package cn.dong.processing.util;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片工具类
 *
 * @Author: dong
 * @Date: 2023/4/13 14:44
 */
@UtilityClass
public class ImageUtils {

    public static BufferedImage setClip(BufferedImage img, int radius){
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage res = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = res.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.fillRoundRect(0, 0,width, height, radius, radius);
        gs.setComposite(AlphaComposite.SrcIn);
//        gs.setClip(new RoundRectangle2D.Double(0,0, width, height, radius, radius));
        gs.drawImage(img,0,0,null);
        gs.dispose();
        return res;
    }
}
