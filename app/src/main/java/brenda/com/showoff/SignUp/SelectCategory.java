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
import android.widget.RelativeLayout;
import android.widget.TextView;

import brenda.com.showoff.R;

public class SelectCategory extends Fragment {

    private RelativeLayout back_button_overlay;
    private TextView sign_up_text;
    private TextView show_talent;
    private RelativeLayout singer_cat;
    private RelativeLayout instruments_cat;
    private RelativeLayout dancer_cat;
    private RelativeLayout acting_cat;
    private RelativeLayout poems_cat;

    private TextView singer_text;
    private TextView instruments_text;
    private TextView dancer_text;
    private TextView acting_text;
    private TextView poems_text;
    private Typeface typeface;
    private String selected_cat;


    public SelectCategory() {
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
        View view = inflater.inflate(R.layout.choose_category, container, false);

        //Initialize the variables.
        back_button_overlay = view.findViewById(R.id.back_button_overlay);
        sign_up_text = view.findViewById(R.id.sign_up_text);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");

        singer_cat = view.findViewById(R.id.singer_cat);
        instruments_cat = view.findViewById(R.id.instruments_cat);
        dancer_cat = view.findViewById(R.id.dancer_cat);
        acting_cat = view.findViewById(R.id.acting_cat);
        poems_cat = view.findViewById(R.id.poems_cat);

        singer_text = view.findViewById(R.id.singer_text);
        instruments_text = view.findViewById(R.id.instruments_text);
        dancer_text = view.findViewById(R.id.dancer_text);
        acting_text = view.findViewById(R.id.acting_text);
        poems_text = view.findViewById(R.id.poems_text);

        sign_up_text.setTypeface(typeface);
        singer_text.setTypeface(typeface);
        instruments_text.setTypeface(typeface);
        dancer_text.setTypeface(typeface);
        acting_text.setTypeface(typeface);
        poems_text.setTypeface(typeface);

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

        singer_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected_cat = "Singer";

                change_fragment();

            }
        });

        instruments_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected_cat = "Instruments";
                change_fragment();

            }
        });

        acting_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected_cat = "Acting";

                change_fragment();

            }
        });

        dancer_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected_cat = "Dancer";
                change_fragment();

            }
        });

        poems_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selected_cat = "Poems";
                change_fragment();

            }
        });

        return view;

    }

    private void change_fragment()
    {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                //Do something here
                    Fragment add_bio = new AddBio();
                    FragmentTransaction ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                    ft.replace(R.id.base_layout, add_bio, "add_bio"); //Replace the main activity base layout with the fragment.
                    ft.commit(); //Commit the transaction.

            }
        }, 500);


    }


}

