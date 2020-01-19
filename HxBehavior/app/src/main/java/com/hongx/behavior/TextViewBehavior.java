package com.hongx.behavior;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: fuchenming
 * @create: 2020-01-19 13:17
 */
public class TextViewBehavior extends MyBehavior {

    public TextViewBehavior(Context context) {
        super(context);
    }

    @Override
    public boolean layoutDependsOn(@NonNull View parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof RecyclerView;
    }


}
