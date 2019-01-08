package brenda.com.showoff.Firebase;

/**
 * Created by Mukul on 8/13/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class FirebaseId extends FirebaseInstanceIdService {

    private String PREFS_CRICDUEL;
    ProgressDialog progressDialog;
    String push_id;
    String user_token;
    Boolean push_token_updated;

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {

        SharedPreferences sharedPreferences = getSharedPreferences("pushshared", Context.MODE_PRIVATE);

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        push_id = refreshedToken;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("push_id", refreshedToken);
        editor.apply();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);


    }


    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server


    }
}