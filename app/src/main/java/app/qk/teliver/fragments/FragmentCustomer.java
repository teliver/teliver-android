package app.qk.teliver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.TrackingBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.qk.teliver.R;
import app.qk.teliver.utils.Utils;


public class FragmentCustomer extends Fragment {

    private Activity context;

    private View viewRoot;

    private Dialog dialogBuilder;

    private int[] icons = new int[]{R.drawable.nav_blue, R.drawable.nav_green, R.drawable.nav_purple};

    private Random random;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        viewRoot = view.findViewById(R.id.view_root);
        random = new Random();
        view.findViewById(R.id.trip_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });
    }


    private void startTracking() {
        try {
            dialogBuilder = new Dialog(context);
            dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(
                    ContextCompat.getColor(context, android.R.color.transparent)));
            dialogBuilder.setContentView(R.layout.view_cus_tracking);
            final EditText edtId = (EditText) dialogBuilder.findViewById(R.id.edt_id);
            final TextView btnOk = (TextView) dialogBuilder.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startTracking(edtId.getText().toString().trim());
                }
            });
            dialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTracking(String trackingId) {
        try {
            if (trackingId.isEmpty())
                Utils.showSnack(viewRoot, getString(R.string.text_enter_valid_id));
            else if (!Utils.isNetConnected(context))
                Utils.showSnack(viewRoot, getString(R.string.text_no_internet));
            else {
                dialogBuilder.dismiss();
                List<MarkerOption> markerOptionList = new ArrayList<>();
                String[] ids = trackingId.split(",");
                for (String id : ids) {
                    MarkerOption option = new MarkerOption(id);
                    option.setMarkerTitle(id);
                    option.setIconMarker(icons[random.nextInt(icons.length)]);
                    markerOptionList.add(option);
                }
                TrackingBuilder builder = new TrackingBuilder(markerOptionList);
                Teliver.startTracking(builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
