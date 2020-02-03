package com.hongx.zhibo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 观众功能交互页面, 滑动隐藏效果
 */
public class InteractiveFrag extends DialogFragment {

    public View view;
    public Context myContext;
    private ViewPager vp_interactive;
    private LayerFrag layerFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_interactive, null);
        // 初始化
        initView();
        initData();
        return view;
    }

    /**
     * 初始化View
     */
    public void initView() {
        vp_interactive = view.findViewById(R.id.vp_interactive);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        // EmptyFrag：什么都没有
        // LayerFrag：交互界面
        // 这样就达到了滑动隐藏交互的需求
        vp_interactive.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new EmptyFrag(); // 返回空界面的fragment
                } else if (position == 1) {
                    return layerFrag = new LayerFrag(); // 返回交互界面的frag
                } else { // 设置默认

                    return new EmptyFrag();
                }
            }
        });
        // 设置默认显示交互界面
        vp_interactive.setCurrentItem(1);

        // 同时将界面改为resize已达到软键盘弹出时Fragment不会跟随移动
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 设置DialogFragment的样式，这里的代码最好还是用我的，大家不要改动
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog) {

            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }
}