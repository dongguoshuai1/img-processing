package cn.dong.processing.worker;

import cn.dong.processing.bo.UnitInfo;

import java.awt.*;
import java.io.IOException;

/**
 * 合成接口
 *
 * @Author: dong
 * @Date: 2023/4/13 14:33
 */
public interface Worker <T extends UnitInfo> {

    void loadUnit(T unitInfo, Graphics2D g) throws IOException;
}
