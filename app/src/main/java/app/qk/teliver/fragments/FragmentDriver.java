package app.qk.teliver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import io.teliver.sdk.core.Teliver;
import io.teliver.sdk.core.TripListener;
import io.teliver.sdk.models.PushData;
import io.teliver.sdk.models.Trip;
import io.teliver.sdk.models.TripBuilder;

import java.util.ArrayList;
import java.util.List;

import app.qk.teliver.R;
import app.qk.teliver.adapters.TripsAdapter;
import app.qk.teliver.utils.Constants;
import app.qk.teliver.utils.MPreference;
import app.qk.teliver.utils.Utils;
import app.qk.teliver.views.CustomToast;

import static android.content.Context.LOCATION_SERVICE;


public class FragmentDriver extends Fragment implements TripListener, View.OnClickListener, OnSuccessListener<Location> {

    private Activity context;

    private LocationManager manager;

    private View viewRoot, viewEmpty;

    private Dialog dialogBuilder;

    private static final int REQUEST_CODE_PERMISSIONS = 101;

    private MPreference mPreference;

    private TripsAdapter mAdapter;

    private List<Trip> currentTrips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        Teliver.setTripListener(this);
        mPreference = new MPreference(context);
        viewRoot = view.findViewById(R.id.view_root);
        manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        ExtendedFloatingActionButton txtTripStatus = view.findViewById(R.id.add_trip);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        viewEmpty = view.findViewById(R.id.view_empty);
        currentTrips = new ArrayList<>();
        currentTrips.addAll(Teliver.getCurrentTrips());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new TripsAdapter(context);
        mAdapter.setData(currentTrips, this);
        recyclerView.setAdapter(mAdapter);

        if (currentTrips.isEmpty())
            viewEmpty.setVisibility(View.VISIBLE);

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS &&
                apiAvailability.isUserResolvableError(resultCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(context, resultCode,
                    900);
            dialog.setCancelable(false);
            dialog.show();
        }

        txtTripStatus.setOnClickListener(view1 -> {
            if (Utils.checkLPermission(context))
                Utils.enableGPS(context, this);
        });
    }

    public void validateTrip() {
        try {
                dialogBuilder = new Dialog(context);
                dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(
                        ContextCompat.getColor(context, android.R.color.transparent)));
                dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogBuilder.setContentView(R.layout.alert_add_trip);
                final EditText edtId = dialogBuilder.findViewById(R.id.edt_id);
                final EditText edtTitle = dialogBuilder.findViewById(R.id.edt_title);
                final EditText edtMsg = dialogBuilder.findViewById(R.id.edt_msg);
                final EditText edtUserId = dialogBuilder.findViewById(R.id.edt_user_id);
                final TextView btnOk = dialogBuilder.findViewById(R.id.btn_ok);
                final ImageView imageArrow = dialogBuilder.findViewById(R.id.image_arrow);
                final View expandView = dialogBuilder.findViewById(R.id.expandable_view);
                final View viewNotifyUser = dialogBuilder.findViewById(R.id.view_notify_users);

                viewNotifyUser.setOnClickListener(v -> {
                    if (expandView.getVisibility() == View.GONE) {
                        expandView.setVisibility(View.VISIBLE);
                        imageArrow.setRotation(180);
                    } else {
                        expandView.setVisibility(View.GONE);
                        imageArrow.setRotation(360);
                    }
                });
                btnOk.setOnClickListener(view -> startTrip(edtTitle.getText().toString(), edtMsg.getText().toString(),
                        edtUserId.getText().toString().trim(), edtId.getText().toString().trim()));
                dialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTrip(String title, String msg, String userId, String trackingId) {
        try {
            if (trackingId.isEmpty())
                Utils.showSnack(viewRoot, getString(R.string.text_enter_valid_id));
            else if (!Utils.isNetConnected(context))
                Utils.showSnack(viewRoot, getString(R.string.text_no_internet));
            else {
                dialogBuilder.dismiss();
                TripBuilder builder = new TripBuilder(trackingId);
                if (!userId.isEmpty()) {
                    PushData pushData = new PushData(userId.split(","));
                    pushData.setPayload(msg);
                    pushData.setMessage(title);
                    builder.withUserPushObject(pushData);
                }
                Teliver.startTrip(builder.build());
                Utils.showSnack(viewRoot, getString(R.string.txt_wait_start_trip));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTripStarted(Trip tripDetails) {
        Log.d("Driver:", "Trip started::" + tripDetails);
        changeStatus(tripDetails.getTrackingId(), true);
    }

    @Override
    public void onLocationUpdate(Location location) {

    }

    @Override
    public void onTripEnded(String trackingId) {
        Log.d("Driver:", "Trip Ended::" + trackingId);
        changeStatus(null, false);
    }


    private void changeStatus(String id, boolean status) {
        mPreference.storeBoolean(Constants.IS_TRIP_ACTIVE, status);
        mPreference.storeString(Constants.TRACKING_ID, id);
        currentTrips.clear();
        currentTrips.addAll(Teliver.getCurrentTrips());
        mAdapter.notifyDataSetChanged();
        viewEmpty.setVisibility(currentTrips.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTripError(String reason) {
        Log.d("Driver:", "Trip error: Reason: " + reason);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                try {
                    Teliver.stopTrip(v.getTag().toString());
                    Utils.showSnack(viewRoot, getString(R.string.txt_wait_stop_trip));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void onReqPermission(int[] grantResults) {
        if (Utils.isPermissionOk(grantResults))
            Utils.enableGPS(context,this);
        else
            CustomToast.showToast(context, getString(R.string.text_location_permission));
    }

    @Override
    public void onSuccess(Location location) {
        if (location != null)
            validateTrip();
    }
}
