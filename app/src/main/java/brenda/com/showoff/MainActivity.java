package brenda.com.showoff;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import brenda.com.showoff.Home.Home;
import brenda.com.showoff.Home.HomeScreen;
import brenda.com.showoff.SignUp.Login;
import brenda.com.showoff.Upload.RecordVideo;
import brenda.com.showoff.Upload.Upload;
import brenda.com.showoff.Upload.UploadScreen;
import brenda.com.showoff.Util.Util;
import brenda.com.showoff.activity.ActivityAdapter;
import brenda.com.showoff.activity.ActivityModel;
import brenda.com.showoff.activity.ActivityScreen;
import brenda.com.showoff.apis.ApiUrl;
import brenda.com.showoff.settings.Settings;

public class MainActivity extends AppCompatActivity {

    //Declare the variables.
    private RelativeLayout base_layout;
    protected AppController mMyApp;
    SharedPreferences sharedPreferences;
    String PREFS_SHOWOFF = "showoff";
    private String user_token;
    FragmentTransaction ft;
    Fragment fragment;
    AHBottomNavigation.OnTabSelectedListener my_lis;
    private String utoken;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    private String push_id;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    AHBottomNavigation ahBottomNavigation;

    boolean notificationVisible = false;

    private static String imageStoragePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialize the variables.
        mMyApp = (AppController) this.getApplicationContext();
        base_layout = findViewById(R.id.base_layout);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences tokenpref = getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");
        push_id = sharedpreferences.getString("pushid","");


        //Check for active internet connection
        Boolean connected = Util.isOnline(MainActivity.this);
        if (!connected) {

            //Show toast.
            Toast.makeText(MainActivity.this, "No active internet connection found. Please make sure you are connected to the internet and relaunch the app.",
                    Toast.LENGTH_LONG).show();

        } else {
            ahBottomNavigation = findViewById(R.id.navigation);
            // Initialize shared preferences.
            sharedPreferences = this.getApplicationContext().getSharedPreferences(PREFS_SHOWOFF, 0);
            AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.home, R.drawable.home, R.color.colorPink);
            AHBottomNavigationItem search = new AHBottomNavigationItem(R.string.search, R.drawable.magnifying_glass, R.color.colorPink);
            AHBottomNavigationItem upload = new AHBottomNavigationItem(R.string.upload, R.drawable.ccamera, R.color.colorPink);
            AHBottomNavigationItem activity = new AHBottomNavigationItem(R.string.activity_text, R.drawable.activity, R.color.colorPink);
            AHBottomNavigationItem settings = new AHBottomNavigationItem(R.string.settings, R.drawable.settings, R.color.colorPink);
            ahBottomNavigation.addItem(home);
            ahBottomNavigation.addItem(search);
            ahBottomNavigation.addItem(upload);
            ahBottomNavigation.addItem(activity);
            ahBottomNavigation.addItem(settings);
            ahBottomNavigation.setTranslucentNavigationEnabled(true);

            ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
            ahBottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorBlueText));
            ahBottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorPink));
            ahBottomNavigation.setInactiveColor(ContextCompat.getColor(this, R.color.colorWhite));
            ahBottomNavigation.setCurrentItem(0);
            loadFragment(HomeScreen.newInstance());

            ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    switch (position) {
                        case 0:
                            loadFragment(HomeScreen.newInstance());
                            break;
                        case 1:
                            //loadFragment(MyContestFragment.newInstance());
                            break;
                        case 2:
                            loadFragment(UploadScreen.newInstance());
                            Log.d("user_token","clicked");
                            break;
                        case 3:
                            loadFragment(ActivityScreen.newInstance());
                            break;
                        default:
                            loadFragment(Settings.newInstance());
                            break;

                    }
                    return true;
                }
            });


            /*try {
                if (sharedPreferences.contains("user_token")) {

                    // Retrieve the string.
                    user_token = sharedPreferences.getString("user_token", "0");



                } else {

                    loadFragment(HomeScreen.newInstance());

                }


            } catch (Exception e) {

            }*/

        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.base_layout, fragment);
        transaction.commitNow();
    }

    private void createFakeNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("1")
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.
                ahBottomNavigation.setNotification(notification, ahBottomNavigation.getItemsCount() - 2);
                notificationVisible = true;
            }
        }, 1000);
    }

    @Override

    public void onResume() {
        super.onResume();

        getActivityCount();
        //createFakeNotification();
    }

    private void getActivityCount(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.activityCount,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){

                                final String totalCount = jsonObject.getString("total_count");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AHNotification notification = new AHNotification.Builder()
                                                .setText(totalCount)
                                                .setBackgroundColor(Color.YELLOW)
                                                .setTextColor(Color.BLACK)
                                                .build();
                                        // Adding notification to last item.
                                        ahBottomNavigation.setNotification(notification, ahBottomNavigation.getItemsCount() - 2);
                                        notificationVisible = true;
                                    }
                                }, 1000);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", utoken);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
