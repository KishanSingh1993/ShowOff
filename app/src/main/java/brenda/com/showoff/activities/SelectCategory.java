package brenda.com.showoff.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import brenda.com.showoff.R;
import brenda.com.showoff.SignUp.AddBio;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCategory extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.singer_text)
    TextView singer;

    @BindView(R.id.instruments_text)
    TextView instrument;

    @BindView(R.id.dancer_text)
    TextView dancer;

    @BindView(R.id.acting_text)
    TextView acting;

    @BindView(R.id.singer_cat)
    RelativeLayout singerLayout;

    @BindView(R.id.instruments_cat)
    RelativeLayout instrumentLayout;

    @BindView(R.id.dancer_cat)
    RelativeLayout dancerLayout;

    @BindView(R.id.acting_cat)
    RelativeLayout actingLayout;

    private String fName,lName,userName,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        fName = bundle.getString("fname");
        lName = bundle.getString("lname");
        userName = bundle.getString("username");
        email = bundle.getString("email");
        password = bundle.getString("password");

        singerLayout.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.singer_cat:
                singer.setTextColor(R.color.colorPrimary);
                String category = singer.getText().toString();
                Intent intent = new Intent(getApplicationContext(),AddBioScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("fname", fName);
                bundle.putString("lname", lName);
                bundle.putString("username", userName);
                bundle.putString("email", email);
                bundle.putString("password", password);
                bundle.putString("category", category);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.instruments_cat:
                instrument.setTextColor(R.color.colorPrimary);
                String icategory = instrument.getText().toString();
                Intent iintent = new Intent(getApplicationContext(),AddBioScreen.class);
                Bundle ibundle = new Bundle();
                ibundle.putString("fname", fName);
                ibundle.putString("lname", lName);
                ibundle.putString("username", userName);
                ibundle.putString("email", email);
                ibundle.putString("password", password);
                ibundle.putString("category", icategory);
                iintent.putExtras(ibundle);
                startActivity(iintent);
                break;

            case R.id.dancer_cat:
                dancer.setTextColor(R.color.colorPrimary);
                String dcategory = dancer.getText().toString();
                Intent dintent = new Intent(getApplicationContext(),AddBioScreen.class);
                Bundle dbundle = new Bundle();
                dbundle.putString("fname", fName);
                dbundle.putString("lname", lName);
                dbundle.putString("username", userName);
                dbundle.putString("email", email);
                dbundle.putString("password", password);
                dbundle.putString("category", dcategory);
                dintent.putExtras(dbundle);
                startActivity(dintent);
                break;

            case R.id.acting_cat:
                dancer.setTextColor(R.color.colorPrimary);
                String acategory = dancer.getText().toString();
                Intent aintent = new Intent(getApplicationContext(),AddBioScreen.class);
                Bundle abundle = new Bundle();
                abundle.putString("fname", fName);
                abundle.putString("lname", lName);
                abundle.putString("username", userName);
                abundle.putString("email", email);
                abundle.putString("password", password);
                abundle.putString("category", acategory);
                aintent.putExtras(abundle);
                startActivity(aintent);
                break;

        }
    }
}
