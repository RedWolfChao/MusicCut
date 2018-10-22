package icbc.com.musiccut.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import icbc.com.musiccut.R;

/**
 * Created By RedWolf on 2018/10/19 9:52
 * 频谱.java
 */

public class SpectrumView extends View {
    private Paint mPaint;
    private Path mPath;
    private boolean mIsPlaying;

    public SpectrumView(Context context) {
        this(context, null);

    }

    public SpectrumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpectrumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mPath = new Path();

    }

    private void drawSpectrum(byte[] points) {
        mPath.reset();
        float height = getHeight();
        float width = getWidth();
        float widthScale = width / points.length;
        List<Float> widthPointList = new ArrayList<>();
        float dPoint = 0;
        for (byte ignored : points) {
            widthPointList.add(dPoint += widthScale);
        }
        float heightScale = height / 256;
        //  中心点开始
        for (int i = 0; i < points.length; i++) {
            float point = points[i];
            if (point > 0) {
                point = point * heightScale;
            } else {
                point = height + (point * heightScale);
            }
            if (i == 0) {
                mPath.moveTo(widthPointList.get(i), point);
                continue;
            }
            mPath.lineTo(widthPointList.get(i), point);
        }
    }

    public void stopDraw() {
        mIsPlaying = false;
    }

    public void startDraw(byte[] points) {
        if (!mIsPlaying) {
            mIsPlaying = true;
        }
        drawSpectrum(points);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
