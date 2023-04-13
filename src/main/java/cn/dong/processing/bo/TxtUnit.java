package cn.dong.processing.bo;

import lombok.Data;

import java.awt.*;

/**
 * 文本元素
 *@Author dong
 *@Date 2023/4/13 上午 11:32
 */
@Data
public class TxtUnit extends UnitInfo {
    /**
     * 字体大小
     */
    private Integer size = 10;
    /**
     * 字体颜色
     * 默认黑色
     */
    private String color = "#000000";
    /**
     * 字体
     */
    private String name = "Dialog.bold";
    /**
     * 字体样式 默认加粗
     */
    private int style = Font.BOLD;
    /**
     * 最大长度 默认最长
     */
    private int maxLength;
}
