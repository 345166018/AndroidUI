package com.hongx.zhibo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongx.zhibo.utils.ViewHolderUtil;

import java.util.List;

/**
 * 评论列表适配器
 */
public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> data;

    public MessageAdapter(Context context, List<String> data) {

        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {

        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public void NotifyAdapter(List<String> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_messageadapter, null);
        }

        // 评论
        TextView tv_msg = ViewHolderUtil.getView(convertView, R.id.tv_msg);
        tv_msg.setText(data.get(position));

        return convertView;
    }
}