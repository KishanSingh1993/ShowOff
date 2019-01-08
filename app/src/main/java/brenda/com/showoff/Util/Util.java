package brenda.com.showoff.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import brenda.com.showoff.R;

/**
 * Created by Mukul on 7/1/17.
 */

public class Util {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_progress_bar);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static ProgressDialog ProgressDialogAl(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_progress_bar);
        dialog.setMessage("Entering Contest");
        return dialog;
    }


//    public static void buttonEffect(View button){
//        button.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        v.getBackground().setColorFilter(0xe0d3d3d3, PorterDuff.Mode.SRC_ATOP);
//                        v.invalidate();
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP: {
//                        v.getBackground().clearColorFilter();
//                        v.invalidate();
//                        break;
//                    }
//                }
//                return false;
//            }
//        });
//    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static long getHoursRemaining(String start_date_string)
    {
        SimpleDateFormat iso_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current_date = new Date();
        System.out.println("current date wdout format is " + current_date);
        Date start_date = null;
        try {
            start_date = iso_format.parse(start_date_string);
        }catch (Exception e)
        {
            System.out.println("Exception in date first" + e.getMessage());
        }

        long duration  = start_date.getTime() - current_date.getTime();
        System.out.println("Difference is" + duration);

        return duration;
    }

    public static String changeToIST(String start_date_string)
    {
        SimpleDateFormat iso_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start_date = null;
        try {
        start_date = iso_format.parse(start_date_string);
        }catch (Exception e)
        {
            System.out.println("Exception in date first" + e.getMessage());
        }
        TimeZone tz = TimeZone.getTimeZone("IST");
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        formatter.setTimeZone(tz);
        return formatter.format(start_date);
    }

    public static long getTimeInMilis(String start_date_string)
    {
        SimpleDateFormat iso_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current_date = new Date();
        System.out.println("current date wdout format is " + current_date);
        long timeInMilliseconds = 0;
        Date start_date = null;
        try {
            start_date = iso_format.parse(start_date_string);
            timeInMilliseconds = start_date.getTime();
            System.out.println("time in milis is" + timeInMilliseconds);

        }catch (Exception e)
        {
            System.out.println("Exception in date first" + e.getMessage());
        }

        //long duration  = start_date.getTime() - current_date.getTime();
        //System.out.println("Difference is" + duration);

        return timeInMilliseconds;
    }


    public static String getHistoryDate(String start_date_string)
    {
        String dest_date = "";
        SimpleDateFormat iso_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dest_format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date current_date = iso_format.parse(start_date_string);
            dest_date = dest_format.format(current_date);

        }catch (Exception e)
        {
            System.out.println("Exception in date first" + e.getMessage());
        }

        return dest_date;
    }

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


}
