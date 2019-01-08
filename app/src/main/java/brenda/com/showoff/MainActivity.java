package brenda.com.showoff;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;

import brenda.com.showoff.Home.Home;
import brenda.com.showoff.SignUp.Login;
import brenda.com.showoff.Upload.RecordVideo;
import brenda.com.showoff.Upload.Upload;
import brenda.com.showoff.Upload.UploadScreen;
import brenda.com.showoff.Util.Util;
import brenda.com.showoff.activity.ActivityScreen;
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

    private static String imageStoragePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the variables.
        mMyApp = (AppController) this.getApplicationContext();
        base_layout = findViewById(R.id.base_layout);

        SharedPreferences sharedpreferences = getSharedPreferences("pushshared",MODE_PRIVATE);

        push_id = sharedpreferences.getString("pushid","");

        Toast.makeText(getApplicationContext(),push_id,Toast.LENGTH_LONG).show();

        //Check for active internet connection
        Boolean connected = Util.isOnline(MainActivity.this);
        if (!connected) {

            //Show toast.
            Toast.makeText(MainActivity.this, "No active internet connection found. Please make sure you are connected to the internet and relaunch the app.",
                    Toast.LENGTH_LONG).show();

        } else {

            // Initialize shared preferences.
            sharedPreferences = this.getApplicationContext().getSharedPreferences(PREFS_SHOWOFF, 0);

            //Initialize the bottom bar.
            int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.tabColors);
            AHBottomNavigation ahBottomNavigation = findViewById(R.id.navigation);
            ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
            ahBottomNavigation.setDefaultBackgroundColor(Color.parseColor("#3b97d3"));
            AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.my_navigation_items);
            navigationAdapter.setupWithBottomNavigation(ahBottomNavigation, tabColors);
            ahBottomNavigation.setInactiveColor(Color.parseColor("#ffffff"));
            ahBottomNavigation.setAccentColor(Color.parseColor("#fe5c97"));






            my_lis = new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {

                    String tag = "";
                    switch (position) {
                        case 0:

                            fragment = new Home();
                            ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                            //ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left); //Set the animation.
                            ft.replace(R.id.base_layout, fragment, "home"); //Replace the main activity base layout with the fragment.
                            ft.addToBackStack(null);
                            ft.commit(); //Commit the transaction.
                            return true;

                        case 1:

//                            fragment = new LiveContestFragment();
//                            tag = "live_contest";
//                            ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
//                            ft.replace(R.id.base_layout, fragment, tag); //Replace the main activity base layout with the fragment.
//                            ft.addToBackStack(null);
//                            ft.commit(); //Commit the transaction.
//                            return true;
                        //break;

                        case 2:

                            fragment = new UploadScreen();
                            ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                            ft.replace(R.id.base_layout, fragment, "upload"); //Replace the main activity base layout with the fragment.
                            ft.addToBackStack(null);
                            ft.commit(); //Commit the transaction.
                            return true;

                        // break;

                        case 3:

                            fragment = new ActivityScreen();
                            tag = "upcoming_contest";
                            ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                            ft.replace(R.id.base_layout, fragment, tag); //Replace the main activity base layout with the fragment.
                            ft.addToBackStack(null);
                            ft.commit(); //Commit the transaction.
                            return true;

                        case 4:

                            fragment = new Settings();
                            tag = "upcoming_contest";
                            ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                            ft.replace(R.id.base_layout, fragment, tag); //Replace the main activity base layout with the fragment.
                            ft.addToBackStack(null);
                            ft.commit(); //Commit the transaction.

//                            fragment = new HistoryFragment();
//                            tag = "history_fragment";
//                            ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
//                            ft.replace(R.id.base_layout, fragment, tag); //Replace the main activity base layout with the fragment.
//                            ft.addToBackStack(null);
//                            ft.commit(); //Commit the transaction.
//                            return true;
                        //break;
                    }

                    return true;
                }
            };

            ahBottomNavigation.setOnTabSelectedListener(my_lis);



            // Make sure shared preferences contains token.
            try {
                if (sharedPreferences.contains("user_token")) {

                    // Retrieve the string.
                    user_token = sharedPreferences.getString("user_token", "0");

                    //System.out.println("user token" + user_token);

//                    Fragment error_layout = new ErrorLayout();
//                    FragmentTransaction ft_signup = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
//                    ft_signup.replace(R.id.base_layout, error_layout, "error_layout"); //Replace the main activity base layout with the fragment.
//                    ft_signup.addToBackStack(null);
//                    ft_signup.commit(); //Commit the transaction.


                } else {


                    Fragment upload = new UploadScreen();
                    FragmentTransaction ft_signup = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                    ft_signup.replace(R.id.base_layout, upload, "upload"); //Replace the main activity base layout with the fragment.
                    ft_signup.addToBackStack(null);
                    ft_signup.commit(); //Commit the transaction.
                }

//                    Fragment login = new Login();
//                    FragmentTransaction ft_signup = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
//                    ft_signup.replace(R.id.base_layout, login, "login"); //Replace the main activity base layout with the fragment.
//                    ft_signup.addToBackStack(null);
//                    ft_signup.commit(); //Commit the transaction.
//                }

            } catch (Exception e) {

            }

        }
    }
}
