package brenda.com.showoff.Util;

import android.os.Handler;
import android.widget.TextView;


/**
 * Created by Mukul on 5/6/18.
 */

public class CustomRunnable implements Runnable {

    public long millisUntilFinished;
    public TextView holder;
    Handler handler;

    public CustomRunnable(Handler handler, TextView holder, long millisUntilFinished) {
        this.handler = handler;
        this.holder = holder;
        this.millisUntilFinished = millisUntilFinished;
    }

    @Override
    public void run() {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = millisUntilFinished / hoursInMilli;
        millisUntilFinished = millisUntilFinished % hoursInMilli;

        long elapsedMinutes = millisUntilFinished / minutesInMilli;
        millisUntilFinished = millisUntilFinished % minutesInMilli;

        long elapsedSeconds = millisUntilFinished / secondsInMilli;

        String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
        holder.setText(yy);

        handler.postDelayed(this, 1000);
    }

}