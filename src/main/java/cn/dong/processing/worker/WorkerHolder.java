package cn.dong.processing.worker;

import cn.dong.processing.bo.ImageUnit;
import cn.dong.processing.bo.TxtUnit;
import cn.dong.processing.bo.UnitInfo;
import cn.dong.processing.worker.impl.ImageWorker;
import cn.dong.processing.worker.impl.TxtWorker;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.io.IOException;

/**
 * 合成实现持有
 * @Author: dong
 * @Date: 2023/4/13 14:39
 */
@UtilityClass
public class WorkerHolder {

    private ImageWorker imageWorker = new ImageWorker();
    private TxtWorker txtWorker = new TxtWorker();


    public void loadUnit(UnitInfo unitInfo, Graphics2D g) throws IOException {
        if(unitInfo==null){
            return;
        }
        // 后续可以考虑使用谋设计模式
        if(unitInfo instanceof ImageUnit){
            imageWorker.loadUnit((ImageUnit) unitInfo,g);
        }else if(unitInfo instanceof TxtUnit){
            txtWorker.loadUnit((TxtUnit) unitInfo,g);
        }
    }
}
