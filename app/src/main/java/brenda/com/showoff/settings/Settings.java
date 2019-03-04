package brenda.com.showoff.settings;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import brenda.com.showoff.R;
import brenda.com.showoff.Upload.UploadScreen;
import brenda.com.showoff.activities.LoginScreen;
import brenda.com.showoff.changepassword.ChangePassword;


public class Settings extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferences sharedpreferences;

    public Settings() {
        // Required empty public constructor
    }

    public static Settings newInstance() {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button logoutButton = view.findViewById(R.id.logout);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button changePassword = view.findViewById(R.id.changepassword);

        changePassword.setOnClickListener(this);

        logoutButton.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.changepassword:
                Fragment changepass = new ChangePassword();
                FragmentTransaction ft_signup = getActivity().getSupportFragmentManager().beginTransaction();
                ft_signup.replace(R.id.base_layout, changepass, "change");
                ft_signup.addToBackStack(null);
                ft_signup.commit();
                break;

            case R.id.logout:
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.putString("token", "");
                editor.apply();
                startActivity(new Intent(getContext(), LoginScreen.class));
                break;

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
