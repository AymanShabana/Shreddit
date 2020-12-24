package com.example.shreddit.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Board;
import com.example.shreddit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {
    private final LayoutInflater mInflater;
    public List<Board> mBoards; // Cached copy of boards
    private Context context;

    public SubAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public SubAdapter.SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.sub_item, parent, false);
        return new SubViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubAdapter.SubViewHolder holder, int position) {
        if (mBoards != null) {
            Board current = mBoards.get(position);
            holder.subreddit_name.setText(current.getName());
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

            if(!current.getIcon_img().isEmpty())
                imageLoader.displayImage(current.getIcon_img(), holder.subreddit_img, options);

        } else {
            holder.subreddit_name.setText("Loading...");
        }

    }

    public void setBoards(List<Board> boards) {
        mBoards = boards;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mBoards != null)
            return mBoards.size();
        else return 0;
    }

    public class SubViewHolder extends RecyclerView.ViewHolder {
        public final CircleImageView subreddit_img;
        private final TextView subreddit_name;

        private SubViewHolder(View itemView) {
            super(itemView);
            subreddit_img = itemView.findViewById(R.id.boardIcon);
            subreddit_name = itemView.findViewById(R.id.boardTitle);
        }

    }
}

