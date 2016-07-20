package com.lynn.code.miuiweatherchartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.List;

/**
 * Created by Lynn on 7/12/16.
 */

public class WeatherChartView extends HorizontalScrollView {
    private WeatherView mChild;
    //text Color
    private int mTextColor;
    //fill color
    private int mFillColor;

    public WeatherChartView(Context context) {
        this(context, null);
    }

    public WeatherChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeatherChartView);
        if (typedArray != null) {
            mFillColor = typedArray.getColor(R.styleable.WeatherChartView_fillColor, Color.parseColor("#000000"));
            mTextColor = typedArray.getColor(R.styleable.WeatherChartView_textColor, Color.parseColor("#000000"));
            typedArray.recycle();
        } else {
            mFillColor = Color.parseColor("#000000");
            mTextColor = Color.parseColor("#000000");
        }

        init();
    }

    @Override
    public void fling(int velocityX) {
        super.fling((int) (velocityX * 0.8));
    }

    private void init() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            //disable hareware acceleration
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        setHorizontalScrollBarEnabled(false);
//        setSmoothScrollingEnabled(true);
        setOverScrollMode(OVER_SCROLL_NEVER);
//        setFillViewport(true);
        mChild = new WeatherView(getContext());

        mChild.setFillColor(mFillColor);
        mChild.setTextColor(mTextColor);
        //add to scrollview
        addView(mChild);
    }

    public void setWeathers(List<WNode> weathers) {
        if (mChild != null) {
            mChild.setWeathers(weathers);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mChild != null) {
            mChild.onScrollVisibleChanged(l);
        }
    }
}
