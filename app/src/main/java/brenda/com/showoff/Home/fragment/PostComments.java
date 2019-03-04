package brenda.com.showoff.Home.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import brenda.com.showoff.Home.CommentAdapter;
import brenda.com.showoff.Home.PostItem;
import brenda.com.showoff.R;
import brenda.com.showoff.apis.ApiUrl;

import static android.content.Context.MODE_PRIVATE;


public class PostComments extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<PostItem> postList = new ArrayList<PostItem>();
    private CommentAdapter adapter;
    private EditText etCmnt;
    private String utoken,postID,postDesc;
    private String cmntTxt;

    public PostComments() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostComments newInstance(String param1, String param2) {
        PostComments fragment = new PostComments();
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
        View view = inflater.inflate(R.layout.fragment_post_comments, container, false);
        Button postCmnt = view.findViewById(R.id.postcmnt);
        etCmnt = view.findViewById(R.id.addcmnt);
        postID = getArguments().getString("postID");
        recyclerView = view.findViewById(R.id.activity_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        postCmnt.setOnClickListener(this);
        //recyclerView.setVisibility(View.GONE);
        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");
        getPostDetails();
        return view;
    }


    private void getPostDetails(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.get_post_details,
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){

                                String cmntArray = jsonObject.getString("comments");
                                JSONArray array=new JSONArray(cmntArray);
                                int length = array.length();
                                String upvotes = jsonObject.getString("upvotes");
                                String downvotes = jsonObject.getString("downvotes");
                                postDesc = jsonObject.getString("post_description");
                                String uName = jsonObject.getString("username");

                                String cmntlink = "<a href='http://www.google.com'> Comments </a>";
                                String cmntNo = "<a href='http://www.google.com'>"+ String.valueOf(length) +"</a>";


                                for (int i=0; i<array.length(); i++){
                                    JSONObject ob=array.getJSONObject(i);
                                    PostItem listData = new PostItem();
                                    listData.post_comments = ob.getString("comment_text");
                                    listData.post_username = ob.getString("username");
                                    postList.add(listData);
                                }
                                adapter = new CommentAdapter(getContext(),postList);
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
                params.put("post_id", postID);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void callCommentApi(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.post_comment,
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){

                                etCmnt.getText().clear();
                                getPostDetails();
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
                params.put("post_id", postID);
                params.put("comment_text", cmntTxt);

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

        switch (view.getId()){

            case R.id.postcmnt:
                cmntTxt = etCmnt.getText().toString();
                callCommentApi();
                break;
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
