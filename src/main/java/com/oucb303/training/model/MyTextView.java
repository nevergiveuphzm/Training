package com.oucb303.training.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oucb303.training.R;

/**
 * Created by HP on 2017/8/15.
 */
public class MyTextView extends TextView {
    private Bitmap mBitmap;

    /**
     * @param context
     * @param attrs
     */
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.champion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, getPaint());
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
