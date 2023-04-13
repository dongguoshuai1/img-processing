package cn.dong.processing.worker.impl;

import cn.dong.processing.bo.ImageUnit;
import cn.dong.processing.bo.UnitInfo;
import cn.dong.processing.support.ImgCache;
import cn.dong.processing.util.ImageUtils;
import cn.dong.processing.worker.Worker;
import cn.dong.processing.worker.WorkerHolder;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片实现
 *
 * @Author: dong
 * @Date: 2023/4/13 14:34
 */
@Slf4j
public class ImageWorker implements Worker<ImageUnit> {

    @Override
    public void loadUnit(ImageUnit imageUnit, Graphics2D g) throws IOException {
        log.info("--------------覆盖图片：{}",imageUnit.getValue());
        BufferedImage erweima = ImgCache.get(imageUnit);
        if(imageUnit.getRadius()!=null){
            erweima = ImageUtils.setClip(erweima,imageUnit.getRadius());
        }
        int eWidth = imageUnit.getWidth()==null?erweima.getWidth():imageUnit.getWidth();
        int wHeight = imageUnit.getHeight()==null?erweima.getHeight():imageUnit.getHeight();
        g.drawImage(erweima, imageUnit.getX(), imageUnit.getY(), eWidth, wHeight, null);
        UnitInfo after = imageUnit.getAfter();
        if(after!=null){
            int i = imageUnit.getX();
            after.setX(imageUnit.getWidth()+i+after.getX());
            WorkerHolder.loadUnit(after,g);
        }
    }


}
