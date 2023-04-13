package cn.dong.processing.worker.impl;

import cn.dong.processing.bo.TxtUnit;
import cn.dong.processing.bo.UnitInfo;
import cn.dong.processing.worker.Worker;
import cn.dong.processing.worker.WorkerHolder;
import lombok.extern.slf4j.Slf4j;
import sun.java2d.SunGraphics2D;

import java.awt.*;
import java.io.IOException;

/**
 * 文本处理
 *
 * @Author: dong
 * @Date: 2023/4/13 14:36
 */
@Slf4j
public class TxtWorker implements Worker<TxtUnit> {

    @Override
    public void loadUnit(TxtUnit txtUnit, Graphics2D g) throws IOException {
        log.info("--------------覆盖图片 增加文字：{}",txtUnit.getValue());
        String text = txtUnit.getValue();
        Font font = new Font(txtUnit.getName(), txtUnit.getStyle(), txtUnit.getSize());
        g.setFont(font);
        g.setPaint(Color.decode(txtUnit.getColor()));
        // 水平居中
        if(txtUnit.isXCenter()){
            int width = ((SunGraphics2D) g).getCompClip().getWidth();
            int i = g.getFontMetrics().stringWidth(text);
            int x = (width-i)/2;
            txtUnit.setX(x);
        }
        g.drawString(text, txtUnit.getX(), txtUnit.getY());
        UnitInfo after = txtUnit.getAfter();
        if(after!=null){
            int i = g.getFontMetrics().stringWidth(text);
            after.setX(txtUnit.getX()+i+after.getX());
            WorkerHolder.loadUnit(after,g);
        }
    }
}
