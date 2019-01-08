package brenda.com.showoff.Firebase;

/**
 * Created by Mukul on 8/13/16.
 */


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import brenda.com.showoff.AppController;
import brenda.com.showoff.MainActivity;

public class NotificationReceiver extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String user_id;
    MainActivity currentActivity;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        System.out.println("message is" + remoteMessage.getNotification().getBody());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().toString());
        currentActivity = (MainActivity) ((AppController) getApplicationContext()).getCurrentActivity();
        if(currentActivity != null)
        {
            //currentActivity.get_count();
        }


        //message = remoteMessage.getNotification().getBody();

//        if(remoteMessage.getData().containsKey("provider_id"))
//        {
//            provider_id = remoteMessage.getData().get("provider_id");
//            currentActivity = (MainActivity) ((App) getApplicationContext()).getCurrentActivity();
//            System.out.println("cueee" + currentActivity);
//            if (currentActivity != null) {
//
//                currentActivity.connectCall(provider_id);
//
//            }
//
//            else
//            {
//
//            sendNotification(message, "Request Assigned.", "open_user");
//
//            }
//        }
//        else if(remoteMessage.getData().containsKey("test_id"))
//        {
//            sendNotification(message, "VideoFullScreen Assigned.", "test_open");
//
//        }
//        else if(remoteMessage.getData().containsKey("start_date"))
//        {
//
//            start_date = remoteMessage.getData().get("start_date");
//            start_time = remoteMessage.getData().get("start_time");
//            end_date = remoteMessage.getData().get("end_date");
//            end_time = remoteMessage.getData().get("end_time");
//            total_price = remoteMessage.getData().get("total_price");
//
//            currentActivity = (MainActivity) ((App) getApplicationContext()).getCurrentActivity();
//            System.out.println("cueessse" + currentActivity);
//            if (currentActivity != null) {
//
//                currentActivity.updateFragment(start_date, start_time, end_date, end_time, total_price);
//
//            }
//            else {
//                sendNotification(message, "Date and Time Details.", "time_open");
//            }
//
//        }
//        else if(remoteMessage.getData().containsKey("total_price"))
//        {
//            hours = remoteMessage.getData().get("hours");
//            minutes = remoteMessage.getData().get("minutes");
//            final_total_price = remoteMessage.getData().get("total_price");
//            sendNotification(message, "Congrats!! Your job has been finished.", "finished_job");
//
//        }
//        else if(remoteMessage.getData().containsKey("assigned_jobs_id"))
//        {
//                assigned_jobs_id = remoteMessage.getData().get("assigned_jobs_id");
//            user_id_assigned = remoteMessage.getData().get("user_id");
//            sendNotification(message, "Congrats!! Your have been assigned a job.", "assigned_job");
//        }
//        else {
//            request_id = remoteMessage.getData().get("service_request_id");
//            user_id = remoteMessage.getData().get("user_id");
//            sendNotification(message, "New Service Request. Tap to apply", "open_provider");
//
//        }


        //Calling method to generate notification
       // sendNotification(remoteMessage.getNotification().getBody());
    }

    //This method is only generating push notification.
//    private void sendNotification(String title, String messageBody, String what) {
//        Intent intent = new Intent(this, MainActivity.class);
//
//        if(what.equals("open_provider")) {
//            intent.putExtra("what", "open_provider");
//            intent.putExtra("service_request_id", request_id);
//            intent.putExtra("user_id", user_id);
//        }
//        else if(what.equals("assigned_job"))
//        {
//            intent.putExtra("what", "open_my_requests");
//            intent.putExtra("assigned_jobs_id", assigned_jobs_id);
//            intent.putExtra("assigned_user_id", user_id_assigned);
//
//        }
//        else if(what.equals("finished_job"))
//        {
//            intent.putExtra("what", "open_assigned_jobs");
//            intent.putExtra("hours", hours);
//            intent.putExtra("minutes", minutes);
//            intent.putExtra("final_total_price", final_total_price);
//
//        }
//        else
//        {
//            intent.putExtra("what", "open_user");
//            intent.putExtra("provider_id", provider_id);
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }

}