package brenda.com.showoff.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import brenda.com.showoff.MainActivity;
import brenda.com.showoff.R;
import brenda.com.showoff.SignUp.AddBio;
import brenda.com.showoff.apis.ApiUrl;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBioScreen extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bio_edit_text)
    EditText etBio;

    @BindView(R.id.link_edit_text)
    EditText etLink;

    @BindView(R.id.finish_button)
    Button btnFinish;

    private String fName,lName,userName,email,password,category,push_id;

    private static final String MyPREFERENCES = "userPref";

    private SharedPreferences userPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bio_screen);
        ButterKnife.bind(this);
        btnFinish.setOnClickListener(this);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        userPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        push_id = sharedpreferences.getString("pushid","");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        fName = bundle.getString("fname");
        lName = bundle.getString("lname");
        userName = bundle.getString("username");
        email = bundle.getString("email");
        password = bundle.getString("password");
        category = bundle.getString("category");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.finish_button:

                String txtBio = etBio.getText().toString();
                String txtLink = etLink.getText().toString();

                if (!txtLink.matches("") || !txtBio.matches("")) {

                    callSignupApi(fName,lName,userName,email,password,push_id,category,txtBio,txtLink);

                } else {

                    etBio.setError("Required");
                    etLink.setError("Required");
                }

                break;

        }
    }


    public void callSignupApi(final String fName, final String lName, final String userName, final String email, final String password, final String pId, final String category, final String bio, final String link) {

        final ProgressDialog progressDialog = new ProgressDialog(AddBioScreen.this);
        progressDialog.setMessage("Loging...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.signUp,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");


                            if (status.matches("1")){
                                String user_token = jsonObject.getString("user_token");
                                String user_id = jsonObject.getString("user_id");
                                String message = jsonObject.getString("message");
                                SharedPreferences.Editor editor = userPreference.edit();
                                editor.putString("user_token", user_token);
                                editor.putString("user_id", user_id);
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                                finish();

                            }
                            else if (status.matches("0")){
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                            else {

                                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", fName);
                params.put("last_name", lName);
                params.put("username", userName);
                params.put("email", email);
                params.put("password", password);
                params.put("push_id", pId);
                params.put("category", category);
                params.put("bio", bio);
                params.put("link", link);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
