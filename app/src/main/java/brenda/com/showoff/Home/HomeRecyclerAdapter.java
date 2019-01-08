package brenda.com.showoff.Home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import brenda.com.showoff.AppController;
import brenda.com.showoff.R;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostItem> posts_list;
    private Activity activity;
    private Typeface typeface;
    private ImageLoader imageLoader;
    private SharedPreferences sharedPreferences;
    String PREFS_SHOWOFF = "showoff";

    //Constructor
    public HomeRecyclerAdapter(Activity context, ArrayList<PostItem> posts_list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.posts_list = posts_list;
        this.activity = context;
    }

    @Override
    public HomeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_image_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Initialize the viewholder class.
    public class ViewHolder extends RecyclerView.ViewHolder {


      private ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);


        }
    }

    //Function when view is binded to the UI.
    @Override
    public void onBindViewHolder(final HomeRecyclerAdapter.ViewHolder viewHolder, final int position) {

        String image_url = posts_list.get(position).getPost_url();
        imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(image_url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Image Load Error in user image: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    viewHolder.thumbnail.setImageBitmap(response.getBitmap());

                }
            }
        });


    }

        //Get count of elements in games arraylist.
        @Override
        public int getItemCount () {
            return posts_list.size();
        }
    }

