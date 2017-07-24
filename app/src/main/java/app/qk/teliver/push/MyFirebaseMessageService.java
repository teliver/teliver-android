package app.qk.teliver.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.NotificationData;
import com.teliver.sdk.models.TConstants;
import com.teliver.sdk.models.TrackingBuilder;

import java.util.Map;

import app.qk.teliver.R;


public class MyFirebaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            if (Teliver.isTeliverPush(remoteMessage)) {
                Map<String, String> pushData = remoteMessage.getData();
                NotificationData data = new GsonBuilder().create().fromJson(pushData.get("description"), NotificationData.class);
                if (data.getCommand().equals(TConstants.CMD_TRIP_START)) {

                    TrackingBuilder builder = new TrackingBuilder(new MarkerOption(data.getTrackingID()));
                    Intent notificationIntent = new Intent(this, Teliver.getMap());
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    notificationIntent.putExtra(TConstants.DOTS_OBJ, builder.build());
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationManager notificationManager = (NotificationManager)
                            this.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this);
                    notBuilder.setSmallIcon(R.drawable.ic_notification);
                    notBuilder.setContentTitle(this.getString(R.string.app_name));
                    notBuilder.setContentText(data.getMessage());
                    notBuilder.setAutoCancel(true);
                    notBuilder.setOnlyAlertOnce(true);
                    notBuilder.addAction(R.drawable.ic_toolbar, this.getString(R.string.txt_start_tracking), pendingIntent);
                    Notification notification = notBuilder.build();
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(12, notification);
                } else if (data.getCommand().equals(TConstants.CMD_EVENT_PUSH)) {
                    //The Event Push Area
                }
            } else {
                //The push is not for us, You can handle it.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
