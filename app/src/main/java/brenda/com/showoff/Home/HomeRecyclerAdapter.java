package brenda.com.showoff.Home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import brenda.com.showoff.AppController;
import brenda.com.showoff.Home.fragment.PostComments;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Initialize the viewholder class.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,upvote,txtDesc,txtComment,timeDate;
        ImageView postImage;
        SimpleExoPlayerView player;
        LinearLayout linearLayout;
        SimpleExoPlayer simpleExoPlayer;


        ViewHolder(View view) {
            super(view);

            this.postImage = view.findViewById(R.id.feedImage1);
            this.player = view.findViewById(R.id.thumbnail1);
            this.txtComment = view.findViewById(R.id.comment);
            this.txtDesc = view.findViewById(R.id.txt_desc);
            this.upvote = view.findViewById(R.id.upvote);
            this.textViewName = view.findViewById(R.id.name);
            this.timeDate = view.findViewById(R.id.timestamp);

        }
    }

    //Function when view is binded to the UI.
    @Override
    public void onBindViewHolder(final HomeRecyclerAdapter.ViewHolder viewHolder, final int position) {

        final String post_url = posts_list.get(position).getPost_url();
        String post_type = posts_list.get(position).getType();
        String cmntlink = "<a href='http://www.google.com'> Comments </a>";
        String cmntNo = posts_list.get(position).getPost_comments();
        viewHolder.txtComment.setText(Html.fromHtml(cmntNo+cmntlink));
        viewHolder.txtDesc.setText(posts_list.get(position).getDesc());
        viewHolder.upvote.setText(String.format("%s Upvotes", posts_list.get(position).getPost_upvotes()));
        viewHolder.textViewName.setText(posts_list.get(position).getPost_username());
        viewHolder.timeDate.setText(posts_list.get(position).getTimestamp());

        viewHolder.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("imgurl", post_url);
                bundle.putString("postID", posts_list.get(position).getPost_id());
                Fragment upload = new PostComments();
                upload.setArguments(bundle);
                FragmentTransaction ft_signup = ((Activity) context).getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                ft_signup.replace(R.id.base_layout, upload, "upload"); //Replace the main activity base layout with the fragment.
                ft_signup.addToBackStack(null);
                ft_signup.commit();

            }
        });

        if (post_type.equals("Video")){

            viewHolder.postImage.setVisibility(View.GONE);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            //Initialize the player
            viewHolder.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);


            viewHolder.player.setPlayer(viewHolder.simpleExoPlayer);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(context, Util.getUserAgent(context, "CloudinaryExoplayer"));

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // This is the MediaSource representing the media to be played.
            Uri videoUri = Uri.parse(post_url);
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);

            // Prepare the player with the source.
            viewHolder.simpleExoPlayer.prepare(videoSource);
        }
        else if (post_type.equals("Image")){
            viewHolder.player.setVisibility(View.GONE);
            Picasso.with(context)
                    .load(post_url)
                    .into(viewHolder.postImage);
        }
    }



        //Get count of elements in games arraylist.
        @Override
        public int getItemCount () {
            return posts_list.size();
        }
    }

