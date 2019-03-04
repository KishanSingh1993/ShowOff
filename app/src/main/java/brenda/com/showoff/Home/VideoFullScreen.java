package brenda.com.showoff.Home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import brenda.com.showoff.R;
import brenda.com.showoff.apis.ApiUrl;

import static android.content.Context.MODE_PRIVATE;

public class VideoFullScreen extends Fragment implements View.OnClickListener {

    SimpleExoPlayer player;
    View view;
    String url;

    private TextView upVote,downVote,post,cmnt,user;

    private String utoken,postID,postDesc;

    private int upvote_downvote;

    private String cmntTxt;

    private EditText etCmnt;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<PostItem> postList = new ArrayList<PostItem>();
    private CommentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_test, container, false);
        url = getArguments().getString("imgurl");
        initializePlayer();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        postID = getArguments().getString("postID");
        ImageView postImage = view.findViewById(R.id.postimage);
        upVote = view.findViewById(R.id.upvote1);
        downVote = view.findViewById(R.id.downvote1);
        ImageView txtUpvote = view.findViewById(R.id.upvote);
        ImageView txtDownvote = view.findViewById(R.id.downvote);
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
        cmnt.setOnClickListener(this);

        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");


        getPostDetails();
        return view;
    }

    private void initializePlayer(){
        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = view.findViewById(R.id.thumbnail1);
        simpleExoPlayerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "CloudinaryExoplayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(url);
        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        player.prepare(videoSource);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.upvote:
                upvote_downvote=1;
                callVoteApi();
                break;

            case R.id.downvote:
                upvote_downvote=0;
                callVoteApi();
                break;

            case R.id.postcmnt:
                cmntTxt = etCmnt.getText().toString();
                callCommentApi();
                break;

            case R.id.cmnt:
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                break;

        }
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
                                cmnt.setText(Html.fromHtml(cmntNo+cmntlink));
                                upVote.setText(String.format("%s Upvotes", upvotes));
                                downVote.setText(String.format("%s Downvotes", downvotes));
                                user.setText(uName);
                                if (postDesc.matches("null")){
                                    post.setText("Post description not available");
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
