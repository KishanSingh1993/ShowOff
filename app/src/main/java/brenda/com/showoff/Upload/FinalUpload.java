package brenda.com.showoff.Upload;

import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import brenda.com.showoff.R;

public class FinalUpload extends Fragment {

    private RelativeLayout back_button_overlay;
    private TextView confirm_text;
    private Typeface typeface;
    private VideoView my_video_view;
    private Bundle extra;
    private String final_video_path;

    public FinalUpload() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.final_upload_video, container, false);

        extra = this.getArguments();
        final_video_path = extra.getString("path", "0");

        //Initialize the variables.
        back_button_overlay = view.findViewById(R.id.back_button_overlay);
        confirm_text = view.findViewById(R.id.confirm_text);
        my_video_view = view.findViewById(R.id.my_video_view);

        //Initialize the font.
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");

        confirm_text.setTypeface(typeface);

        back_button_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Do something here
                        try {
                            getFragmentManager().popBackStack();
                        } catch (Exception h) {
                            System.out.println("exception in back button");
                        }
                    }
                }, 500);

            }
        });

        String videoName1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath()+"/MyApplication/"+final_video_path+".mp4";
        System.out.println("video path in final is" + videoName1);
        Uri videoUri = Uri.parse(videoName1);
        my_video_view.setVideoURI(videoUri);


        return view;


    }
    }
