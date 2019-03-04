package brenda.com.showoff.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import brenda.com.showoff.AppController;
import brenda.com.showoff.Home.HomeRecyclerAdapter;
import brenda.com.showoff.Home.PostItem;
import brenda.com.showoff.R;
import brenda.com.showoff.Util.RoundedImageView;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {

    private ArrayList<ActivityModel> dataSet;
    private Context context;
    FragmentManager mFragmentManager;
    android.app.FragmentTransaction mFragmentTransaction;
    private String userId;
    private View view;
    private MediaPlayer mediaPlayer;


    ActivityAdapter(Context context, ArrayList<ActivityModel> movieList) {

        this.dataSet = movieList;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,upvote;
        ImageView postImage;
        VideoView videoView;
        LinearLayout linearLayout;


        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName =  itemView.findViewById(R.id.name);
            this.postImage = itemView.findViewById(R.id.imageView);
            this.videoView = itemView.findViewById(R.id.video);
            this.linearLayout = itemView.findViewById(R.id.ll);
            //this.upvote =  itemView.findViewById(R.id.type);


        }
    }

    public ActivityAdapter(ArrayList<ActivityModel> data) {
        this.dataSet = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_list, parent, false);


        context = view.getContext();
        ActivityAdapter.MyViewHolder myViewHolder = new ActivityAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {



        String upVoteData = dataSet.get(i).getUpvote();

        String imgurl = dataSet.get(i).getImgurl();

        userId = dataSet.get(i).getUserId();


        MediaController mediaController= new MediaController(context);
        mediaController.setAnchorView(myViewHolder.videoView);

        if (userId.matches("6")){

            //removeAt(i);
        }

        if (imgurl.endsWith(".mp4")){
            myViewHolder.postImage.setVisibility(View.INVISIBLE);
            myViewHolder.videoView.setVisibility(View.VISIBLE);
            Uri video = Uri.parse(imgurl);
            myViewHolder.videoView.setVideoURI(video);
            myViewHolder.videoView.seekTo( 1 );
//            myViewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//                mp.setVolume(0,0);
//                //myViewHolder.videoView.start();
//
//            }
//        });

        }
        else if(imgurl.endsWith(".png")){
            //myViewHolder.videoView.setVisibility(View.INVISIBLE);
            Picasso.with(context)
                    .load(imgurl)
                    .into(myViewHolder.postImage);        }

        if (upVoteData.matches("1")){
            SpannableStringBuilder builder = new SpannableStringBuilder();
            //myViewHolder.upvote.setText(R.string.upvoted_text);
            String name = dataSet.get(i).getFname();
            String up = "Up voted your post.";
            String blank = " ";

            SpannableString redSpannable= new SpannableString(name);
            redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,105,180)), 0, name.length(), 0);
            builder.append(redSpannable);

            SpannableString blankSpannable= new SpannableString(blank);
            builder.append(blankSpannable);

            SpannableString whiteSpannable= new SpannableString(up);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, up.length(), 0);
            builder.append(whiteSpannable);

            myViewHolder.textViewName.setText(builder, TextView.BufferType.SPANNABLE);
        }
        else if (upVoteData.matches("0")){
            SpannableStringBuilder builder = new SpannableStringBuilder();
            //myViewHolder.upvote.setText(R.string.downvoted_text);
            String name = dataSet.get(i).getFname();
            String up = "Down voted your post.";
            String blank = " ";

            SpannableString redSpannable= new SpannableString(name);
            redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,105,180)), 0, name.length(), 0);
            builder.append(redSpannable);

            SpannableString blankSpannable= new SpannableString(blank);
            builder.append(blankSpannable);

            SpannableString whiteSpannable= new SpannableString(up);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, up.length(), 0);
            builder.append(whiteSpannable);

            myViewHolder.textViewName.setText(builder, TextView.BufferType.SPANNABLE);
            //myViewHolder.textViewName.setText(String.format("%s,Down voted your post", dataSet.get(i).getFname()));
        }
        else {

            //myViewHolder.upvote.setText(dataSet.get(i).getUpvote());
            SpannableStringBuilder builder = new SpannableStringBuilder();
            String name = dataSet.get(i).getFname();
            String up = "Commented on your post. ";
            String blank = " ";
            String cmnt = dataSet.get(i).getUpvote();

            SpannableString redSpannable= new SpannableString(name);
            redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,105,180)), 0, name.length(), 0);
            builder.append(redSpannable);

            SpannableString blankSpannable= new SpannableString(blank);
            builder.append(blankSpannable);

            SpannableString whiteSpannable= new SpannableString(up);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, up.length(), 0);
            builder.append(whiteSpannable);

            SpannableString cmntSpannable= new SpannableString(cmnt);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, up.length(), 0);
            builder.append(cmntSpannable);

            myViewHolder.textViewName.setText(builder, TextView.BufferType.SPANNABLE);

            //myViewHolder.textViewName.setText(String.format("%s,%s", dataSet.get(i).getFname(), dataSet.get(i).getUpvote()));
        }


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private void removeAt(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataSet.size());
    }
}

