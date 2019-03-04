package brenda.com.showoff.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import brenda.com.showoff.Constants;
import brenda.com.showoff.MainActivity;
import brenda.com.showoff.R;
import brenda.com.showoff.Util.Validator;
import brenda.com.showoff.apis.ApiUrl;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.email_edit_text)
    EditText etEmail;

    @BindView(R.id.password_edit_text)
    EditText etPassword;

    @BindView(R.id.sign_in_button)
    Button signIn;

    @BindView(R.id.sign_up_button)
    Button signUp;

    @BindView(R.id.rl)
    RelativeLayout mainLayout;

    private String userEmail,userPassword;

    private static final String LoginPref = "login" ;
    private static final String logintoken = "token";
    private static final String email = "email";

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        ButterKnife.bind(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        signUp.setOnClickListener(this);

        signIn.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(LoginPref, Context.MODE_PRIVATE);

        if (sharedpreferences!=null){

            String token = sharedpreferences.getString("token","");

            if (!token.matches("")){

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.sign_in_button:

                userEmail = etEmail.getText().toString();
                userPassword = etPassword.getText().toString();
                if (!Validator.isValidEmail(userEmail) || !Validator.isValidPassword(userPassword)){

                    etEmail.setError("Please enter a valid email");
                    etPassword.setError("Invalid Password");

                }else {

                    callLoginApi(userEmail,userPassword);
                }
                break;

            case R.id.sign_up_button:

                startActivity(new Intent(getApplicationContext(),SignupPersonalDetails.class));
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                break;

        }
    }

    private void callLoginApi(final String email, final String password){

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Logging in..please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl+ApiUrl.login,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");


                            if (status.matches("1")){
                                String token = jsonObject.getString("token");
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(logintoken, token);
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
