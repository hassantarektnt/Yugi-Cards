package com.enterprises.wayne.yugicards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ahmed on 9/3/2016.
 */
public class ScaledImage extends ImageView
{


    public ScaledImage(Context context)
    {
        super(context);
    }

    public ScaledImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScaledImage(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = width * 500 / 350;
        setMeasuredDimension(width, height);
    }
}
