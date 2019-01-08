package brenda.com.showoff.SignUp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import brenda.com.showoff.R;
import brenda.com.showoff.SignUp.SignUp;

public class Login extends Fragment {

    private RelativeLayout back_button_overlay;
    private TextView login_text;
    private ImageView logo_image_view;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private Button sign_in_button;
    private Button sign_up_button;
    private LinearLayout progress_bar_ll;
    private Typeface typeface;

    private String bio;
    private String link;


    public Login() {
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
        View view = inflater.inflate(R.layout.login, container, false);

        //Initialize the variables.
        login_text = view.findViewById(R.id.sign_up_button);
        logo_image_view = view.findViewById(R.id.logo_image_view);
        email_edit_text = view.findViewById(R.id.email_edit_text);
        password_edit_text = view.findViewById(R.id.password_edit_text);
        sign_in_button = view.findViewById(R.id.sign_in_button);
        sign_up_button = view.findViewById(R.id.sign_up_button);
        progress_bar_ll = view.findViewById(R.id.progress_bar_ll);

        //Initialize the font.
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");

        //Set font to views.
        login_text.setTypeface(typeface);
        email_edit_text.setTypeface(typeface);
        password_edit_text.setTypeface(typeface);
        sign_up_button.setTypeface(typeface);
        sign_in_button.setTypeface(typeface);

        //set on click listener on finish button.
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Fragment sign_up = new SignUp();
                        FragmentTransaction ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                        ft.replace(R.id.base_layout, sign_up, "sign_up"); //Replace the main activity base layout with the fragment.
                        ft.commit(); //Commit the transaction.

                    }
                }, 500);


            }
        });
        return view;
    }
}
