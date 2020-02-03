package com.hongx.zhibo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hongx.zhibo.utils.MagicTextView;

/**
 * @author: fuchenming
 * @create: 2020-02-02 10:39
 */
public class MagicTextViewActivity extends AppCompatActivity {


    private TranslateAnimation inAnim;
    private NumberAnim giftNumberAnim;
    private MagicTextView mtv_giftNum;
    int count = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic);

        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in); // 礼物进入时动画


        mtv_giftNum = findViewById(R.id.mtv_giftNum);
//        int giftCount = (int) mtv_giftNum.getTag() + 1; // 递增

        mtv_giftNum.setText("x" + count);

        giftNumberAnim = new NumberAnim(); // 初始化数字动画

        mtv_giftNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                mtv_giftNum.setText("x" + count);
                giftNumberAnim.showAnimator(mtv_giftNum);
            }
        });

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
}
