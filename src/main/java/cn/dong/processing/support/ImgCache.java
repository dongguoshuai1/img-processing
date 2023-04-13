package cn.dong.processing.support;

import cn.dong.processing.bo.ImageUnit;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * 图片缓存
 *@Author dong
 *@Date 2023/4/13 上午 11:37
 */
@Slf4j
@UtilityClass
public class ImgCache {
    /**
     * 缓存使用弱引用，避免oom
     *@Author dong
     *@Date 2022/4/1 14:56
     *@param null
     *@return
     */
    private Cache<String, Entry> cache = CacheUtil.newLRUCache(50,60000);

    private Field osis;

    static {
        try {
            osis = BufferedImage.class.getDeclaredField("osis");
            osis.setAccessible(true);
        } catch (Exception e) {
        }
    }

    /**
     * 获取缓存的图片数据
     * 由于ImageIO.read(new URL(key))锁竞争激烈，所以加载时锁key。
     *@Author dong
     *@Date 2023/4/13 上午 11:37
     *@param unit
     *@return
     */
    public BufferedImage get(ImageUnit unit) {
        String key = unit.getValue().intern();
        BufferedImage result = null;
        Entry entry = cache.get(key);
        if(entry!=null&&entry.get()!=null){
            result = entry.get();
        }else{
            synchronized (key){
                entry = cache.get(key);
                if(entry==null||entry.get()==null){
                    try {
                        if(unit.getResourceType()==1){
                            result = ImageIO.read(new URL(key));
                        }else{
                            result = ImageIO.read(new File(key));
                        }
                    }catch (Exception e){
                        log.error("获取文件失败！val={},type={}",key,unit.getResourceType(),e);
                    }
                    entry = new Entry(result);
                    cache.put(unit.getValue(),entry);
                }else{
                    result = entry.get();
                }
            }
        }
        return deepCopy(result);
    }

    /**
     * 根据原图片新建资源，避免FilteredImageSource锁竞争
     *@Author dong
     *@Date 2023/4/13 上午 11:37
     *@param bi
     *@return
     */
    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        BufferedImage bufferedImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
//        BufferedImage bufferedImage = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        ImageProducer source = bufferedImage.getSource();
        try {
            osis.set(bufferedImage,source);
        } catch (Exception e) {
            return bi;
        }
        return bufferedImage;
    }

    static class Entry extends SoftReference<BufferedImage> {
        Entry(BufferedImage v) {
            super(v);
        }
    }



}
