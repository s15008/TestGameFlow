package jp.ac.it_college.std.s15008.testgameflow.Mode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import jp.ac.it_college.std.s15008.testgameflow.GameView;

/**
 * Created by s15008 on 17/01/30.
 */

public class IntroMode {
    private static final String TAG = "IntroMode";

    public GameView.Mode mCurrentMode;
    public GameView.Mode mNextMode;

    private float mTouchX;
    private float mTouchY;


    // デバッグ用
    Paint paintText;
    Paint paintBackground;


    class MyButton {
        private final Paint mRectPaint;
        private Rect mRect;
        private int mWidth;
        private int mHeight;

        public MyButton(int x, int y) {
            mRectPaint = new Paint(Color.BLUE);
            mWidth = 300;
            mHeight = 250;
            this.mRect = new Rect(x, y, x+mWidth, y+mHeight);
        }

        public void draw(Canvas canvas) {
            canvas.drawRect(mRect, mRectPaint);
        }
    }
    MyButton mMyButton;


    public IntroMode() {
        paintText = new Paint();
        paintText.setColor(Color.RED);
        paintText.setTextSize(25);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintBackground = new Paint();
        paintBackground.setColor(Color.argb(200, 0, 0, 0));

        init();
    }

    // 初期化処理
    public void init() {
        mCurrentMode = GameView.Mode.INTRO;
        mNextMode = mCurrentMode;
    }

    // 更新処理
    public void update(Canvas canvas, MotionEvent motionEvent) {

        if (mMyButton == null) {
            int x = (canvas.getWidth() / 2) - (300 / 2);
            int y = (canvas.getHeight() / 2) - (250 / 2);
            mMyButton = new MyButton(x, y - (canvas.getHeight() / 4));
        }

        // タッチ処理
        if (motionEvent != null) {
            float touchX = motionEvent.getX();
            float touchY = motionEvent.getY();
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mTouchX = touchX;
                mTouchY = touchY;
                if (mMyButton.mRect.contains((int) mTouchX, (int) mTouchY)) {
                    mNextMode = GameView.Mode.GAME;
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mTouchX = touchX;
                mTouchY = touchY;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = touchX;
                mTouchY = touchY;
            }
        }
//        mNextMode = GameView.Mode.INTRO;
    }

    // 描画処理
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintBackground);
        canvas.drawText("Intro Mode", canvas.getWidth()/2, canvas.getHeight()/2, paintText);
        canvas.drawText(
                String.format("TouchX : %f  TouchY : %f", mTouchX, mTouchY),
                canvas.getWidth()/2, canvas.getHeight() - paintText.getTextSize() * 2, paintText);

        mMyButton.draw(canvas);
    }
}
