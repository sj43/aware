package com.example.aware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edtTitle;
    EditText edtMessage;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAKCfwx4o:APA91bG1IE3B1s9oPQSZlvaCRURzUCihWXyr66i0d-3Vn55y_eFEXPBO4hYijy7hM1MHYkTOpWQFtsPP0jrLSm8Zmg4S7MKWkb9yhaRCRAFtOa2oMCL-aWayhsbTv39YHbYNK6b2tYHX";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ////

        edtTitle = findViewById(R.id.speechTitle);
        edtMessage = findViewById(R.id.speechBody);
        Button btnSend = findViewById(R.id.sendMessage);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = edtTitle.getText().toString();
                NOTIFICATION_MESSAGE = edtMessage.getText().toString();

                JSONObject notification = new JSONObject();
                JSONObject notificationBody = new JSONObject();
                try {
                    notificationBody.put("title", NOTIFICATION_TITLE);
                    notificationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", TOPIC);
                    notification.put("data", notificationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                sendNotification(notification);
            }
        });


        ////
//
//
//        Bundle customData = getIntent().getExtras();
//
//        if (customData != null) {
//
//            TextView textView = (TextView) findViewById(R.id.textView3);
//            textView.setText(customData.getString("key1"));
//        }
//
//
//
//        final Button button = (Button) findViewById(R.id.sendMessage);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                openActivity2();
//
//                // Code here executes on main thread after user presses button
//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//
//                final String SENDER_ID = "172468782986";
//                final int messageId = 0;
//
//                fm.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
//                        .setMessageId(Integer.toString(messageId))
//                        .addData("my_message", "Hello World")
//                        .addData("my_action","SAY_HELLO")
//                        .build());
//            }
//        });
//    }
//
//    public void openActivity2() {
//        Intent intent = new Intent(this, RequestMergeLeft.class);
//        startActivity(intent);
//    }


    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        edtTitle.setText("");
                        edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
