package cn.dong.processing.worker;

import cn.dong.processing.support.ImgCache;
import cn.dong.processing.util.EmojiUtils;
import cn.dong.processing.util.ImageUtils;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.dong.processing.bo.ImageUnit;
import cn.dong.processing.bo.TxtUnit;
import cn.dong.processing.bo.UnitInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import sun.java2d.SunGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片工具 进行图片合成工作
 *@Author dong
 *@Date 2023/4/13 上午 11:35
 */
@Slf4j
@UtilityClass
public class WorkerExecute {
    private GraphicsEnvironment localGraphicsEnvironment;

    static {
        try {
            localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
           // TODO 字体扩展，可以尝试注入其他字体
//            Font font = Font.createFont(Font.TRUETYPE_FONT,
//                    new ClassPathResource("file/bb4171.ttf").getStream());
//            localGraphicsEnvironment.registerFont(font);
            Font[] allFonts = localGraphicsEnvironment.getAllFonts();
            for (Font allFont : allFonts) {
                log.info("--------------"+allFont.getName());
            }
        } catch (Exception e) {
            log.error("加载字体失败！",e);
        }
    }

    /**
     * 覆盖图片
     * @param mainImg   主图片路径
     * @param unitInfos    元素
     * @param compoundPath  图片合成保存路径
     * @return 是否成功
     */
    public void overlayImage(ImageUnit mainImg, List<UnitInfo> unitInfos, String compoundPath) {
        try {
            log.info("--------------开始覆盖图片：底图{}",mainImg.getValue());
            //1、设置好主图
            BufferedImage big = ImgCache.get(mainImg);
            if(mainImg.getRadius()!=null){
                big = ImageUtils.setClip(big, mainImg.getRadius());
            }
            int width = mainImg.getWidth()==null?big.getWidth():mainImg.getWidth();
            int height = mainImg.getHeight()==null?big.getHeight():mainImg.getHeight();
            Image image = big.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = localGraphicsEnvironment.createGraphics(bufferedImage2);
            g.drawImage(image, mainImg.getX(), mainImg.getY(), null);
            // 处理emoji标签图
            List<UnitInfo> nowUnitInfos = new ArrayList<>();
            for (UnitInfo unitInfo : unitInfos) {
                if(unitInfo instanceof TxtUnit){
                    nowUnitInfos.add(EmojiUtils.emojiToUnit((TxtUnit) unitInfo));
                }else{
                    nowUnitInfos.add(unitInfo);
                }
            }
            //2、开始覆盖图片
            if(CollectionUtil.isNotEmpty(nowUnitInfos)){
                for (UnitInfo unitInfo : nowUnitInfos) {
                    WorkerHolder.loadUnit(unitInfo,g);
                }
            }
            g.dispose();
            int i = compoundPath.lastIndexOf(".");
            ImageIO.write(bufferedImage2, compoundPath.substring(i+1), new File(compoundPath));
            log.info("--------------覆盖图片成功");
            // TODO 可以获取文件字节数组
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            ImageIO.write(bufferedImage2, compoundPath.substring(i+1), out);
//            out.toByteArray();
        } catch (Exception e) {
            log.error("覆盖图片失败！",e);
        }
    }

}
