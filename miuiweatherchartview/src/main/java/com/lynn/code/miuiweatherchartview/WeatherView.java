package com.lynn.code.miuiweatherchartview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * weather view
 * Created by Lynn on 7/11/16.
 */

public class WeatherView extends View {
    //default x coord offset between two node
    private static final int DEFAULT_TIME_INTERVAL = 190;
    //default dashline min height
    private static final int DEFAULT_MIN_HEIGHT = 270;
    //default height offset in a degree
    private static final int DEFAULT_HEIGHT_OFFSET = 20;
    //default height
    private static final int DEFAULT_HEIGHT = 680;

    //text Paint
    private Paint mTextPaint;
    //drawing Paint
    private Paint mDrawPaint;
    //dash effect
    private DashPathEffect mDashPathEffect;
    //text Color
    private int mTextColor;
    //fill color
    private int mFillColor;

    //dot view radius
    private int mRadius;
    //dashline min height
    private int mDashLineMinOffset;
    //X Axis y coord
    private int mXAxisOffset;
    //Y Axis height
    private int mYAxisHeight;
    //height offset of a degree;
    private int mHeightOffset;
    //internal between two dots
    private int mInternal;
    //min temperature
    private int mMinTem;
    //max temperature
    private int mMaxTem;
    //scroll x
    private float mScrollX;
    //translateX
    private int mTranslateX;

    //dot view set
    private List<WDot> mDots;
    //hint view set
    private List<WHint> mHints;
    //origin data set
    private List<WNode> mWeathers;

    private Executor mInitialExecutor;

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public WeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherView(Context context) {
        this(context, null);
    }

    private void init() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            //disable hareware acceleration
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mInitialExecutor = Executors.newSingleThreadExecutor();

        mRadius = WUtils.dp2px(getContext(), 3);
        mDashLineMinOffset = DEFAULT_MIN_HEIGHT;
        mHeightOffset = DEFAULT_HEIGHT_OFFSET;
        mInternal = DEFAULT_TIME_INTERVAL;
        //default
//        mTextColor = Color.parseColor("#000000");
//        mFillColor = Color.parseColor("#63c3d6");

        mTranslateX = 50;
        mXAxisOffset = -1;
        mMinTem = -1;
        mMaxTem = -1;

        mDots = new ArrayList<>();
        mHints = new ArrayList<>();

        mTextPaint = new TextPaint();
        mDrawPaint = new Paint();

        //dashline effect
        mDashPathEffect = new DashPathEffect(new float[]{18, 9}, 18);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(WUtils.sp2px(getContext(), 12));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int requiredWidth = getPaddingLeft() + getPaddingRight() + mTranslateX;

        if (mWeathers != null && mWeathers.size() > 0) {
            requiredWidth += mInternal * mWeathers.size();
        }

        setMeasuredDimension(measureSize(requiredWidth, widthMeasureSpec), measureSize(DEFAULT_HEIGHT, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mTranslateX, 100);

        if (mDots == null || mDots.size() <= 0) {
            return;
        }

        final int range = (mMaxTem < 0 || mMinTem < 0 || mMaxTem < mMinTem) ? 0 : (mMaxTem - mMinTem);
        mYAxisHeight = mDashLineMinOffset + range * mHeightOffset;

        int endIndex = mDots.size() - 1;
        int startIndex = 0;
        for (int i = 0; i < mDots.size(); i++) {
            if (endIndex > 0 && !isDotVisible(mDots.get(endIndex))
                    && !isDotVisible(mDots.get(endIndex - 1))) {
                //find the right most invisible dot
                endIndex--;
            }

            if (startIndex < mDots.size() - 1 && !isDotVisible(mDots.get(startIndex))
                    && !isDotVisible(mDots.get(startIndex + 1))) {
                //go on until the left most visible dot finded
                startIndex++;
                continue;
            }

            //draw a dot view
            drawDot(canvas, mDots.get(i));

            if (i < mDots.size() - 1) {
                //draw a connected line between two dots
                drawConnectedLine(canvas,
                        mDots.get(i).getX(),
                        mDots.get(i).getY(),
                        mDots.get(i + 1).getY(),
                        mDrawPaint);
            }

            if (i > endIndex) {
                //quit this loop more quickly
                break;
            }
        }

        //draw hint views
        drawHint(canvas);

        //draw the bottom line
        drawBottomLine(canvas, 0, (mDots.size() - 1) * mInternal, mXAxisOffset, mDrawPaint);
    }

    private int measureSize(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(desiredSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
            case MeasureSpec.UNSPECIFIED:
                result = desiredSize;
                break;
        }

        return result;
    }

    /**
     * draw a dot view
     */
    private void drawDot(Canvas canvas, WDot dot) {
        //offset between text and dot / bottom line
        final int offset = WUtils.dp2px(getContext(), 5);

        //draw circle dot
        drawCircleDot(canvas, dot.getX(), dot.getY(), mRadius, mDrawPaint);

        //draw temperature text
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextPaint.setColor(mTextColor);
        canvas.drawText(dot.getTemp(),
                dot.getX(),
                dot.getY() - mRadius - offset - fontMetrics.bottom,
                mTextPaint);

        if (dot.canDrawDashLine()) {
            //draw dashline
            //offset radius to avoid drawing under the dot
            drawVerticalDashLine(canvas, dot.getX(), dot.getY() + mRadius, mYAxisHeight - dot.getY(), mDrawPaint);
        }

        if (mXAxisOffset < 0) {
            //the center of the internal
            mXAxisOffset = mYAxisHeight + mRadius + 5;
        }

        //draw time text
        canvas.drawText(dot.getTime(),
                dot.getX(),
                mYAxisHeight + mRadius + 5 + offset - fontMetrics.top,
                mTextPaint);
    }

    /**
     * draw a hint view
     */
    private void drawHint(Canvas canvas) {
        if (mHints == null || mHints.size() <= 0) {
            return;
        }

        for (int i = 0; i < mHints.size(); i++) {
            WHint hint = mHints.get(i);
            if (hint == null ||
                    hint.getHintDots() == null ||
                    hint.getHintDots().size() <= 0) {
                continue;
            }

            int startIndex = -1;
            int endIndex = -1;
            int s = 0;
            int e = hint.getHintDots().size() - 1;

            //warning: make sure could always quit the loop
            while (s < e) {
                if ((startIndex >= 0) && (endIndex >= 0)) {
                    break;
                }

                if (startIndex < 0) {
                    if (isDotVisible(hint.getHintDots().get(s))) {
                        //find the left most visible index
                        startIndex = s;
                    } else {
                        s++;
                    }
                }

                if (endIndex < 0) {
                    if (isDotVisible(hint.getHintDots().get(e))) {
                        //find the right most visible index
                        endIndex = e;
                    } else {
                        e--;
                    }
                }
            }

            if (startIndex < 0 && endIndex < 0) {
                //invisible hint view, ignore
                continue;
            }

            //actual draw action
            drawHint(canvas, hint, startIndex, endIndex);

            if (endIndex != hint.getHintDots().size() - 1) {
                //if a hint view could not see the right bound
                //ignore all the other hint views on the right side
                return;
            }
        }
    }

    /**
     * actual drawing hint view process
     */
    private void drawHint(Canvas canvas, WHint hint, int startIndex, int endIndex) {
        String weather = getWeatherText(hint.getWeather());

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        Rect textRect = new Rect();
        mTextPaint.getTextBounds(weather, 0, weather.length(), textRect);

        float left = hint.getHintDots().get(0).getX();
        float right = hint.getHintDots().get(hint.getHintDots().size() - 1).getX();

        final float totalCenterX = left + (right - left) / 2;

        if (startIndex == 0) {
            if (endIndex == hint.getHintDots().size() - 1) {
                // totally visible
                hint.setCurX(totalCenterX);
            } else {
                hint.setCurX((left + WUtils.getScreenWidth(getContext()) + mScrollX - mTranslateX) / 2);
            }
        } else if (startIndex > 0) {
            if (endIndex == hint.getHintDots().size() - 1) {
                hint.setCurX((right + mScrollX - mTranslateX) / 2);
            } else {
                hint.setCurX((mScrollX - mTranslateX + WUtils.getScreenWidth(getContext()) + mScrollX - mTranslateX) / 2);
            }
        } else {
            //startIndex < 0
            if (endIndex == hint.getHintDots().size() - 1) {
                hint.setCurX((right + mScrollX - mTranslateX) / 2);
            }
        }

        float tempX = hint.getCurX();
        left += textRect.width() / 2;
        right -= textRect.width() / 2;
        //scroll bound left + textRect.wdith() / 2 <= curX <= right - textRect.width() / 2
        if (hint.getCurX() < left) {
            tempX = left;
        } else if (hint.getCurX() > right) {
            tempX = right;
        }

        Bitmap bitmap = decodeHintBitmap(getWeatherId(hint.getWeather()), textRect.width() / 2, textRect.height() / 2);
        if (bitmap != null) {
            mDrawPaint.reset();
            mDrawPaint.setAntiAlias(true);

            canvas.drawBitmap(bitmap,
                    tempX - bitmap.getWidth() / 2,
                    mXAxisOffset - mDashLineMinOffset / 5 - (fontMetrics.bottom - fontMetrics.top) - bitmap.getHeight() - textRect.height() / 2,
                    mDrawPaint);
        }

        mTextPaint.setColor(mTextColor);
        canvas.drawText(weather,
                tempX,
                mXAxisOffset - mDashLineMinOffset / 5 - fontMetrics.bottom,
                mTextPaint);
    }

    /**
     * vertical dashline
     */
    private void drawVerticalDashLine(Canvas canvas, int x, int y, int height, Paint paint) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setAlpha(255);
        paint.setPathEffect(mDashPathEffect);
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#cccccc"));
        //draw dashline
        canvas.drawLine(x, y, x, y + height, paint);
    }

    /**
     * draw circle node
     */
    private void drawCircleDot(Canvas canvas, int x, int y, int radius, Paint paint) {
        paint.reset();
        paint.setAntiAlias(true);
        //绘制外环
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(mFillColor);
        canvas.drawCircle(x, y, radius, paint);
    }

    /**
     * draw bottom line
     */
    private void drawBottomLine(Canvas canvas, int startX, int endX, int offset, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setColor(Color.parseColor("#cccccc"));
        paint.setStrokeWidth(3);
        canvas.drawLine(startX, offset, endX, offset, paint);
    }

    /**
     * draw connected line between two nodes
     */
    private void drawConnectedLine(Canvas canvas, int startX, int startY, int nextY, Paint paint) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        final float offsetY = ((nextY - startY) / mHeightOffset);
        canvas.drawLine(startX + mRadius + 8,
                startY + offsetY,
                startX + mInternal - mRadius - 8,
                nextY - offsetY,
                paint);
    }

    private int getWeatherId(@Weather int weather) {
        //TODO specify a res id from the weather state
        switch (weather) {
            case Weather.CLOUDY:
                return R.drawable.cloud;
            case Weather.RAINY:
                return R.drawable.rain;
            default:
            case Weather.SUNNY:
                return R.drawable.sun;
        }
    }

    private String getWeatherText(@Weather int weather) {
        switch (weather) {
            case Weather.CLOUDY:
                return "多云";
            case Weather.RAINY:
                return "下雨";
            default:
            case Weather.SUNNY:
                return "晴天";
        }
    }

    private Bitmap decodeHintBitmap(int id, int maxWidth, int maxHeight) {
        BitmapFactory.Options bound = new BitmapFactory.Options();
        bound.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getContext().getResources(), id, bound);
        if (bound.outWidth < 0) {
            //decode error
            return null;
        }

        bound.inSampleSize = caculateInSampleSize(bound, maxWidth, maxHeight);
        bound.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getContext().getResources(), id, bound);
    }

    private int caculateInSampleSize(BitmapFactory.Options bound, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        if (bound.outWidth > maxWidth || bound.outHeight > maxHeight) {
            float width = bound.outWidth / 2;
            float height = bound.outHeight / 2;

            while (width / inSampleSize >= maxWidth || height / inSampleSize >= maxHeight) {
                //when inSampleSize > 1, it should be a power of 2
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * initial dot view set
     */
    private void initialDots() {
        mDots.clear();

        for (int i = 0; i < mWeathers.size(); i++) {
            //computing the node's pos
            final int startX = i * mInternal;
            final int startY = ((mMaxTem < 0) ? 0 : (mMaxTem - mWeathers.get(i).getTemperature())) * mHeightOffset;

            WDot dot = new WDot();

            dot.setX(startX);
            dot.setY(startY);
            dot.setTemp(mWeathers.get(i).getTemperature() + "°");
            dot.setTime(mWeathers.get(i).getTime());

            if (i == 0 ||
                    i == mWeathers.size() - 1 ||
                    ((i > 0) && mWeathers.get(i).getWeather() != mWeathers.get(i - 1).getWeather())) {
                dot.setCanDrawDashLine(true);
            } else {
                dot.setCanDrawDashLine(false);
            }

            mDots.add(dot);
        }
    }

    /**
     * initial hint view set
     */
    private void initialHints() {
        if (mDots == null || mDots.size() <= 0) {
            return;
        }

        mHints.clear();

        WHint hintView = new WHint();
        List<WDot> dots = new ArrayList<>();
        for (int i = 0; i < mDots.size(); i++) {
            dots.add(mDots.get(i));

            if ((i > 0) && mDots.get(i).canDrawDashLine()) {
                hintView.setHintDots(dots);
                //put to hintview list
                mHints.add(hintView);
                //initial curX a negative number
                hintView.setCurX(-1);
                //initial the weather
                hintView.setWeather(mWeathers.get(i - 1).getWeather());

                //new a hintview
                hintView = new WHint();
                //new a dot set
                dots = new ArrayList<>();
                //add this dot to new dot set
                dots.add(mDots.get(i));
            }
        }
    }

    /**
     * record the max and min temperature value
     */
    private void computeMaxAndMin() {
        if (mWeathers == null || mWeathers.size() <= 0) {
            return;
        }

        mMinTem = mWeathers.get(0).getTemperature();
        mMaxTem = mWeathers.get(0).getTemperature();
        for (int i = 1; i < mWeathers.size(); i++) {
            if (mMinTem > mWeathers.get(i).getTemperature()) {
                mMinTem = mWeathers.get(i).getTemperature();
            }

            if (mMaxTem < mWeathers.get(i).getTemperature()) {
                mMaxTem = mWeathers.get(i).getTemperature();
            }
        }
    }

    public void onScrollVisibleChanged(int scrollX) {
        if (mDots == null || mDots.size() <= 0) {
            return;
        }

        //horizontal scroll offset
        mScrollX = scrollX;

        postInvalidate();
    }

    /**
     * return whether the dot is visible or not
     */
    private boolean isDotVisible(WDot dot) {
        return (mScrollX - mTranslateX) <= dot.getX()
                && dot.getX() <= (WUtils.getScreenWidth(getContext()) + (mScrollX - mTranslateX));
    }

    public void setWeathers(List<WNode> weathers) {
        this.mWeathers = weathers;

        if (mWeathers == null || mWeathers.size() <= 0) {
            //nothing to show
            return;
        }

        mInitialExecutor.execute(new Runnable() {
            @Override
            public void run() {
                computeMaxAndMin();
                initialDots();
                initialHints();
                //refresh ui
                postInvalidate();
            }
        });
    }

    public List<WNode> getWeathers() {
        return mWeathers;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(int fillColor) {
        this.mFillColor = fillColor;
    }
}
