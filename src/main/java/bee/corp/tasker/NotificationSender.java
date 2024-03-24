package bee.corp.tasker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationSender {
    String channelID;
    NotificationCompat.Builder builder;
    Intent intent;
    PendingIntent pIntent;
    NotificationManager nManager;
    NotificationChannel nChannel;
    public NotificationSender(Context c){
        channelID = "CHANNEL_ID_NOTIFICATION";
        builder = new NotificationCompat.Builder(c, channelID);
        builder.setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_background);
        intent = new Intent(c, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_MUTABLE);
        nManager = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nChannel = nManager.getNotificationChannel(channelID);
            nChannel = new NotificationChannel(channelID, "notification",NotificationManager.IMPORTANCE_HIGH);
            nChannel.enableVibration(true);
            nManager.createNotificationChannel(nChannel);
        } else {
            System.exit(0);
        }
    }
    public void sendNotification(String title, String text){
        builder.setContentTitle(title)
                .setContentText(text);
        nManager.notify(0, builder.build());
    }
}
