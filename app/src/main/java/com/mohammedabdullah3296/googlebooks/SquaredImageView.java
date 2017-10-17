package com.mohammedabdullah3296.googlebooks;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
//I can   specify custom transformations for more advanced effects.

/** An image view which always remains square with respect to its width. */
final public class SquaredImageView extends android.support.v7.widget.AppCompatImageView {
    public SquaredImageView(Context context) {
        super(context);
    }
    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}//end of the class