package com.hongx.qqzone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private QQHeaderSrollView qqHeaderSrollView;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qqHeaderSrollView = findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new String[]{
                        "星期一 	和马云洽谈",
                        "星期二	约见李彦宏",
                        "星期三 	约见乔布斯",
                        "星期四 	和Lance钓鱼",
                        "星期五 	和Jett洽谈",
                        "星期六 	和Jason洽谈",
                        "星期日 	和MZ洽谈",
                        "星期一 	和马云洽谈",
                        "星期二	约见李彦宏",
                        "星期三 	约见乔布斯",
                        "星期四 	和Ricky钓鱼",
                        "星期五 	和David洽谈",
                        "星期六 	和Jason洽谈",
                        "星期日 	和MZ洽谈",
                        "……"
                }
        );
        //获取到头部的View
        View header = View.inflate(this, R.layout.listview_header, null);
        //获取到头部的View的图片控件
        iv = header.findViewById(R.id.layout_header_image);
        //将imageView传入到ListView中
        qqHeaderSrollView.setZoomImageView(iv);
        //将头部的View设置给ListView的HeaderView
        qqHeaderSrollView.addHeaderView(header);
        //设置适配器
        qqHeaderSrollView.setAdapter(adapter);

    }
}
