package brenda.com.showoff.Home.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import brenda.com.showoff.Home.HomeRecyclerAdapter;
import brenda.com.showoff.Home.PostItem;
import brenda.com.showoff.R;
import brenda.com.showoff.apis.ApiUrl;

import static android.content.Context.MODE_PRIVATE;


public class Others extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private RadioButton optYes, optNo;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private String utoken;
    private String radioText;

    ArrayList<PostItem> postList = new ArrayList<PostItem>();
    private HomeRecyclerAdapter adapter;

    public Others() {
        // Required empty public constructor
    }

    public static MyActivity newInstance(String param1, String param2) {
        MyActivity fragment = new MyActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        optYes = view.findViewById(R.id.optYes);
        optNo = view.findViewById(R.id.optNo);
        recyclerView = view.findViewById(R.id.activity_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");
        Log.d("token",utoken);
        optYes.setChecked(true);

        boolean isYes = optYes.isChecked();
        boolean isNo = optNo.isChecked();

        if (isYes){
            radioText="upvote";
            getHomePageData(radioText);
        }
        else if (isNo){

            radioText="recent";
            getHomePageData(radioText);
        }

        optYes.setOnClickListener(this);
        optNo.setOnClickListener(this);

        return view;
    }

    private void getHomePageData(final String filter){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.get_home_post,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Log.d("homeOtherResponse",response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){

                                String postData = jsonObject.getString("homepage");

                                JSONArray jsonArray = new JSONArray(postData);

                                for (int i =0; i<jsonArray.length();i++){

                                    JSONObject dataObject = jsonArray.getJSONObject(i);

                                    PostItem postItem = new PostItem();

                                    postItem.setPost_url(dataObject.getString("post_url"));
                                    postItem.setType(dataObject.getString("post_type"));
                                    postItem.setPost_username(dataObject.getString("username"));
                                    postItem.setPost_comments(dataObject.getString("post_comments"));
                                    postItem.setPost_upvotes(dataObject.getString("total_upvotes"));
                                    postItem.setDesc(dataObject.getString("post_description"));
                                    postItem.setTimestamp(dataObject.getString("created"));

                                    postList.add(postItem);
                                }

                                adapter=new HomeRecyclerAdapter(getActivity(),postList);
                                recyclerView.setAdapter(adapter);
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
                params.put("token", utoken);
                params.put("category", "singer");
                params.put("other", "Yes");
                params.put("filter", filter);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

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

        switch (view.getId()) {
            case R.id.optYes:
                optNo.setChecked(false);
                radioText="upvote";
                postList.clear();
                getHomePageData(radioText);
                break;

            case R.id.optNo:
                optYes.setChecked(false);
                radioText="recent";
                postList.clear();
                getHomePageData(radioText);
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
