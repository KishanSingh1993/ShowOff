package brenda.com.showoff.Home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import brenda.com.showoff.R;
import brenda.com.showoff.activity.ActivityAdapter;
import brenda.com.showoff.activity.ActivityModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private ArrayList<PostItem> dataSet;

    private Context context;

    public CommentAdapter(Context context, ArrayList<PostItem> postList) {

        this.dataSet = postList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textComment =  itemView.findViewById(R.id.comment);
        }
    }


    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment_view, viewGroup, false);

        CommentAdapter.MyViewHolder myViewHolder = new CommentAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder myViewHolder, int i) {

        String postComments = dataSet.get(i).getPost_comments();

        String userName = dataSet.get(i).getPost_username();

        String blank = " ";

        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString redSpannable= new SpannableString(userName);
        redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,105,180)), 0, userName.length(), 0);
        builder.append(redSpannable);

        SpannableString blankSpannable= new SpannableString(blank);
        builder.append(blankSpannable);

        SpannableString whiteSpannable= new SpannableString(postComments);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, postComments.length(), 0);
        builder.append(whiteSpannable);

        myViewHolder.textComment.setText(builder, TextView.BufferType.SPANNABLE);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
