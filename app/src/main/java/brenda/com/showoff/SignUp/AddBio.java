package brenda.com.showoff.SignUp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import brenda.com.showoff.R;

public class AddBio extends Fragment {

    private RelativeLayout back_button_overlay;
    private TextView sign_up_text;
    private TextView add_bio_text;
    private EditText bio_edit_text;

    private TextView add_link_text;
    private EditText link_edit_text;
    private Button finish_button;
    private LinearLayout progress_bar_ll;

    private String bio;
    private String link;


    public AddBio() {
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
        View view = inflater.inflate(R.layout.add_bio, container, false);

        //Initialize the variables.
        back_button_overlay = view.findViewById(R.id.back_button_overlay);
        sign_up_text = view.findViewById(R.id.sign_up_text);

        add_bio_text = view.findViewById(R.id.add_bio_text);
        add_link_text = view.findViewById(R.id.add_link_text);
        bio_edit_text = view.findViewById(R.id.bio_edit_text);
        link_edit_text = view.findViewById(R.id.link_edit_text);
        finish_button = view.findViewById(R.id.finish_button);
        progress_bar_ll = view.findViewById(R.id.progress_bar_ll);

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

        //set on click listener on finish button.
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bio = bio_edit_text.getText().toString();
                if(bio.equals("") || bio.equals(null))
                {
                    // Show toast.
                    Toast.makeText(getActivity(), "Please tell us a bit about yourself.",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    link = link_edit_text.getText().toString();

                }
            }
        });
        return view;
    }


}
