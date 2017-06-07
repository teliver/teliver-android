package app.qk.teliver.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;


/**
 * The Class CustomToast.
 */
@SuppressLint("InflateParams")
public class CustomToast {


    /**
     * Show toast.
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
