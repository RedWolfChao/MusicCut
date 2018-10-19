package icbc.com.musiccut.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created By RedWolf on 2018/10/19 9:52
 * 频谱.java
 */

public class SpectrumView extends View {
    private Paint mPaint;

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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();


    }
}
