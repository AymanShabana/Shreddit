package com.example.shreddit.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Comment;
import com.example.shreddit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final LayoutInflater mInflater;
    public List<Comment> mComments; // Cached copy of comments
    private Context context;

    public CommentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        if (mComments != null) {
            Comment current = mComments.get(position);
            holder.comment_text.setText(current.getComment());
            holder.comment_time.setText(current.getTimeSinceCreation());
            holder.user_name.setText(current.getAuthor());

        } else {
            holder.comment_text.setText("Loading...");
        }

    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mComments != null)
            return mComments.size();
        else return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public final CircleImageView user_icon;
        private final TextView user_name;
        private final TextView comment_time;
        private final TextView comment_text;

        private CommentViewHolder(View itemView) {
            super(itemView);
            user_icon = itemView.findViewById(R.id.user_icon);
            user_name = itemView.findViewById(R.id.user_name);
            comment_time = itemView.findViewById(R.id.comment_time);
            comment_text = itemView.findViewById(R.id.comment_text);
        }

    }
}
