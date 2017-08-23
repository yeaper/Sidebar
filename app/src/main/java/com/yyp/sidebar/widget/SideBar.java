package com.yyp.sidebar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yyp.sidebar.R;

/**
 * 自定义 Sidebar
 */
public class SideBar extends View {
    //触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    //26个字母
    public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };

    private int choose = -1;// 选中的字母下标，默认都未选中
    private Paint paint = new Paint();

    private TextView mTextDialog;
    private Context ctx;
    int[] textLoc = new int[2];

    Animation fade_in;

    public void setTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ctx = context;
        fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
    }

    public SideBar(Context context) {
        super(context);
        this.ctx = context;
        fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
    }

    /**
     * 重绘界面
     * 1、每个字母均分高度
     * 2、为选中、未选中的字母设置不同样式
     * 3、计算对应字母的x、y坐标，并进行绘制
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        // 计算每一个字母的高度
        int singleHeight = height / b.length;

        for (int i = 0; i < b.length; i++) {
            // 设置字母未选中状态的画笔样式
            paint.setColor(Color.rgb(33, 65, 98));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            // 设置字母选中状态的画笔样式
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true); // 粗体
            }
            // x坐标 = 中间 - 字母宽度的一半
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            // y坐标 随高度增加递增
            float yPos = singleHeight * i + singleHeight;
            // 在对应位置画出字母
            canvas.drawText(b[i], xPos, yPos, paint);
            // 重置画笔
            paint.reset();
        }

    }


    /**
     * 在事件分发时，进行处理
     * 1、每获取到一次事件，就计算选中的字母下标 event.getY() / getHeight() * b.length
     * 2、手指按下时，设置bar背景，提示字母淡入并显示；
     *    手指移动时，提示字母重新布局，设置要提示的字母，调用监听接口，重绘字母表。
     * 3、手指离开时，设置bar背景，提示字母隐藏，重绘字母表
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 点击的 y 坐标
        final float y = event.getY();
        // 点击 y 坐标所占总高度的比例 * b数组的长度 = 点击b中的个数
        final int c = (int) (y / getHeight() * b.length);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 设置bar背景
                setBackgroundResource(R.drawable.sidebar_press_bg);
                mTextDialog.startAnimation(fade_in);
                mTextDialog.setVisibility(View.VISIBLE);
            case MotionEvent.ACTION_MOVE:
                // 重新布局提示字母的位置
                mTextDialog.getLocationOnScreen(textLoc);
                mTextDialog.layout(textLoc[0], (int)y,
                        textLoc[0]+mTextDialog.getWidth(),
                        (int) y + mTextDialog.getHeight());
                // 设置提示的字母
                if (c >= 0 && c < b.length) {
                    // 调用字母选中的接口
                    if (onTouchingLetterChangedListener != null) {
                        onTouchingLetterChangedListener.onTouchingLetterChanged(b[c]);
                    }
                    if (mTextDialog != null) {
                        mTextDialog.setText(b[c]);
                    }

                    // 记录选中的下标，便于绘制
                    choose = c;
                    // 重绘
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP: // 手指离开屏幕时
                setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
                // 默认未选中
                choose = -1;
                // 重绘
                invalidate();
                if (mTextDialog != null) {
                    // 手指离开后隐藏字母提示，清除动画
                    mTextDialog.setVisibility(View.INVISIBLE);
                    mTextDialog.clearAnimation();
                }
                break;
        }
        return true;
    }

    /**
     * 触摸字母的监听方法
     * 
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 触摸字母的监听接口
     * 
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}