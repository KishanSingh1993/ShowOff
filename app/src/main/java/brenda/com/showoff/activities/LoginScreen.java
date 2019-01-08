package brenda.com.showoff.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private String userEmail,userPassword;

    private static final String LoginPref = "login" ;
    private static final String logintoken = "token";

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        ButterKnife.bind(this);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(LoginPref, Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.sign_in_button:
                userEmail = etEmail.getText().toString();
                userPassword = etPassword.getText().toString();
                if (!Validator.isValidEmail(userEmail) || !Validator.isValidPassword(userPassword)){

                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                }else {

                    callLoginApi(userEmail,userPassword);
                }
                break;

            case R.id.sign_up_button:

                startActivity(new Intent(getApplicationContext(),SignupPersonalDetails.class));

                break;

        }
    }

    private void callLoginApi(final String email, final String password){

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Loging...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl+ApiUrl.login+".php",
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
