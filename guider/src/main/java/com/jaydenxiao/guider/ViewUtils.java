package com.jaydenxiao.guider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

/**
 * * des:测量位置
 * Created by jaydenxiao
 * on 2016.08.11:59
 */
public class ViewUtils {
    private static final String FRAGMENT_CON = "NoSaveStateFrameLayout";

    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }
        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }
        Rect result = new Rect();
        Rect tmpRect = new Rect();

        View tmp = child;

        if (child == parent) {
            child.getHitRect(result);
            return result;
        }
        while (tmp != decorView && tmp != parent) {
            //找到控件占据的矩形区域的矩形坐标
            tmp.getHitRect(tmpRect);
            if (!tmp.getClass().equals(FRAGMENT_CON)) {
                result.left += tmpRect.left;
                result.top += tmpRect.top;
            }
            tmp = (View) tmp.getParent();
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }
    
       /**
     * Rect在屏幕上去掉状态栏高度的绝对位置
     */
    public static Rect getViewAbsRect(Activity activity, View view) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        int parentX = 0;
        int parentY;
        final int[] loc = new int[2];
        content.getLocationInWindow(loc);
        parentY = loc[1];//通知栏的高度
        if (parentY == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                parentY = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        view.getLocationInWindow(loc);
        Rect rect = new Rect();
        rect.set(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());
        rect.offset(-parentX, -parentY);
        return rect;
    }
}
