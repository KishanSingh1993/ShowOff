package brenda.com.showoff.Home;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

import brenda.com.showoff.R;
import brenda.com.showoff.Util.RoundedImageView;
import brenda.com.showoff.apis.ApiUrl;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {

    private TextView total_posts_number;
    private TextView total_posts_text;
    private TextView username_text;
    private TextView total_votes_number;
    private TextView total_votes_text;
    //private ImageView user_image_view;
    private RoundedImageView user_rounded_image_view;
    private TextView role_text;
    private TextView name_text;
    private TextView bio_text;
    private Button see_website_button;
    private RecyclerView posts_layout;
    private Typeface typeface;

    private JSONObject posts_object;
    private JSONObject basic_details_object;
    private String success;
    private String message;
    private static String url_get_posts = "http://www.talenttracker.in/showoff/url_get_posts.php";
    private static String url_get_basic_details = "http://www.talenttracker.in/showoff/url_get_basic_details.php";
    private SharedPreferences sharedPreferences;
    String PREFS_SHOWOFF = "showoff";
    private ProgressDialog progress_dialog;
    private String utoken;
    private ArrayList<PostItem> list_data;
    private GridView gridView;
    GridListAdapter adapter;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        //Initialize the font.
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");

        // Initialize shared preferences.
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(PREFS_SHOWOFF, 0);

        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");

        //Initialize the views.
        total_posts_number = view.findViewById(R.id.total_posts_number);
        total_posts_text = view.findViewById(R.id.total_posts_text);
        username_text = view.findViewById(R.id.username_text);
        name_text = view.findViewById(R.id.name_text);
        total_votes_number = view.findViewById(R.id.total_votes_number);
        total_votes_text = view.findViewById(R.id.total_votes_text);
        //user_image_view = view.findViewById(R.id.user_image_view);
        user_rounded_image_view = view.findViewById(R.id.user_rounded_image_view);
        role_text = view.findViewById(R.id.role_text);
        bio_text = view.findViewById(R.id.bio_text);
        see_website_button = view.findViewById(R.id.see_website_button);
        //posts_layout = view.findViewById(R.id.posts_layout);
        gridView=view.findViewById(R.id.gridView);
        list_data=new ArrayList<>();


        //Initialize the font.
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");
        total_posts_number.setTypeface(typeface);
        total_posts_text.setTypeface(typeface);
        username_text.setTypeface(typeface);
        total_votes_number.setTypeface(typeface);
        total_votes_text.setTypeface(typeface);
        role_text.setTypeface(typeface);
        bio_text.setTypeface(typeface);
        see_website_button.setTypeface(typeface);

        getHomeData();

        return view;
    }


    private void getHomeData(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.getHome + ".php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");
                            if (status.matches("1")){

                                String posts = jsonObject.getString("posts");
                                String fName = jsonObject.getString("first_name");
                                String lName = jsonObject.getString("last_name");
                                String imgurl = jsonObject.getString("image_url");
                                String uName = jsonObject.getString("username");
                                String upVotes = jsonObject.getString("upvotes");
                                String tPosts = jsonObject.getString("total_posts");
                                String bio = jsonObject.getString("bio");
                                String category = jsonObject.getString("category");
                                String link = jsonObject.getString("link");

                                total_posts_number.setText(tPosts);
                                total_votes_number.setText(upVotes);
                                username_text.setText(uName);
                                name_text.setText(String.format("%s %s", fName, lName));
                                bio_text.setText(bio);
                                role_text.setText(category);


                                JSONArray array=new JSONArray(posts);
                                for (int i=0; i<array.length(); i++){
                                    JSONObject ob=array.getJSONObject(i);
                                    PostItem listData = new PostItem();
                                    listData.post_url = ob.getString("post_url");
                                    /*if (listData.post_url.endsWith(".png")){
                                        list_data.add(listData);
                                    }
                                    else if (listData.post_url.endsWith(".mp4")){
                                        list_data.add(listData);

                                    }*/

                                    list_data.add(listData);

                                }
                                adapter=new GridListAdapter(getContext(),R.layout.home_image_item,list_data);
                                gridView.setAdapter(adapter);

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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
