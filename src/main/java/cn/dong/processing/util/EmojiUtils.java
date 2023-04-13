package cn.dong.processing.util;

import cn.dong.processing.bo.ImageUnit;
import cn.dong.processing.bo.TxtUnit;
import cn.dong.processing.bo.UnitInfo;
import cn.dong.processing.support.EmojiImgHolder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * emoji工具类
 *
 * @Author: dong
 * @Date: 2021/12/29 18:01
 */
@UtilityClass
public class EmojiUtils {

    public UnitInfo emojiToUnit(TxtUnit txtUnit){
        Integer size = txtUnit.getSize();
        Integer x = txtUnit.getX();
        Integer y = txtUnit.getY();
        List<UnitInfo> unitInfos = EmojiUtils.subStringToUnit(txtUnit.getValue(), Math.max(txtUnit.getMaxLength(), txtUnit.getValue().length()),
                txtUnit.getColor(), size,txtUnit.getName(),null);
        UnitInfo unit = unitInfos.get(0);
        unit.setX(x);
        if(unit instanceof ImageUnit){
            unit.setY(y-size);
        }else{
            unit.setY(y);
        }
        UnitInfo pre = unit;
        if(unitInfos.size()>1){
            // 依次指定下一个元素，并合并连续的字符串
            for(int i=1;i<unitInfos.size();i++){
                UnitInfo now = unitInfos.get(i);
                if(pre instanceof TxtUnit && now instanceof TxtUnit){
                    pre.setValue(pre.getValue()+now.getValue());
                    continue;
                }
                if(now instanceof ImageUnit){
                    now.setY(y-size);
                }else{
                    now.setY(y);
                }
                pre.setAfter(now);
                pre = now;
            }
        }
        return unit;
    }

    /**
     * 截取字符串 表情符视为一个字符，返回元素集合
     * @param str
     * @param len 字符串的最大长度
     * @param fontColor 
     * @param fontSize
     * @param fontName
     * @param style :加粗、斜体
     * @return
     */
    private List<UnitInfo> subStringToUnit(String str, int len,String fontColor,int fontSize,String fontName,Integer style){
        if(!EmojiUtil.containsEmoji(str)){
            String nowPlain;
            if(str.length()>len){
                nowPlain = str.substring(0,len)+"...";
            }else{
                nowPlain = str;
            }
            return Arrays.asList(createTxt(nowPlain,fontColor,fontSize,fontName, style));
        }else{
            List<UnitInfo> result = new ArrayList<>();
            // 包含表情符
            Iterator<String> emojiIt = EmojiUtil.extractEmojis(str).iterator();
            String s = EmojiUtil.removeAllEmojis(str);
            Iterator<String> plainIt = CollectionUtil.toList(s.split("")).iterator();
            StringBuilder sb = new StringBuilder();
            int nowLen = 0;
            String nowPlain = null;
            for(int idx=0;idx<str.length();idx++){
                if(plainIt.hasNext()&&nowPlain==null){
                    nowPlain = plainIt.next();
                }
                boolean doEmoji = true;
                if(Objects.equals(String.valueOf(str.charAt(idx)),nowPlain)){
                    result.add(createTxt(nowPlain,fontColor,fontSize,fontName, style));
                    sb.append(nowPlain);
                    nowPlain = null;
                    nowLen++;
                    doEmoji = false;
                }
                if(doEmoji){
                    String emoji = emojiIt.next();
                    sb.append(emoji);
                    String path = EmojiImgHolder.getEmojiPath(emoji);
                    if(path!=null){
                        ImageUnit unit = new ImageUnit();
                        unit.setValue(path);
                        unit.setWidth(fontSize);
                        unit.setHeight(fontSize);
                        result.add(unit);
                    }
                    nowLen++;
                    idx = sb.length()-1;
                }
                if(nowLen==len){
                    result.add(createTxt("...",fontColor,fontSize,fontName, style));
                    break;
                }
            }
            return result;
        }
    }


    private TxtUnit createTxt(String val, String color, int size, String fontName, Integer style){
        TxtUnit unit = new TxtUnit();
        unit.setSize(size);
        unit.setValue(val);
        unit.setColor(color);
        if(StrUtil.isNotBlank(fontName)){
            unit.setName(fontName);
        }
        if(style!=null){
            unit.setStyle(style);
        }
        return unit;
    }
}
