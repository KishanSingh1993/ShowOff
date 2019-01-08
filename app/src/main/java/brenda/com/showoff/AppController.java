package brenda.com.showoff;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import brenda.com.showoff.Util.LruBitmapCache;

/**
 * Created by Mukul on 6/29/17.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Activity mCurrentActivity = null;
    int socketTimeout = 10000;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueuePost(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    //When the app is in the foreground.
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    //When the app resumes.
    public static void activityResumed() {
        activityVisible = true;

    }

    //When the app goes into the background.
    public static void activityPaused() {
        activityVisible = false;
    }

    //Get current running activity.
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }


    //Boolen which is set and returned when the app comes and goes from background to foreground and vice versa.
    private static boolean activityVisible;
}