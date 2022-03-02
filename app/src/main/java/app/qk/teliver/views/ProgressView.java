package app.qk.teliver.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import app.qk.teliver.R;

public class ProgressView extends Dialog {

    private Context context;

    public ProgressView(Context context) {
        super(context);
        this.context = context;
    }

    public void showProgress() {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_view, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        this.setContentView(view);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.
                        getColor(context, R.color.colorPrimary)
                , PorterDuff.Mode.MULTIPLY);
        this.show();
    }
}
