package brenda.com.showoff.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    private String fName,lName,userName,email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_personal_details);

        ButterKnife.bind(this);

        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.next_button:
                fName = etfirstName.getText().toString();
                lName = etlastName.getText().toString();
                userName = etuserName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

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
