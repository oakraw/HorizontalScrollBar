package com.oakraw.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalScrollBar extends View {
    private static final String TAG = HorizontalScrollBar.class.getSimpleName();
    private static final int SCROLL_LOCATION_START = 1;
    private static final int SCROLL_LOCATION_MIDDLE = 2;
    private static final int SCROLL_LOCATION_END = 3;

    private RecyclerView mRecyclerView;
    private OnTransformersScrollListener onTransformersScrollListener;
    private int mWidth;
    private int mHeight;

    private Paint mPaint = new Paint();
    private RectF mTrackRectF = new RectF();
    private RectF mThumbRectF = new RectF();
    private float radius;
    private int mTrackColor;
    private int mThumbColor;
    private static final int DEFAULT_TRACK_COLOR = Color.parseColor("#f0f0f0");
    private static final int DEFAULT_THUMB_COLOR = Color.parseColor("#ffc107");
    private static final int DEFAULT_SCROLL_BAR_WIDTH = 48;//dp
    private static final int DEFAULT_SCROLL_BAR_HEIGHT = 3;//dp
    private float mThumbScale = 0f;
    private float mScrollScale = 0f;
    private int mScrollLocation = SCROLL_LOCATION_START;
    private boolean scrollBySelf;

    public void setScrollBySelf(boolean bySelf) {
        this.scrollBySelf = bySelf;
    }

    public void setOnTransformersScrollListener(OnTransformersScrollListener listener) {
        this.onTransformersScrollListener = listener;
    }

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (onTransformersScrollListener != null) {
                onTransformersScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            computeScrollScale();
            if (scrollBySelf && mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
                scrollBySelf = false;
            }
            if (onTransformersScrollListener != null) {
                onTransformersScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    };

    public HorizontalScrollBar(Context context) {
        this(context, null);
    }

    public HorizontalScrollBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(context, attrs);
        init();
    }

    private void init() {
        initPaint();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollBar);

        float scrollBarRadius = array.getDimensionPixelSize(R.styleable.HorizontalScrollBar_scrollbarRadius, -1);
        int scrollBarTrackColor = array.getColor(R.styleable.HorizontalScrollBar_scrollbarTrackColor, DEFAULT_TRACK_COLOR);
        int scrollBarThumbColor = array.getColor(R.styleable.HorizontalScrollBar_scrollbarThumbColor, DEFAULT_THUMB_COLOR);
        array.recycle();

        if (scrollBarRadius < 0) {
            scrollBarRadius = dp2px(DEFAULT_SCROLL_BAR_HEIGHT) / 2f;
        }


        setTrackColor(scrollBarTrackColor)
                .setThumbColor(scrollBarThumbColor)
                .setRadius(scrollBarRadius)
                .applyChange();

    }


    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void attachRecyclerView(RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(mScrollListener);
            mRecyclerView.addOnScrollListener(mScrollListener);
            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                    computeScrollScale();
                    return true;
                }
            });
        }
    }

    public HorizontalScrollBar setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public HorizontalScrollBar setTrackColor(@ColorInt int color) {
        this.mTrackColor = color;
        return this;
    }

    public HorizontalScrollBar setThumbColor(@ColorInt int color) {
        this.mThumbColor = color;
        return this;
    }

    public void applyChange() {
        postInvalidate();
    }

    public void computeScrollScale() {
        if (mRecyclerView == null) return;
        float mScrollExtent = mRecyclerView.computeHorizontalScrollExtent();
        float mScrollRange = mRecyclerView.computeHorizontalScrollRange();
        if (mScrollRange != 0) {
            mThumbScale = mScrollExtent / mScrollRange;
        }

        float canScrollDistance = mScrollRange - mScrollExtent;

        float mScrollOffset = mRecyclerView.computeHorizontalScrollOffset();
        if (mScrollRange != 0) {
            mScrollScale = mScrollOffset / mScrollRange;
        }
        if (mScrollOffset == 0) {
            mScrollLocation = SCROLL_LOCATION_START;
        } else if (canScrollDistance == mScrollOffset) {
            mScrollLocation = SCROLL_LOCATION_END;
        } else {
            mScrollLocation = SCROLL_LOCATION_MIDDLE;
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);

        drawThumb(canvas);
    }

    private void drawThumb(Canvas canvas) {
        initPaint();
        mPaint.setColor(mThumbColor);
        float left = mScrollScale * mWidth;
        float right = left + mWidth * mThumbScale;
        switch (mScrollLocation) {
            case SCROLL_LOCATION_START:
                mThumbRectF.set(0, 0, right, mHeight);
                break;
            case SCROLL_LOCATION_MIDDLE:
                mThumbRectF.set(left, 0, right, mHeight);
                break;
            case SCROLL_LOCATION_END:
                mThumbRectF.set(left, 0, mWidth, mHeight);
                break;
        }
        canvas.drawRoundRect(mThumbRectF, radius, radius, mPaint);
    }

    private void drawTrack(Canvas canvas) {
        initPaint();
        mPaint.setColor(mTrackColor);
        mTrackRectF.set(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(mTrackRectF, radius, radius, mPaint);
    }

    private int dp2px(float dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}