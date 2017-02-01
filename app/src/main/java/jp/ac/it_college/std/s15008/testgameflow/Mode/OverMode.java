package jp.ac.it_college.std.s15008.testgameflow.Mode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;

import jp.ac.it_college.std.s15008.testgameflow.GameView;

/**
 * Created by s15008 on 17/01/31.
 */

public class OverMode {
    private static final String TAG = "OverMode";

    private Context mContext;

    public GameView.Mode mCurrentMode;
    public GameView.Mode mNextMode;

    private float mTouchX;
    private float mTouchY;

    // 画像イメージ
    private Bitmap mBitmapLogo;
    private MyImageButton mButtonLogo;
    private MyImageButton mButtonContinue;
    private MyImageButton mButtonTitleBack;

    // 音声イメージ
    private final int SOUND_MAX = 1;
    private SoundPool mSoundPool;
    private int mSoundWolf;

    // デバッグ用
    Paint paintText;
    Paint paintBackground;

    /**
     * イメージボタンクラス
     */
    class MyImageButton {
        private final Paint mPaint;
        private Bitmap mBitmap;
        private Rect mRect;

        // アニメーション用
        private ValueAnimator mValueAnimation;
        private boolean mIsAnimate;
        private Rect mRectOrigin;

        public MyImageButton(Canvas canvas, Bitmap bitmap, int x, int y, int width, int height) {
            mBitmap = bitmap;
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.STROKE);
            mRect = new Rect(x, y, (x + width), (y + height));

            mValueAnimation = null;
            mIsAnimate = false;
            mRectOrigin = null;
        }

        public void action() {
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, mRect.left, mRect.top, mPaint);
            canvas.drawRect(mRect, mPaint);
        }
    }



    public OverMode(Context context) {
        mContext = context;

        // デバッグ用
        paintText = new Paint();
        paintText.setColor(Color.RED);
        paintText.setTextSize(25);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintBackground = new Paint();
        paintBackground.setColor(Color.argb(200, 0, 0, 0));

        // サウンドイメージ
        mSoundPool = new SoundPool(SOUND_MAX, AudioManager.STREAM_MUSIC, 0);
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd("over/se_wolf.mp3");
            mSoundWolf = mSoundPool.load(assetFileDescriptor, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentMode = GameView.Mode.OVER;
        mNextMode = mCurrentMode;
    }

    // 初期化処理
    public void init(Canvas canvas) {
        mCurrentMode = GameView.Mode.OVER;
        mNextMode = mCurrentMode;

        // ロゴの位置の初期化
        mButtonLogo.mRect.set(mButtonLogo.mRectOrigin);

        // ロゴアニメーション設定(右スライドイン)
        mButtonLogo.mRectOrigin = new Rect(mButtonLogo.mRect);
        mButtonLogo.mValueAnimation = ValueAnimator.ofInt(0, canvas.getWidth()/2 + mButtonLogo.mRect.width() / 2);
        mButtonLogo.mValueAnimation.setDuration(1000);
        mButtonLogo.mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int x = mButtonLogo.mRectOrigin.left - (int) valueAnimator.getAnimatedValue();
                int y = mButtonLogo.mRectOrigin.top;
                mButtonLogo.mRect.offsetTo(x, y);
            }
        });
        mButtonLogo.mValueAnimation.start();

        // コンティニューアニメーション設定(左スライドイン)
        mButtonContinue.mRect.set(mButtonContinue.mRectOrigin);

        // アニメーション設定
        mButtonContinue.mRectOrigin = new Rect(mButtonContinue.mRect);
        mButtonContinue.mValueAnimation = ValueAnimator.ofInt(
                0, (mButtonContinue.mRect.width() / 2) + (canvas.getWidth() / 2));
        mButtonContinue.mValueAnimation.setDuration(2000);
        mButtonContinue.mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int x = mButtonContinue.mRectOrigin.left + (int) valueAnimator.getAnimatedValue();
                int y = mButtonContinue.mRectOrigin.top;
                mButtonContinue.mRect.offsetTo(x, y);
            }
        });

        // タイトルバックアニメーション設定(左スライドイン)
        mButtonTitleBack.mRect.set(mButtonTitleBack.mRectOrigin);

        // アニメーション設定
        mButtonTitleBack.mRectOrigin = new Rect(mButtonTitleBack.mRect);
        mButtonTitleBack.mValueAnimation = ValueAnimator.ofInt(
                0, (mButtonTitleBack.mRect.width() / 2) + (canvas.getWidth() / 2));
        mButtonTitleBack.mValueAnimation.setDuration(2000);
        mButtonTitleBack.mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int x = mButtonTitleBack.mRectOrigin.left + (int) valueAnimator.getAnimatedValue();
                int y = mButtonTitleBack.mRectOrigin.top;
                mButtonTitleBack.mRect.offsetTo(x, y);
            }
        });

        // アニメーションスタート
        mButtonContinue.mValueAnimation.start();
        mButtonTitleBack.mValueAnimation.start();
    }

    /**
     * 指定した画像イメージを取得する
     * @param context
     * @param filePath
     * @return bitmap image
     */
    private static Bitmap getBitmapImageToAssets(Context context, String filePath) {
        final AssetManager assetManager = context.getAssets();
        BufferedInputStream bufferedInputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(assetManager.open(filePath));
            return BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    // 更新処理
    public void update(Canvas canvas, MotionEvent motionEvent) {
        // GameOverボタン
        if (mButtonLogo == null) {
            Bitmap bitmap = getBitmapImageToAssets(mContext, "over/logo_gameover.png");
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int x = canvas.getWidth();
            int y = (canvas.getHeight() / 3) - (height / 2);
            mButtonLogo = new MyImageButton(canvas, bitmap, x, y, width, height);

            // アニメーション設定
            mButtonLogo.mRectOrigin = new Rect(mButtonLogo.mRect);
            mButtonLogo.mValueAnimation = ValueAnimator.ofInt(0, canvas.getWidth()/2 + width / 2);
            mButtonLogo.mValueAnimation.setDuration(1000);
            mButtonLogo.mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int x = mButtonLogo.mRectOrigin.left - (int) valueAnimator.getAnimatedValue();
                    int y = mButtonLogo.mRectOrigin.top;
                    mButtonLogo.mRect.offsetTo(x, y);
                }
            });
            mButtonLogo.mValueAnimation.start();
        }

        // Continueボタン
        if (mButtonContinue == null) {
            Bitmap bitmap = getBitmapImageToAssets(mContext, "over/logo_continue.png");
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int x = -width;
            int y = (canvas.getHeight() - (canvas.getHeight() / 4)) - (height / 2) - (height);
            mButtonContinue = new MyImageButton(canvas, bitmap, x, y, width, height);


            // アニメーション設定
            mButtonContinue.mRectOrigin = new Rect(mButtonContinue.mRect);
            int distance = (mButtonContinue.mRect.width() / 2) + (canvas.getWidth() / 2);
            mButtonContinue.mValueAnimation = ValueAnimator.ofInt(0, distance);
            mButtonContinue.mValueAnimation.setDuration(2000);
            mButtonContinue.mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int x = mButtonContinue.mRectOrigin.left + (int) valueAnimator.getAnimatedValue();
                    int y = mButtonContinue.mRectOrigin.top;
                    mButtonContinue.mRect.offsetTo(x, y);
                }
            });
            mButtonContinue.mValueAnimation.start();
        }

        // TitleBackボタン
        if (mButtonTitleBack == null) {
            Bitmap bitmap = getBitmapImageToAssets(mContext, "over/logo_titleback.png");
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int x = -width;
            int y = (canvas.getHeight() - (canvas.getHeight() / 4)) - (height / 2) + (height);
            mButtonTitleBack = new MyImageButton(canvas, bitmap, x, y, width, height);


            // アニメーション設定
            mButtonTitleBack.mRectOrigin = new Rect(mButtonTitleBack.mRect);
            mButtonTitleBack.mValueAnimation = ValueAnimator.ofInt(
                    0, (mButtonTitleBack.mRect.width() / 2) + (canvas.getWidth() / 2));
            mButtonTitleBack.mValueAnimation.setDuration(2000);
            mButtonTitleBack.mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int x = mButtonTitleBack.mRectOrigin.left + (int) valueAnimator.getAnimatedValue();
                    int y = mButtonTitleBack.mRectOrigin.top;
                    mButtonTitleBack.mRect.offsetTo(x, y);
                }
            });
            mButtonTitleBack.mValueAnimation.start();
        }

        // タッチ処理
        if (motionEvent != null) {
            float touchX = motionEvent.getX();
            float touchY = motionEvent.getY();
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mTouchX = touchX;
                mTouchY = touchY;

                if (mButtonLogo.mRect.contains((int) mTouchX, (int) mTouchY)) {
                    // GameOverボタンをタッチしたとき
                    mSoundPool.play(mSoundWolf, 1f, 1f, 0, 0, 1);
                    mButtonLogo.mValueAnimation.reverse();
                } else if (mButtonContinue.mRect.contains((int) mTouchX, (int) mTouchY)) {
                    // Continueボタンをタッチしたとき
                    mNextMode = GameView.Mode.INTRO;
                    mCurrentMode = mNextMode;
                } else if (mButtonTitleBack.mRect.contains((int) mTouchX, (int) mTouchY)) {
                    Toast.makeText(mContext, "タイトルアクティビティに遷移する", Toast.LENGTH_SHORT).show();
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            }
        }
//        mNextMode = GameView.Mode.INTRO;
    }

    // 描画処理
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintBackground);
        canvas.drawText(
                String.format("TouchX : %f\nTouchY : %f", mTouchX, mTouchY),
                canvas.getWidth()/2, canvas.getHeight()/2 + 100, paintText);

        mButtonLogo.draw(canvas);
        mButtonContinue.draw(canvas);
        mButtonTitleBack.draw(canvas);
    }
}
