package app.qk.teliver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.core.TripListener;
import com.teliver.sdk.models.PushData;
import com.teliver.sdk.models.Trip;
import com.teliver.sdk.models.TripBuilder;

import java.util.ArrayList;
import java.util.List;

import app.qk.teliver.R;
import app.qk.teliver.adapters.TripsAdapter;
import app.qk.teliver.utils.Constants;
import app.qk.teliver.utils.MPreference;
import app.qk.teliver.utils.Utils;
import app.qk.teliver.views.CustomToast;

import static android.content.Context.LOCATION_SERVICE;


public class FragmentDriver extends Fragment implements TripListener, View.OnClickListener {

    private Activity context;

    private LocationManager manager;

    private View viewRoot;

    private Dialog dialogBuilder;

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
        TextView txtTripStatus = (TextView) view.findViewById(R.id.trip_status);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        currentTrips = new ArrayList<>();
        currentTrips.addAll(Teliver.getCurrentTrips());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new TripsAdapter(context);
        mAdapter.setData(currentTrips, this);
        recyclerView.setAdapter(mAdapter);

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS &&
                apiAvailability.isUserResolvableError(resultCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(context, resultCode,
                    900);
            dialog.setCancelable(false);
            dialog.show();
        }
        txtTripStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.checkPermission(context))
                    validateTrip();
            }
        });
    }

    public void validateTrip() {
        try {
            String provider = manager.getBestProvider(new Criteria(), true);
            if ((!TextUtils.isEmpty(provider)) &&
                    LocationManager.PASSIVE_PROVIDER.equals(provider))
                Utils.showLocationAlert(context);
            else {
                dialogBuilder = new Dialog(context);
                dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(
                        ContextCompat.getColor(context, android.R.color.transparent)));
                dialogBuilder.setContentView(R.layout.view_tracking);
                final EditText edtId = (EditText) dialogBuilder.findViewById(R.id.edt_id);
                final EditText edtTitle = (EditText) dialogBuilder.findViewById(R.id.edt_title);
                final EditText edtMsg = (EditText) dialogBuilder.findViewById(R.id.edt_msg);
                final EditText edtUserId = (EditText) dialogBuilder.findViewById(R.id.edt_user_id);
                final TextView btnOk = (TextView) dialogBuilder.findViewById(R.id.btn_ok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTrip(edtTitle.getText().toString(), edtMsg.getText().toString(),
                                edtUserId.getText().toString().trim(), edtId.getText().toString().trim());
                    }
                });
                dialogBuilder.show();
            }

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
    }

    @Override
    public void onTripError(String reason) {
        Log.d("Driver:", "Trip error: Reason: " + reason);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode != Constants.PERMISSION_REQ_CODE)
            return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            validateTrip();
        else
            CustomToast.showToast(context, getString(R.string.text_location_permission));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                try {

                    Teliver.stopTrip(v.getTag().toString());
                    Utils.showSnack(viewRoot,getString(R.string.txt_wait_stop_trip));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }


}
