package app.qk.teliver.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teliver.sdk.models.Trip;

import java.util.List;

import app.qk.teliver.R;


public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.MyViewHolder> {

    private List<Trip> tripList;

    private View.OnClickListener listener;

    private LayoutInflater inflater;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trackingId, tripId, stop;

        MyViewHolder(View view) {
            super(view);
            trackingId = (TextView) view.findViewById(R.id.trackingId);
            stop = (TextView) view.findViewById(R.id.stop);
            tripId = (TextView) view.findViewById(R.id.tripId);
        }
    }

    public TripsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Trip> trips, View.OnClickListener listener) {
        this.tripList = trips;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.trip_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.trackingId.setText(trip.getTrackingId());
        holder.stop.setTag(trip.getTrackingId());
        holder.stop.setOnClickListener(listener);
        holder.tripId.setText(trip.getTripId());
    }


    @Override
    public int getItemCount() {
        return tripList.size();
    }


}
