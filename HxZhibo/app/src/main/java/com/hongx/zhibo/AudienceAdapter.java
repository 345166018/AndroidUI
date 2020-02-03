package com.hongx.zhibo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 观众列表适配器
 * 
 */
public class AudienceAdapter extends BaseAdapter {

    private Context mContext;

    public AudienceAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 1000;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	return View.inflate(mContext, R.layout.item_audienceadapter, null);
    }
}