package com.example.shreddit.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private final LayoutInflater mInflater;
    private List<Post> mPosts; // Cached copy of posts
    private Context context;
    private OnItemClickListener listener;

    public BoardAdapter(Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BoardAdapter.BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.board_item, parent, false);
        return new BoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.BoardViewHolder holder, int position) {
        if (mPosts != null) {
            Post current = mPosts.get(position);
            holder.user_name.setText("Posted by "+current.getAuthor());
            holder.post_time.setText(current.getTimeSinceCreation());
            holder.post_title.setText(current.getTitle());
            holder.upvotes_lbl.setText(current.getUpvotes()+"");
            holder.comments_lbl.setText(current.getComments()+"");
            //Bitmap temp = MySingleton.getInstance(context).getImageLoader().
            //new Utils.DownloadImageTask(holder).execute(current.getPostImg());
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            switch (current.getType()){
                case "link":
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.post_img.setVisibility(View.VISIBLE);
                    holder.post_img.setImageResource(R.drawable.ic_baseline_link2_24);
                    break;
                case "image":
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.post_img.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(current.getPostImg(), holder.post_img, options);
                    break;
                case "text":
                    holder.post_img.setVisibility(View.GONE);
                    break;
                case "video":
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.post_img.setVisibility(View.VISIBLE);
                    holder.post_img.setImageResource(R.drawable.ic_baseline_videocam_w_24);
                    break;
            }
            holder.bind(current,listener);
        } else {
            holder.user_name.setText("Loading...");
        }

    }
    public void setPosts(List<Post> posts){
        mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPosts != null)
            return mPosts.size();
        else return 0;
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        private final TextView user_name;
        private final TextView post_time;
        private final ImageButton more_btn;
        private final TextView awards_lbl;
        private final TextView post_title;
        public final ImageView post_img;
        private final ImageButton upvote_btn;
        private final TextView upvotes_lbl;
        private final ImageButton downvote_btn;
        private final ImageView comments_btn;
        private final TextView comments_lbl;
        private final TextView share_btn;
        private final RichLinkView richLinkView;

        private BoardViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            post_time = itemView.findViewById(R.id.post_time);
            more_btn = itemView.findViewById(R.id.more_btn);
            awards_lbl = itemView.findViewById(R.id.awards_lbl);
            post_title = itemView.findViewById(R.id.post_title);
            post_img = itemView.findViewById(R.id.post_img);
            upvote_btn = itemView.findViewById(R.id.upvote_btn);
            upvotes_lbl = itemView.findViewById(R.id.upvotes_lbl);
            downvote_btn = itemView.findViewById(R.id.downvote_btn);
            comments_btn = itemView.findViewById(R.id.comments_btn);
            comments_lbl = itemView.findViewById(R.id.comments_lbl);
            share_btn = itemView.findViewById(R.id.share_btn);
            richLinkView = itemView.findViewById(R.id.richLinkView);
        }

        public void bind(Post current, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(current);
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Post post);
    }

}
