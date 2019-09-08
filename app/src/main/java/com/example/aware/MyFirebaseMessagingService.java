package com.example.aware;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Authenticator;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebase";
    private static final String SUBSCRIBE_TO = "userABC";




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message Data Payload " + remoteMessage.getData());
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMessage = data.getString("event_action");
                Log.d(TAG, "This is the JSON message: " + jsonMessage);

                sendNotification(jsonMessage);
//
//                if (jsonMessage.equals("REQUEST_MERGE_LEFT")) {
//                    request_merge_left();
//                } else if (jsonMessage.equals("REQUEST_MERGE_RIGHT")) {
//                    //request_merge_right();
//                } else if (jsonMessage.equals("REQUEST_PASS")) {
//                    //request_pass();
//                } else if (jsonMessage.equals("RESPOND_MERGE_LEFT")) {
//                    //respond_merge_left();
//                } else if (jsonMessage.equals("RESPOND_MERGE_RIGHT")) {
//                    //respond_merge_right();
//                } else {
//                    //respond_pass();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void request_merge_left() {
        Intent intent;
        intent = new Intent(this, RequestMergeLeft.class);
        startActivity(intent);



//        if(event_action.equals("REQUEST_MERGE_LEFT")) {
//        } else {
//            intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }

    }





    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        // OKHTTP를 이용해 웹서버로 토큰값을 날려준다.
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url("토큰 저장할라고 보낼 URL")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Push Test") // 이부분은 어플 켜놓은 상태에서 알림 메세지 받으면 저 텍스트로 띄워준다.
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}