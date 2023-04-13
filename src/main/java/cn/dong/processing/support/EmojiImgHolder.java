package cn.dong.processing.support;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * emoji图片映射持有类
 * 解决emoji转换图片，
 * TODO emoji表情扩展 所有表情文件位于/emoji下，文件名就是表情对应的unicode码。
 *@Author dong
 *@Date 2023/4/13 上午 11:42
 */
@Slf4j
@UtilityClass
public class EmojiImgHolder {

    // 参考com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties#getResources
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private Map<String,String> existsSet = new HashMap<>();

    private String EMOJI_PATH = "classpath:/emoji/*.png";

    private String EMOJI_KEY = "{emoji}";

    private String BASE_PATH;

    static {
        Resource[] resources = null;
        try {
            resources = resourceResolver.getResources(EMOJI_PATH);
            log.info("共发现emoji表情图{}个！",resources.length);
            for (Resource resource : resources) {
                String name = resource.getFilename();
                String unicode = name.substring(0, name.indexOf(StrPool.DOT));
                String key = convert(unicode);
                existsSet.put(key,name);
            }
            Resource first = resources[0];
            String filename = first.getFilename();
            String path = first.getURI().toString();
            BASE_PATH = path.substring(0,path.lastIndexOf(filename))+EMOJI_KEY;
        } catch (Exception e) {
            log.error("emoji图片映射关系加载失败！",e);
        }
    }

    /**
     * 获取emoji表情对应的文件地址
     *@Author dong
     *@Date 2023/4/13 上午 11:42
     *@param str
     *@return
     */
    public String getEmojiPath(String str){
        String emojiName = existsSet.get(str);
        if(emojiName==null){
            return null;
        }
        return StrUtil.replace(BASE_PATH,EMOJI_KEY,emojiName);
    }



    /**
     * unicode转化
     *@Author dong
     *@Date 2023/4/13 上午 11:42
     *@param unicodeStr
     *@return
     */
    private static String convert(String unicodeStr) {
        if (unicodeStr.isEmpty()) {
            return unicodeStr;
        }
        String[] parts = unicodeStr.split("-");
        StringBuilder buff = new StringBuilder();
        for (String s : parts) {
            int part = Integer.parseInt(s, 16);
            if (part >= 0x10000 && part <= 0x10FFFF) {
                int hi = (int) (Math.floor((part - 0x10000) / 0x400) + 0xD800);
                int lo = ((part - 0x10000) % 0x400) + 0xDC00;
                buff.append(new String(Character.toChars(hi)) + new String(Character.toChars(lo)));
            } else {
                buff.append(new String(Character.toChars(part)));
            }
        }
        return buff.toString();
    }
}
