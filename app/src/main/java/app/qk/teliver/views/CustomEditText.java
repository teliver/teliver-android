package app.qk.teliver.views;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import app.qk.teliver.utils.Utils;


/**
 * The Class CustomEditTextView.
 */
public class CustomEditText extends TextInputEditText {

    /** The m utils. */
    private Utils mUtils;

    /**
     * Instantiates a new custom edit text view.
     * 
     * @param context
     *            the context
     */
    public CustomEditText(Context context) {
        super(context);
    }

    /**
     * Instantiates a new custom edit text view.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
    }

    /**
     * Instantiates a new custom edit text view.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
    }

}
