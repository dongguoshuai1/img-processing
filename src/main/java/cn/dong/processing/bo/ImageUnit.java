package cn.dong.processing.bo;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static cn.dong.processing.constant.Constants.POSTER_DIR;

/**
 * 图片元素
 *@Author dong
 *@Date 2023/4/13 上午 11:28
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUnit extends UnitInfo {
    /**
     * 渲染宽度
     * 宽度 默认原尺寸
     */
    private Integer width;
    /**
     * 渲染高度
     * 高度 默认原尺寸
     */
    private Integer height;
    /**
     * 类型
     * 1 URL文件
     * 2 本地文件
     */
    private Integer resourceType = 1;
    /**
     * 圆角弧度；自测720时为圆形
     */
    private Integer radius;

    static {
        File dir = new File(POSTER_DIR);
        if(!dir.exists()){
            dir.mkdir();
        }
    }


    /**
     * 获取持久化图片
     * 根据url下载下图片到本地目录，后续使用本地文件。若下载处理失败，使用url远程图片
     * // TODO 删除缓存，创建的一些本地文件。如果再spring容器中使用，可以容器销毁时做一下删除操作
     *@Author dong
     *@Date 2023/4/13 上午 11:28
     *@param url
     *@return
     */
    public static ImageUnit createLasting(String url){
        ImageUnit result;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            result = new ImageUnit();
            result.setValue(url);
            String[] split = url.split(StrPool.SLASH);
            String fileName = POSTER_DIR+ File.separator+ IdUtil.simpleUUID()+split[split.length - 1];
            inputStream = new URL(url).openConnection().getInputStream();
            outputStream = new FileOutputStream(fileName);
            IoUtil.copy(inputStream,outputStream);
            result = new ImageUnit();
            result.setValue(fileName);
            result.setResourceType(2);
        }catch (Exception e){
            result = new ImageUnit();
            result.setValue(url);
            log.warn("持久化图片失败！url = {}",url,e);
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }
}
