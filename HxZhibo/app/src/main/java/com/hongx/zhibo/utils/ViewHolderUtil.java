package com.hongx.zhibo.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 稀疏数组
 */
public class ViewHolderUtil {

    public static <T extends View> T getView(View convertView, int id) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (null == holder) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }
        View view = holder.get(id);
        if (null == view) {
            view = convertView.findViewById(id);
            holder.put(id, view);
        }
        return (T) view;
    }
}