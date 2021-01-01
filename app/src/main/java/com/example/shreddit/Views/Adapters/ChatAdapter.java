package com.example.shreddit.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Chat;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final LayoutInflater mInflater;
    public List<Chat> mChats; // Cached copy of chats
    private Context context;
    private ChatAdapter.OnItemClickListener listener;

    public ChatAdapter(Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        if (mChats != null) {
            Chat current = mChats.get(position);
            if(!current.getLastMessage().isEmpty())
                holder.last_mssg.setText(current.getLastMessage());
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

            if(UserFirebaseModel.mUser.getUsername_c().equals(current.getName1_c())) {
                holder.users_name.setText(current.getName2());
                if(!current.getUserImage2().isEmpty())
                    imageLoader.displayImage(current.getUserImage2(), holder.boardIcon, options);
            }
            else {
                holder.users_name.setText(current.getName1());
                if(!current.getUserImage1().isEmpty())
                    imageLoader.displayImage(current.getUserImage1(), holder.boardIcon, options);
            }

            holder.bind(current,listener);

        } else {
            holder.users_name.setText("Loading...");
        }

    }

    public void setChats(List<Chat> chats) {
        mChats = chats;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mChats != null)
            return mChats.size();
        else return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final CircleImageView boardIcon;
        private final TextView users_name;
        private final TextView last_mssg;

        private ChatViewHolder(View itemView) {
            super(itemView);
            boardIcon = itemView.findViewById(R.id.boardIcon);
            users_name = itemView.findViewById(R.id.users_name);
            last_mssg = itemView.findViewById(R.id.last_mssg);
        }
        public void bind(Chat current, ChatAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(current);
                }
            });
        }

    }
    public interface OnItemClickListener{
        void onItemClick(Chat chat);
    }

}
