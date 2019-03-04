package brenda.com.showoff.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import brenda.com.showoff.Home.GridListAdapter;
import brenda.com.showoff.Home.PostItem;
import brenda.com.showoff.R;
import brenda.com.showoff.apis.ApiUrl;

import static android.content.Context.MODE_PRIVATE;


public class ActivityScreen extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String utoken;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private OnFragmentInteractionListener mListener;
    ArrayList<ActivityModel> postList = new ArrayList<ActivityModel>();
    private ActivityAdapter  adapter;
    public ActivityScreen() {
        // Required empty public constructor
    }

    public static ActivityScreen newInstance() {
        ActivityScreen fragment = new ActivityScreen();
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
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        recyclerView = view.findViewById(R.id.activity_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        callActivityApi();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void callActivityApi(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.activity,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){

                                String posts = jsonObject.getString("activity");


                                JSONArray array=new JSONArray(posts);
                                for (int i=0; i<array.length(); i++){
                                    JSONObject ob=array.getJSONObject(i);
                                    ActivityModel listData = new ActivityModel();

                                    listData.fname = ob.getString("first_name");
                                    listData.upvote = ob.getString("upvote_downvote");
                                    listData.imgurl = ob.getString("post_url");
                                    listData.userId = ob.getString("user_id");
                                    listData.postID = ob.getString("post_id");

                                    /*if (listData.userId.matches("6")){

                                        postList.remove(i);
                                    }
                                    else {

                                    }*/

                                    postList.add(listData);
                                }
                                adapter=new ActivityAdapter(getActivity(),postList);
                                recyclerView.setAdapter(adapter);

                                recyclerView.post(new Runnable()
                                {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });

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
                params.put("page_number", "0");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
