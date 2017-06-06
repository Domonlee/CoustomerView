package domon.cn.coustomerview.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import domon.cn.coustomerview.R;

/**
 * Created by Domon on 2017/6/6.
 */

public class NumberProgressBar extends View {
    private int mMaxProgress = 100;

    /**
     * Current progress,can not exceed the max progress.
     */
    private int mCurrentProgress = 0;

    /*
     * The progress area bar color.
     */
    private int mReachedBarColor;

    /*
     * The bar unreadched area color.
     */
    private int mUnReachedBarColor;

    /*
     * The progress text color.
     */
    private int mTextColor;

    /*
     * The progress text size.
     */
    private float mTextSize;

    /*
     * The height of the reached area.
     */
    private float mReachedBarHeight;

    /*
     * The height of the unreached area.
     */
    private float mUnReachedBarHeight;

    /*
     * The suffix of the number.
     */
    private String mSuffix = "%";

    /*
     * The prefix .
     */
    private String mPrefix = "";

    /**
     * default value
     */
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(66, 145, 241);
    private final float default_progress_text_offset;
    private final float default_text_size;
    private final float default_reached_bar_height;
    private final float default_unreached_bar_height;

    /*
     * For save and restore instance of progressbar.
     */

    private static final String INSTANCE_SATAE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height";
    private static final String INSTANCE_REACHED_BAR_COLOR = "reached_bar_color";
    private static final String INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height";
    private static final String INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_TEXT_VISBILITY = "text_visibility";

    private static final int PROGRESS_TEXT_VISIBLE = 0;

    /*
     * The width of the text that to be drawn.
     */
    private float mDrawTextWidth;

    /*
     * The drawn text start.
     */
    private float mDrawTextStart;
    private float mDrawTextEnd;

    /*
     * The text that to be drawn in onDraw().
     */
    private String mCurrentDrawText;

    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;
    private Paint mTextPaint;

    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);

    /*
     * The progress text offset.
     */
    private float mOffset;

    /*
     * Determine if need to draw unreached area.
     */
    private boolean mDrawUnreachedBar = true;
    private boolean mDrawReachedBar = true;
    private boolean mIfDrawText = true;

    private OnProgressBarListener mListener;

    public enum ProgressTextVisibility {
        Visible, Invisible
    }

    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_reached_bar_height = dp2px(1.5f);
        default_unreached_bar_height = dp2px(1.0f);
        default_text_size = sp2px(10);
        default_progress_text_offset = dp2px(3.0f);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.NumberProgressBar, defStyleAttr, 0);

        mReachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_reached_color, default_reached_color);
        mUnReachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_unreached_color, default_unreached_color);
        mTextColor = attributes.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_text_size);

        mReachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_reached_bar_height, default_reached_bar_height);
        mUnReachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_unreached_bar_height, default_unreached_bar_height);
        mOffset = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_offset, default_progress_text_offset);

        int textVisible = attributes.getInt(R.styleable.NumberProgressBar_progress_text_visibility, PROGRESS_TEXT_VISIBLE);
        if (textVisible != PROGRESS_TEXT_VISIBLE) {
            mIfDrawText = false;
        }

        setCurrentProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        setMaxProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_max, 100));

        attributes.recycle();
        initializePainters();
    }
    
    private void initializePainters(){

    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public int getReachedBarColor() {
        return mReachedBarColor;
    }

    public int getUnReachedBarColor() {
        return mUnReachedBarColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public float getReachedBarHeight() {
        return mReachedBarHeight;
    }

    public float getUnReachedBarHeight() {
        return mUnReachedBarHeight;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setCurrentProgress(int currentProgress) {
        if (currentProgress <= getMaxProgress() && currentProgress >= 0){
            this.mCurrentProgress = currentProgress;
            invalidate();
        }
    }

    public void setReachedBarColor(int reachedBarColor) {
        mReachedBarColor = reachedBarColor;
    }

    public void setUnReachedBarColor(int unReachedBarColor) {
        mUnReachedBarColor = unReachedBarColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public void setReachedBarHeight(float reachedBarHeight) {
        mReachedBarHeight = reachedBarHeight;
    }

    public void setUnReachedBarHeight(float unReachedBarHeight) {
        mUnReachedBarHeight = unReachedBarHeight;
    }

    public void setSuffix(String suffix) {
        mSuffix = suffix;
    }

    public void setPrefix(String prefix) {
        mPrefix = prefix;
    }

    public void setDrawTextWidth(float drawTextWidth) {
        mDrawTextWidth = drawTextWidth;
    }

    public void setDrawTextStart(float drawTextStart) {
        mDrawTextStart = drawTextStart;
    }

    public void setDrawTextEnd(float drawTextEnd) {
        mDrawTextEnd = drawTextEnd;
    }

    public void setCurrentDrawText(String currentDrawText) {
        mCurrentDrawText = currentDrawText;
    }

    public void setReachedBarPaint(Paint reachedBarPaint) {
        mReachedBarPaint = reachedBarPaint;
    }

    public void setUnreachedBarPaint(Paint unreachedBarPaint) {
        mUnreachedBarPaint = unreachedBarPaint;
    }

    public void setTextPaint(Paint textPaint) {
        mTextPaint = textPaint;
    }

    public void setUnreachedRectF(RectF unreachedRectF) {
        mUnreachedRectF = unreachedRectF;
    }

    public void setReachedRectF(RectF reachedRectF) {
        mReachedRectF = reachedRectF;
    }

    public void setOffset(float offset) {
        mOffset = offset;
    }

    public void setDrawUnreachedBar(boolean drawUnreachedBar) {
        mDrawUnreachedBar = drawUnreachedBar;
    }

    public void setDrawReachedBar(boolean drawReachedBar) {
        mDrawReachedBar = drawReachedBar;
    }

    public void setIfDrawText(boolean ifDrawText) {
        mIfDrawText = ifDrawText;
    }

    public void setListener(OnProgressBarListener listener) {
        mListener = listener;
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
