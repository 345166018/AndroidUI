package com.hongx.zhibo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hongx.zhibo.utils.DisplayUtil;
import com.hongx.zhibo.utils.HorizontalListView;
import com.hongx.zhibo.utils.MagicTextView;
import com.hongx.zhibo.utils.SoftKeyBoardListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: fuchenming
 * @create: 2020-02-01 15:50
 */
public class LayerFrag extends Fragment implements View.OnClickListener {

    // 礼物
    private int[] GiftIcon = new int[]{R.drawable.zem72,
            R.drawable.zem70,
            R.drawable.zem68,
            R.drawable.zem63};

    private NumberAnim giftNumberAnim;
    private List<String> messageData = new LinkedList<>();
    private MessageAdapter messageAdapter;
    private ListView lv_message;
    private HorizontalListView hlv_audience;
    private LinearLayout ll_gift_group;
    private TranslateAnimation outAnim;
    private TranslateAnimation inAnim;
    private LinearLayout ll_inputparent;
    private Button tv_chat;
    private TextView tv_send;
    private EditText et_chat;
    private LinearLayout ll_anchor;
    private RelativeLayout rl_num;

    private Context myContext;
    private Button btn_gift01;
    private Button btn_gift02;
    private Button btn_gift03;
    private Button btn_gift04;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_layer, null);
        myContext = getActivity();
        initView(view);
        initData();
        return view;
    }


    public void initView(View view) {
        lv_message = view.findViewById(R.id.lv_message);
        hlv_audience = view.findViewById(R.id.hlv_audience);
        ll_gift_group = view.findViewById(R.id.ll_gift_group);
        ll_inputparent = view.findViewById(R.id.ll_inputparent);
        tv_chat = view.findViewById(R.id.tv_chat);
        tv_send = view.findViewById(R.id.tv_send);
        et_chat = view.findViewById(R.id.et_chat);
        ll_anchor = view.findViewById(R.id.ll_anchor);
        rl_num = view.findViewById(R.id.rl_num);
        btn_gift01 = view.findViewById(R.id.btn_gift01);
        btn_gift02 = view.findViewById(R.id.btn_gift02);
        btn_gift03 = view.findViewById(R.id.btn_gift03);
        btn_gift04 = view.findViewById(R.id.btn_gift04);
    }

    private void initData() {
        initAudience(); // 初始化观众
        initMessage(); // 初始化评论
        initListener();
        clearTiming(); // 开启定时清理礼物列表
        initAnim(); // 初始化动画
    }

    /**
     * 初始化观众列表
     */
    private void initAudience() {
        hlv_audience.setAdapter(new AudienceAdapter(myContext));
    }

    /**
     * 初始化评论列表
     */
    private void initMessage() {
        for (int x = 0; x < 20; x++) {
            messageData.add("小明: 主播好漂亮啊" + x);
        }
        messageAdapter = new MessageAdapter(getActivity(), messageData);
        lv_message.setAdapter(messageAdapter);
        lv_message.setSelection(messageData.size());
    }

    private void initListener() {
        btn_gift01.setOnClickListener(this);
        btn_gift02.setOnClickListener(this);
        btn_gift03.setOnClickListener(this);
        btn_gift04.setOnClickListener(this);
        tv_chat.setOnClickListener(this);
        tv_send.setOnClickListener(this);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ll_inputparent.getVisibility() == View.VISIBLE) {
                    tv_chat.setVisibility(View.VISIBLE);
                    ll_inputparent.setVisibility(View.GONE);
                    hideKeyboard();
                }
            }
        });

        // 软键盘监听
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/

                // 输入文字时的界面退出动画
                AnimatorSet animatorSetHide = new AnimatorSet();
                ObjectAnimator leftOutAnim = ObjectAnimator.ofFloat(rl_num, "translationX", 0, -rl_num.getWidth());
                ObjectAnimator topOutAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", 0, -ll_anchor.getHeight());
                animatorSetHide.playTogether(leftOutAnim, topOutAnim);
                animatorSetHide.setDuration(300);
                animatorSetHide.start();
                // 改变listview的高度
                dynamicChangeListviewH(90);
                dynamicChangeGiftParentH(true);
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                tv_chat.setVisibility(View.VISIBLE);
                ll_inputparent.setVisibility(View.GONE);
                // 输入文字时的界面进入时的动画
                AnimatorSet animatorSetShow = new AnimatorSet();
                ObjectAnimator leftInAnim = ObjectAnimator.ofFloat(rl_num, "translationX", -rl_num.getWidth(), 0);
                ObjectAnimator topInAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", -ll_anchor.getHeight(), 0);
                animatorSetShow.playTogether(leftInAnim, topInAnim);
                animatorSetShow.setDuration(300);
                animatorSetShow.start();

                // 改变listview的高度
                dynamicChangeListviewH(150);
                dynamicChangeGiftParentH(false);
            }
        });

    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        giftNumberAnim = new NumberAnim(); // 初始化数字动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in); // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out); // 礼物退出时动画
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gift01: // 礼物1,送香皂
                showGift("gift01");
                break;
            case R.id.btn_gift02: // 礼物2,送玫瑰
                showGift("gift02");
                break;
            case R.id.btn_gift03: // 礼物3,送爱心
                showGift("gift03");
                break;
            case R.id.btn_gift04: // 礼物4,送蛋糕
                showGift("gift04");
                break;
            case R.id.tv_chat:// 聊天
                tv_chat.setVisibility(View.GONE);
                ll_inputparent.setVisibility(View.VISIBLE);
                ll_inputparent.requestFocus(); // 获取焦点
                showKeyboard();
                break;
            case R.id.tv_send:// 发送消息
                String chatMsg = et_chat.getText().toString();
                if (!TextUtils.isEmpty(chatMsg)) {
                    messageData.add("小明: " + chatMsg);
                    et_chat.setText("");
                    messageAdapter.NotifyAdapter(messageData);
                    lv_message.setSelection(messageData.size());
                }
                hideKeyboard();
                break;
        }
    }

    public class NumberAnim {
        private Animator lastAnimator;

        public void showAnimator(View v) {

            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.cancel();
                lastAnimator.end();
            }
            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.3f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY);
            animSet.setDuration(200);
            lastAnimator = animSet;
            animSet.start();
        }
    }

    /**
     * 刷礼物的方法
     */
    private void showGift(String tag) {
        View newGiftView = ll_gift_group.findViewWithTag(tag);
        // 是否有该tag类型的礼物
        if (newGiftView == null) {
            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group.getChildCount() >= 3) {
                // 获取前2个元素的最后更新时间
                View giftView01 = ll_gift_group.getChildAt(0);
                ImageView iv_gift01 = giftView01.findViewById(R.id.iv_gift);
                long lastTime1 = (long) iv_gift01.getTag();

                View giftView02 = ll_gift_group.getChildAt(1);
                ImageView iv_gift02 = giftView02.findViewById(R.id.iv_gift);
                long lastTime2 = (long) iv_gift02.getTag();

                if (lastTime1 > lastTime2) { // 如果第二个View显示的时间比较长
                    removeGiftView(1);
                } else { // 如果第一个View显示的时间长
                    removeGiftView(0);
                }
            }

            // 获取礼物
            newGiftView = getNewGiftView(tag);
            ll_gift_group.addView(newGiftView);

            // 播放动画
            newGiftView.startAnimation(inAnim);
            final MagicTextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            inAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumberAnim.showAnimator(mtv_giftNum);
                }
            });
        } else {
            // 如果列表中已经有了该类型的礼物，则不再新建，直接拿出
            // 更新标识，记录最新修改的时间，用于回收判断
            ImageView iv_gift = newGiftView.findViewById(R.id.iv_gift);
            iv_gift.setTag(System.currentTimeMillis());

            // 更新标识，更新记录礼物个数
            MagicTextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            int giftCount = (int) mtv_giftNum.getTag() + 1; // 递增
            mtv_giftNum.setText("x" + giftCount);
            mtv_giftNum.setTag(giftCount);
            giftNumberAnim.showAnimator(mtv_giftNum);
        }
    }


    /**
     * 获取礼物
     */
    private View getNewGiftView(String tag) {

        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        View giftView = LayoutInflater.from(myContext).inflate(R.layout.item_gift, null);
        giftView.setTag(tag);

        // 添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        ImageView iv_gift = giftView.findViewById(R.id.iv_gift);
        iv_gift.setTag(System.currentTimeMillis());

        // 添加标识，记录礼物个数
        MagicTextView mtv_giftNum = giftView.findViewById(R.id.mtv_giftNum);
        mtv_giftNum.setTag(1);
        mtv_giftNum.setText("x1");

        switch (tag) {
            case "gift01":
                iv_gift.setImageResource(GiftIcon[0]);
                break;
            case "gift02":
                iv_gift.setImageResource(GiftIcon[1]);
                break;
            case "gift03":
                iv_gift.setImageResource(GiftIcon[2]);
                break;
            case "gift04":
                iv_gift.setImageResource(GiftIcon[3]);
                break;
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        giftView.setLayoutParams(lp);

        return giftView;
    }


    /**
     * 移除礼物列表里的giftView
     */
    private void removeGiftView(final int index) {
        // 移除列表，外加退出动画
        final View removeGiftView = ll_gift_group.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_gift_group.removeViewAt(index);
            }
        });

        // 开启动画，因为定时原因，所以可能是在子线程
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeGiftView.startAnimation(outAnim);
            }
        });
    }

    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                int childCount = ll_gift_group.getChildCount();
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < childCount; i++) {

                    View childView = ll_gift_group.getChildAt(i);
                    ImageView iv_gift = (ImageView) childView.findViewById(R.id.iv_gift);
                    long lastUpdateTime = (long) iv_gift.getTag();

                    // 更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 3000) {
                        removeGiftView(i);
                    }
                }
            }
        }, 0, 3000);
    }

    /**
     * 显示软键盘
     */
    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_chat, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_chat.getWindowToken(), 0);
    }


    /**
     * 动态的修改listview的高度
     */
    private void dynamicChangeListviewH(int heightPX) {
        ViewGroup.LayoutParams layoutParams = lv_message.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getActivity(), heightPX);
        lv_message.setLayoutParams(layoutParams);
    }

    /**
     * 动态修改礼物父布局的高度
     */
    private void dynamicChangeGiftParentH(boolean showhide) {
        if (showhide) {// 如果软键盘显示中
            if (ll_gift_group.getChildCount() != 0) {

                // 判断是否有礼物显示，如果有就修改父布局高度，如果没有就不作任何操作
                ViewGroup.LayoutParams layoutParams = ll_gift_group.getLayoutParams();
                layoutParams.height = ll_gift_group.getChildAt(0).getHeight();
                ll_gift_group.setLayoutParams(layoutParams);
            }
        } else {
            // 如果软键盘隐藏中
            // 就将装载礼物的容器的高度设置为包裹内容
            ViewGroup.LayoutParams layoutParams = ll_gift_group.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ll_gift_group.setLayoutParams(layoutParams);
        }
    }

}
