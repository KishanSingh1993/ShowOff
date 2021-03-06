package brenda.com.showoff.Home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import brenda.com.showoff.R;
import brenda.com.showoff.activity.ActivityAdapter;
import brenda.com.showoff.activity.ActivityModel;
import brenda.com.showoff.activity.ActivityScreen;
import brenda.com.showoff.apis.ApiUrl;

import static android.content.Context.MODE_PRIVATE;


public class ImageFullScreen extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<PostItem> postList = new ArrayList<PostItem>();
    private CommentAdapter adapter;

    private TextView upVote,downVote,post,cmnt,user;

    private ImageView txtUpvote,txtDownvote;

    private String utoken,postID,postDesc;

    private int upvote_downvote;

    private String cmntTxt;

    private EditText etCmnt;

    private OnFragmentInteractionListener mListener;

    public ImageFullScreen() {
        // Required empty public constructor
    }


    public static ImageFullScreen newInstance(String param1, String param2) {
        ImageFullScreen fragment = new ImageFullScreen();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_full_screen, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String url = getArguments().getString("imgurl");
        postID = getArguments().getString("postID");
        ImageView postImage = view.findViewById(R.id.postimage);
        upVote = view.findViewById(R.id.upvote1);
        downVote = view.findViewById(R.id.downvote1);
        txtUpvote = view.findViewById(R.id.upvote);
        txtDownvote = view.findViewById(R.id.downvote);
        post = view.findViewById(R.id.postdesc);
        cmnt = view.findViewById(R.id.cmnt);
        user = view.findViewById(R.id.sign_up_text);
        etCmnt = view.findViewById(R.id.addcmnt);
        Button postCmnt = view.findViewById(R.id.postcmnt);

        recyclerView = view.findViewById(R.id.activity_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setVisibility(View.GONE);

        txtUpvote.setOnClickListener(this);
        txtDownvote.setOnClickListener(this);
        postCmnt.setOnClickListener(this);

        //cmnt.setMovementMethod(LinkMovementMethod.getInstance());
        cmnt.setOnClickListener(this);


        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");

        Picasso.with(getContext())
                .load(url)
                .into(postImage);

        getPostDetails();

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
        switch (view.getId()){

            case R.id.upvote:
                upvote_downvote=1;
                callVoteApi();
                //getPostDetails();
                break;

            case R.id.downvote:
                upvote_downvote=0;
                callVoteApi();
                //getPostDetails();
                break;

            case R.id.postcmnt:
                cmntTxt = etCmnt.getText().toString();
                callCommentApi();
                //getPostDetails();
                break;

            case R.id.cmnt:
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                break;

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getPostDetails(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        postList = new ArrayList<PostItem>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.get_post_details + ".php",
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
                                cmnt.setText(Html.fromHtml(cmntNo+cmntlink));
                                //cmnt.setText(length+"Comments");
                                upVote.setText(String.format("%s Upvotes", upvotes));
                                downVote.setText(String.format("%s Downvotes", downvotes));
                                user.setText(uName);
                                if (postDesc.matches("null")){
                                    post.setText("Post description not available.");
                                }
                                else {
                                    post.setText(postDesc);
                                }

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

    private void callVoteApi(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.vote_post + ".php",
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){
                                getPostDetails();
                                etCmnt.getText().clear();
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
                params.put("upvote_downvote", String.valueOf(upvote_downvote));

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

        etCmnt.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.post_comment + ".php",
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){
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
}
