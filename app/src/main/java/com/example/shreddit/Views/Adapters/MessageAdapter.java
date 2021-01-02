package com.example.shreddit.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Message;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final LayoutInflater mInflater;
    public List<Message> mMessages; // Cached copy of messages
    private Context context;
    public RecyclerView recyclerView;
    public String imageUrl_him;
    public String imageUrl_me;
    public MessageAdapter(Context context,String imageUrl_him) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.imageUrl_him = imageUrl_him;
        this.imageUrl_me = UserFirebaseModel.mUser.getImageUrl();
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        if (mMessages != null) {
            Message current = mMessages.get(position);
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            if(current.getSender().equalsIgnoreCase(UserFirebaseModel.mUser.getUsername())){
                holder.layout_him.setVisibility(View.GONE);
                holder.layout_me.setVisibility(View.VISIBLE);
                holder.msg_me.setText(current.getMessage());
                if(imageUrl_me.startsWith("http")) {
                    imageLoader.displayImage(imageUrl_me, holder.img_me, options);
                }
            }
            else{
                holder.layout_him.setVisibility(View.VISIBLE);
                holder.layout_me.setVisibility(View.GONE);
                holder.msg_him.setText(current.getMessage());
                if(imageUrl_him.startsWith("http")) {
                    imageLoader.displayImage(imageUrl_him, holder.img_him, options);
                }
            }

        }

    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
        recyclerView.scrollToPosition(this.getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        if (mMessages != null)
            return mMessages.size();
        else return 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout layout_me;
        private final LinearLayout layout_him;
        private final TextView msg_me;
        private final TextView msg_him;
        private final CircleImageView img_me;
        private final CircleImageView img_him;

        private MessageViewHolder(View itemView) {
            super(itemView);
            layout_me = itemView.findViewById(R.id.layout_me);
            layout_him = itemView.findViewById(R.id.layout_him);
            msg_me = itemView.findViewById(R.id.msg_me);
            msg_him = itemView.findViewById(R.id.msg_him);
            img_me = itemView.findViewById(R.id.imageView_me);
            img_him = itemView.findViewById(R.id.imageView_him);
        }

    }
}
