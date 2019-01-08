package brenda.com.showoff.SignUp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import brenda.com.showoff.AppController;
import brenda.com.showoff.R;
import brenda.com.showoff.Util.CustomRequest;
import brenda.com.showoff.Util.RoundedImageView;
import brenda.com.showoff.Util.Util;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SignUp extends Fragment {

    //Declare the variables.
    private EditText first_name_edit_text;
    private EditText last_name_edit_text;
    private RelativeLayout total_rl;
    private EditText email_edit_text;
    private LinearLayout progress_bar_ll;
    private EditText password_edit_text;
    private EditText username_edit_text;
    private Button next_button;
    private RelativeLayout back_button_overlay;
    private TextView sign_up_text;

    private ImageView user_image_view;
    private String first_name;
    private String last_name;
    private String user_name;
    private String email;
    private String password;

    private Typeface typeface;
    private JSONObject check_details_object;
    private String success;
    private String message;
    private static String url_check_email_username = "http://www.talenttracker.in/showoff/check_email_username.php";
    private SharedPreferences sharedPreferences;
    String PREFS_SHOWOFF = "showoff";
    private ProgressDialog progress_dialog;
    private String push_id;
    private Handler ha;
    private Runnable myRunnable;

    // Initialize an array with all the edit texts ids which cannot be left empty.
    int[] ids = new int[]{
            R.id.first_name_edit_text,
//            R.id.last_name_edit_text,
            R.id.email_edit_text,
            R.id.password_edit_text,
            R.id.username_edit_text,
    };

    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sign_up, container, false);

        ha = new Handler();

        // Initialize shared preferences.
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(PREFS_SHOWOFF, 0);

        //Initialize the variables.
        back_button_overlay = view.findViewById(R.id.back_button_overlay);
        sign_up_text = view.findViewById(R.id.sign_up_text);
        total_rl = view.findViewById(R.id.total_rl);
        user_image_view = view.findViewById(R.id.user_image_view);
        progress_bar_ll = view.findViewById(R.id.progress_bar_ll);
        first_name_edit_text = view.findViewById(R.id.first_name_edit_text);
        last_name_edit_text = view.findViewById(R.id.last_name_edit_text);
        username_edit_text = view.findViewById(R.id.username_edit_text);
        email_edit_text = view.findViewById(R.id.email_edit_text);
        password_edit_text = view.findViewById(R.id.password_edit_text);
        next_button = view.findViewById(R.id.next_button);
        progress_bar_ll = view.findViewById(R.id.progress_bar_ll);

        //Initialize the font.
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");

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


        total_rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null) {
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });

        //Set font for the elements on screen.
        sign_up_text.setTypeface(typeface);
        first_name_edit_text.setTypeface(typeface);
        last_name_edit_text.setTypeface(typeface);
        email_edit_text.setTypeface(typeface);
        password_edit_text.setTypeface(typeface);
        username_edit_text.setTypeface(typeface);
        next_button.setTypeface(typeface);

        //Check for firebase token.
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                push_id = instanceIdResult.getToken();
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
            }
        });
        System.out.println("my firebase token in sign up is" + push_id);

        //If firebase is not yet initialized then throw an exception.
        //Set the value of push id to empty string.
        try {
            if(push_id.equals(null)) throw new NullPointerException();
        }
        catch (NullPointerException ex) {
            System.out.println("is empty");
            push_id = "";
        }



        back_button_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Do something here
                        try {
                            getFragmentManager().popBackStack();
                        }catch(Exception j)
                        {
                            System.out.println("Exception in sign up back button.");
                        }
                    }
                }, 500);


            }
        });



        //Set up on click listener on next button.
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Fragment select_category = new SelectCategory();
                        FragmentTransaction ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                        ft.replace(R.id.base_layout, select_category, "select_category"); //Replace the main activity base layout with the fragment.
                        ft.commit(); //Commit the transaction.

                    }
                }, 500);




                    //Call function to check if all the fields have been filled and are correct.
                    Boolean check = check_edit_text_data(ids);

                    //If check is true means some fields were not filled.
                    if (check == true) {
                        //Show toast.
                        Toast.makeText(getActivity(), "Please fill out all the fields.",
                                Toast.LENGTH_SHORT).show();
                    }

                    //If check is false that means all fields were filled.
                    else {

                        // Check if the email is valid, if not then show error in toast.
                        if (!(email_edit_text.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))) {
                            // Show toast.
                            Toast.makeText(getActivity(), "Please enter a valid email address.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        //If the email is correct then check that password is at least
                        //six characters.
                        else {
                            if (password_edit_text.getText().toString().length() < 6) {
                                // Show toast.
                                Toast.makeText(getActivity(), "The password should be minimum 6 characters.",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                //If the firebase token/Push id is still null or empty then start a thread
                                //and check after 5 seconds.
                                try {
                                    if (push_id.equals(null) || push_id.equals("")) {
                                        progress_dialog = Util.createProgressDialog(getActivity());
                                        progress_dialog.setMessage("Initializing firebase");
                                        progress_dialog.show();

                                        ha.postDelayed(myRunnable = new Runnable() {
                                            @Override
                                            public void run() {

                                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                                        push_id = instanceIdResult.getToken();
                                                        // Do whatever you want with your token now
                                                        // i.e. store it on SharedPreferences or DB
                                                        // or directly send it to server
                                                    }
                                                });

                                                System.out.println("my firebase token in sign up  after thread is" + push_id);
                                                progress_dialog.dismiss();
                                                //sign_up_user();

                                            }
                                        }, 5000);
                                    } else {
                                        check_email_username();
                                    }
                                } catch (Exception w) {
                                    System.out.println("Exception in w of firebase" + w.getMessage());

                                }
                            }

                        }
                    }

            }
        });


        return view;
    }


    //Function to check the details by sending request to the API.
    public void check_email_username()
    {

        //Get all the values from the edit text and store them in variables.
        first_name = first_name_edit_text.getText().toString();
        last_name = last_name_edit_text.getText().toString();
        email = email_edit_text.getText().toString();
        user_name = username_edit_text.getText().toString();
        password = password_edit_text.getText().toString();
        next_button.setVisibility(View.GONE);
        progress_bar_ll.setVisibility(View.VISIBLE);

        // Post params to send with the request.
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("user_name", user_name);

        // Defining a custom request.
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, url_check_email_username, params, this.createRequestSuccessListener(), this.createRequestErrorListener());

        jsonReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonReq, "sign_up_request");

    }
    // Function to handle if error occurs during request.
    private Response.ErrorListener createRequestErrorListener() {
        // TODO Auto-generated method stub
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                next_button.setVisibility(View.VISIBLE);
                progress_bar_ll.setVisibility(View.GONE);

                // Display toast with error.
                Toast.makeText(getActivity(), error.toString(),
                        Toast.LENGTH_SHORT).show();

                Log.v("error", error.toString());

            }
        };
    }

    //Function to handle the response if there is no error during request.
    private Response.Listener<JSONObject> createRequestSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseJSON) {

                progress_bar_ll.setVisibility(View.GONE);

                Log.v("response", responseJSON.toString());
                if (responseJSON != null) {

                    // Store the result in object and call function to parse the result.
                    check_details_object = responseJSON;
                    parse_result_data();
                }
            }
        };
    }

    // Function to parse the result data returned from the server.
    public void parse_result_data() {

        try {

            // Get success string from json object.
            success = check_details_object.getString("success");

            // If success is equal to 1 that means card was successfully created.
            if (success.equals("1")) {

                Fragment select_category = new SelectCategory();
                FragmentTransaction ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                ft.replace(R.id.base_layout, select_category, "select_category"); //Replace the main activity base layout with the fragment.
                ft.commit(); //Commit the transaction.

            } else {

                message = check_details_object.getString("message");
                // Display toast with message if success is 0.
                Toast.makeText(getActivity(), message,
                        Toast.LENGTH_SHORT).show();

                next_button.setVisibility(View.VISIBLE);

            }

        } catch (Exception e) {
            // Display toast with exception.
            Toast.makeText(getActivity(), e.getMessage().toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    // Function to check valid form data.
    // Pass all the edit text ids as parameters.
    public boolean check_edit_text_data(int[] ids) {

        boolean isEmpty = false;

        // Loop through all the ids and initialize edit texts one by one.
        for(int id: ids)
        {
            EditText et = getView().findViewById(id);

            // If the edit text is empty then set error and set is empty flag to true.
            if(TextUtils.isEmpty(et.getText().toString()))
            {
                et.setError("Please enter a valid value.");
                isEmpty = true;
            }
        }

        // Return is empty flag.
        return isEmpty;

    }


    @Override
    public void onResume()
    {
        super.onResume();
        //MainActivity currentActivity = (MainActivity) ((AppController) getActivity().getApplicationContext()).getCurrentActivity();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ha.removeCallbacks(myRunnable);
    }
}
