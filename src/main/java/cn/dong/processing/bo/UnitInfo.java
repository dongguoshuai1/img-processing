package cn.dong.processing.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 元素基类
 *@Author dong
 *@Date 2023/4/13 上午 11:26
 */
@Data
public class UnitInfo implements Serializable {
    /**
     * 内容
     */
    private String value;
    /**
     * x轴位置默认0
     */
    private Integer x = 0;
    /**
     * xy轴位置默认0
     */
    private Integer y = 0;

    /**
     * 后续元素
     */
    private UnitInfo after;

    /**
     * 水平居中
     */
    private boolean xCenter;
}
