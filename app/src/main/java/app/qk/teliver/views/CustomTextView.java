package app.qk.teliver.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import app.qk.teliver.utils.Utils;


public class CustomTextView extends AppCompatTextView {

    private Utils mUtils;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
    }

}
