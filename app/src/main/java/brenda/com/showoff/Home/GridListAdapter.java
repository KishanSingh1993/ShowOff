package brenda.com.showoff.Home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import java.util.Objects;

import brenda.com.showoff.R;

public class GridListAdapter extends ArrayAdapter<PostItem> {
    ArrayList<PostItem> listdata;
    Context context;
    int resource;
    SimpleExoPlayer player;

    public GridListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PostItem> listdata) {
        super(context, resource, listdata);
        this.listdata=listdata;
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.home_image_item,null,true);
        }
        PostItem listdata=getItem(position);
        ImageView img=convertView.findViewById(R.id.thumbnail);
        ImageButton play=convertView.findViewById(R.id.play_button);
        //final VideoView videoView=convertView.findViewById(R.id.thumbnail1);
        //MediaController mediaController= new MediaController(context);
        //mediaController.setAnchorView(videoView);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance((Activity) context, trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = convertView.findViewById(R.id.thumbnail1);
        simpleExoPlayerView.setPlayer(player);
        simpleExoPlayerView.hideController();

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory((Activity) context, Util.getUserAgent((Activity) context, "CloudinaryExoplayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();




        final String posturl = Objects.requireNonNull(listdata).getPost_url();
        final String postID = Objects.requireNonNull(listdata).getPost_id();

        if (posturl.endsWith(".png")){

            simpleExoPlayerView.setVisibility(View.GONE);
            play.setVisibility(View.GONE);
            Picasso.with(context)
                    .load(posturl)
                    .into(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("imgurl", posturl);
                    bundle.putString("postID", postID);

                    Fragment imageScreen = new ImageFullScreen();
                    imageScreen.setArguments(bundle);
                    FragmentTransaction ft_signup = ((Activity) context).getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                    ft_signup.replace(R.id.base_layout, imageScreen, "upload"); //Replace the main activity base layout with the fragment.
                    ft_signup.addToBackStack(null);
                    ft_signup.commit();

                }
            });
        }
        else if (posturl.endsWith(".mp4")){
            img.setVisibility(View.GONE);
            // This is the MediaSource representing the media to be played.
            Uri videoUri = Uri.parse("http://www.talenttracker.in/showoff/images/VID_20181230_114553.mp4");
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);

            // Prepare the player with the source.
            //player.prepare(videoSource);
            player.seekTo(1);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("imgurl", posturl);
                    bundle.putString("postID", postID);
                    Fragment upload = new VideoFullScreen();
                    upload.setArguments(bundle);
                    FragmentTransaction ft_signup = ((Activity) context).getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                    ft_signup.replace(R.id.base_layout, upload, "upload"); //Replace the main activity base layout with the fragment.
                    ft_signup.addToBackStack(null);
                    ft_signup.commit();
                }
            });


        }

        if (player!=null) {
            player.release();
            player = null;
        }

        return convertView;
    }
}
