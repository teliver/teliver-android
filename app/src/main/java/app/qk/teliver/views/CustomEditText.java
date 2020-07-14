package app.qk.teliver.views;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import app.qk.teliver.utils.Utils;


public class CustomEditText extends AppCompatEditText {

    private Utils mUtils;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
    }

}
