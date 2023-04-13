package cn.dong.processing.bo;

import lombok.Data;

import java.util.List;

/**
 * 文件合成任务
 *@Author dong
 *@Date 2023/4/13 上午 11:32
 */
@Data
public class ProcessingTaskInfo {

    /**
     * 底图
     */
    private ImageUnit mainImg;

    /**
     * 底图上的各种元素
     */
    private List<UnitInfo> unitInfos;
}
