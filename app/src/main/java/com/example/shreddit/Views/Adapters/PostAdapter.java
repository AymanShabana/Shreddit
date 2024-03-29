package com.example.shreddit.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostFirebaseModel;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.BoardActivity;
import com.example.shreddit.Views.PostDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final LayoutInflater mInflater;
    private List<Post> mPosts; // Cached copy of posts
    private Context context;
    private OnItemClickListener listener;

    public PostAdapter(Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        if (mPosts != null) {
            Post current = mPosts.get(position);
            holder.subreddit_name.setText(current.getBoard());
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
            holder.subreddit_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BoardActivity.class);
                    intent.putExtra("board", holder.subreddit_name.getText().toString());
                    context.startActivity(intent);
                }
            });
            holder.upvote_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.upvote_btn.getTag().equals(1)){
                        holder.upvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        holder.downvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        holder.upvote_btn.setTag(0);
                        holder.upvotes_lbl.setText((Integer.parseInt(holder.upvotes_lbl.getText().toString())-1)+"");
                    }
                    else{
                        holder.upvote_btn.setImageResource(R.drawable.up_pressed);
                        holder.downvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        if(holder.upvote_btn.getTag().equals(0)){
                            holder.upvotes_lbl.setText((Integer.parseInt(holder.upvotes_lbl.getText().toString())+1)+"");
                        }
                        else{
                            holder.upvotes_lbl.setText((Integer.parseInt(holder.upvotes_lbl.getText().toString())+2)+"");
                        }
                        holder.upvote_btn.setTag(1);
                    }

                    PostFirebaseModel.upvotePost(current.getId(),new MyCallbackInterface() {
                        @Override
                        public void onAuthFinished(String result) {
                            if(result.startsWith("Failure: ")){
                            }
                            else{
                               // holder.upvotes_lbl.setText(result);
                            }
                        }
                    });
                }
            });
            holder.downvote_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.upvote_btn.getTag().equals(2)){
                        holder.upvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        holder.downvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        holder.upvote_btn.setTag(0);
                        holder.upvotes_lbl.setText((Integer.parseInt(holder.upvotes_lbl.getText().toString())+1)+"");
                    }
                    else{
                        holder.upvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        holder.downvote_btn.setImageResource(R.drawable.down_pressed);
                        if(holder.upvote_btn.getTag().equals(0)){
                            holder.upvotes_lbl.setText((Integer.parseInt(holder.upvotes_lbl.getText().toString())-1)+"");
                        }
                        else{
                            holder.upvotes_lbl.setText((Integer.parseInt(holder.upvotes_lbl.getText().toString())-2)+"");
                        }
                        holder.upvote_btn.setTag(2);
                    }
                    PostFirebaseModel.downvotePost(current.getId(),new MyCallbackInterface() {
                        @Override
                        public void onAuthFinished(String result) {
                            if(result.startsWith("Failure: ")){
                            }
                            else{
                                //holder.upvotes_lbl.setText(result);
                            }
                        }
                    });
                }
            });
            PostFirebaseModel.voteExists(current.getId(), new MyCallbackInterface() {
                @Override
                public void onAuthFinished(String result) {
                    switch (result){
                        case "1":
                            holder.upvote_btn.setImageResource(R.drawable.up_pressed);
                            holder.downvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                            holder.upvote_btn.setTag(1);
                            break;
                        case "2":
                            holder.upvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                            holder.downvote_btn.setImageResource(R.drawable.down_pressed);
                            holder.upvote_btn.setTag(2);
                            break;
                        default:
                            holder.upvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                            holder.downvote_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                            holder.upvote_btn.setTag(0);
                    }
                }
            });

        } else {
            holder.subreddit_name.setText("Loading...");
        }

    }
    public void setPosts(List<Post> posts){
        mPosts = posts;
        notifyDataSetChanged();
    }
    public void addPosts(List<Post> posts){
        mPosts.addAll(posts);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mPosts != null)
            return mPosts.size();
        else return 0;
    }
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        public final CircleImageView subreddit_img;
        private final TextView subreddit_name;
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

        private PostViewHolder(View itemView) {
            super(itemView);
            subreddit_img = itemView.findViewById(R.id.subreddit_img);
            subreddit_name = itemView.findViewById(R.id.subreddit_name);
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
