package cn.dong.processing;

import cn.dong.processing.bo.ImageUnit;
import cn.dong.processing.bo.TxtUnit;
import cn.dong.processing.bo.UnitInfo;
import cn.dong.processing.worker.WorkerExecute;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Author: dong
 * @Date: 2023/4/13 14:15
 */
public class TestClass {

    public static void main(String[] args) {
        ImageUnit main = new ImageUnit();
        main.setResourceType(2);
        main.setValue("C:\\Users\\14910\\Desktop\\ditu.png");
        // 设置覆盖的图片
        ImageUnit tu1 = new ImageUnit();
        tu1.setResourceType(2);
        tu1.setValue("C:\\Users\\14910\\Desktop\\tu1.png");
        tu1.setRadius(720);
        tu1.setX(50);
        tu1.setY(50);
        List<UnitInfo> list = new ArrayList<>();
        list.add(tu1);
        // 设置文字
        TxtUnit txtUnit = new TxtUnit();
        // 昵称😄
        txtUnit.setValue("昵称\uD83D\uDE04");
        txtUnit.setSize(20);
        txtUnit.setX(100);
        txtUnit.setY(200);
        list.add(txtUnit);
        WorkerExecute.overlayImage(main,list,"C:\\Users\\14910\\Desktop\\tu2.png");
    }
}
