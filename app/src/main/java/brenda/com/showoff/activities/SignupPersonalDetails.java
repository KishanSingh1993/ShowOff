package brenda.com.showoff.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import brenda.com.showoff.R;
import brenda.com.showoff.Util.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupPersonalDetails extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.first_name_edit_text) EditText etfirstName;

    @BindView(R.id.last_name_edit_text) EditText etlastName;

    @BindView(R.id.username_edit_text) EditText etuserName;

    @BindView(R.id.email_edit_text) EditText etEmail;

    @BindView(R.id.password_edit_text) EditText etPassword;

    @BindView(R.id.next_button) Button nextButton;

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.total_rl)
    RelativeLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_personal_details);

        ButterKnife.bind(this);

        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.next_button:
                String fName = etfirstName.getText().toString();
                String lName = etlastName.getText().toString();
                String userName = etuserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!Validator.isValidName(fName) || !Validator.isValidName(lName) || !Validator.isValidName(userName) || !Validator.isValidEmail(email) || !Validator.isValidPassword(password)){
                    etfirstName.setError("Invalid Name");
                    etlastName.setError("Invalid Last Name");
                    etuserName.setError("Invalid Username");
                    etEmail.setError("Invalid Email");
                    etPassword.setError("Invalid Password");
                }
                else {

                    Intent intent = new Intent(getApplicationContext(),SelectCategory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fname", fName);
                    bundle.putString("lname", lName);
                    bundle.putString("username", userName);
                    bundle.putString("email", email);
                    bundle.putString("password", password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;

            case R.id.back_button:
                startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                break;
        }
    }
}
