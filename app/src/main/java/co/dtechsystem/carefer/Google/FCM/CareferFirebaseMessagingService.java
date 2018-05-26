package co.dtechsystem.carefer.Google.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.MainActivity;


public class CareferFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = CareferFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Notification Body Custom: " + remoteMessage.getData());
                sendNotification(remoteMessage.getData().get("Title"), remoteMessage.getData().get("Message"), remoteMessage.getData().get("Link"));

            } else {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), "");

            }

        }


        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//
////            try {
////                JSONObject json = new JSONObject(remoteMessage.getData().toString());
////            } catch (Exception e) {
////                Log.e(TAG, "Exception: " + e.getMessage());
////            }
//        }
    }

    private void sendNotification(String Title, String messageBody, String Link) {
        Intent intent;
        if (Link != null && !Link.equals("")) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        } else {
            intent = new Intent(this, MainActivity.class);

        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int not_nu = generateRandom();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, not_nu, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(not_nu, notificationBuilder.build());


    }
    private int generateRandom() {
        return new Random().nextInt(8999) + 1000;
    }

}