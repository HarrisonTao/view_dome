package com.dykj.module.util.webview;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Stack;

/**
 * Created by lcjing on 2019/3/18.
 */

public class MyTagHandler implements Html.TagHandler {

    private Context context;

    public MyTagHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (opening) {
            if (tag.equalsIgnoreCase("size")) {
                handlerStartSIZE(output, xmlReader);
            }
        } else {
            if (tag.equalsIgnoreCase("size")) {
                handlerEndSIZE(output,context);
            }
        }
    }


    //自定义<size>标签----开始----
    public void handlerStartSIZE(Editable output, XMLReader xmlReader) {
        if (startIndex == null) {
            startIndex = new Stack<>();
        }
        startIndex.push(output.length());
        if (propertyValue == null) {
            propertyValue = new Stack<>();
        }

        propertyValue.push(getProperty(xmlReader, "value"));

    }

    //自定义<size>标签====结束====
    public void handlerEndSIZE(Editable output, Context context) {
        if (!isEmpty(propertyValue)) {
            try {
                int value = Integer.parseInt(propertyValue.pop());
                output.setSpan(new AbsoluteSizeSpan(sp2px(context, value)), startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获取html标签的 value

    private String getProperty(XMLReader xmlReader, String property) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);

            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);

            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            for (int i = 0; i < len; i++) {
                if (property.equals(data[i * 5 + 1])) {
                    return data[i * 5 + 4];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Handler mHandler = new Handler();
    private String TAG = "TAG";
    private Stack<Integer> startIndex;
    private Stack<String> propertyValue;


    //判断是否为空
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static int sp2px(Context context, float spValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics()) + 0.5f);
    }

}
