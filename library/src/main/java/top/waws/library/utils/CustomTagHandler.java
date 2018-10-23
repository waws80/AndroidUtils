package top.waws.library.utils;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 *  功能描述: 自定义的html 标签解析器
 *  @className: CustomTagHandler
 *  @author: thanatos
 *  @createTime: 2018/2/23
 *  @updateTime: 2018/2/23 09:27
 */
public final class CustomTagHandler implements Html.TagHandler{

    /**
     * 自定义的删除线标签
     * <p>
     *     <cs>我是删除线</cs>
     * </p>
     * cs节点下标标记
     */
    private int csStartIndex = 0;
    private int cfontStrtIndex = 0;

    /**
     * 自定义的font标签支持 设置字体大小
     * <p>
     *     <cfont size='20px' color='#FFFFFF'>我是自定义font</cfont>
     * </p>
     * cfont节点下标标记
     */
    private int csEndIndex = 0;
    private int cfontEndIndex = 0;

    /**
     * cfont节点属性存储map
     */
    private HashMap<String,String> attributes = new HashMap<>();


    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equals("cs")){
            if (opening){
                startTag(tag,output);
            }else {
                endTad(tag,output);
            }
        }else if (tag.equals("cfont")){
            paseAttribute(xmlReader);
            if (opening){
                startTag(tag,output);
            }else {
                endTad(tag,output);
            }
        }

    }

    /**
     * 标记节点开始下标
     * @param tag
     * @param outPut
     */
    private void startTag(String tag, Editable outPut){
        if (tag.equals("cs")){
            csStartIndex = outPut.length();
        }else if (tag.equals("cfont")){
            cfontStrtIndex = outPut.length();
        }

    }

    /**
     * 节点结束下标并进行属性处理
     * @param tag
     * @param outPut
     */
    private void endTad( String tag, Editable outPut){
        if (tag.equals("cs")){
            csEndIndex = outPut.length();
            outPut.setSpan(new StrikethroughSpan(),csStartIndex,csEndIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if (tag.equals("cfont")){
            cfontEndIndex = outPut.length();
            String color = attributes.get("color");
            String attr = attributes.get("size");
            String size;
            if (attr != null && attr.contains("px")){
                size = attr.split("px")[0];
            }else {
                size = "";
            }

            setAttribute(color,size, outPut,cfontStrtIndex,cfontEndIndex);
        }

    }

    private void setAttribute(String color, String size, Editable outPut, int cfontStrtIndex, int cfontEndIndex) {
        if (!TextUtils.isEmpty(color)){
            outPut.setSpan(new ForegroundColorSpan(Color.parseColor(color)),cfontStrtIndex,
                    cfontEndIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (!TextUtils.isEmpty(size)){
            outPut.setSpan(new AbsoluteSizeSpan(Integer.parseInt(size)),cfontStrtIndex,
                    cfontEndIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 解析节点属性
     * @param xmlReader
     */
    private void paseAttribute(XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[])dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer)lengthField.get(atts);

            for(int i = 0; i < len; i++){
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
