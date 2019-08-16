package com.example.tomho.specforu.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tomho.specforu.ChatActivity;
import com.example.tomho.specforu.R;
import com.example.tomho.specforu.datastrucuture.Friends;

import java.util.List;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendViewHolder> {

    private Context context;
    private List<Friends> listData;
    private View view;

    public FriendsRecyclerViewAdapter(Context context, List<Friends> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.item_friends,parent,false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, final int position) {

        holder.img_photo.setImageResource(listData.get(position).getPhoto());
        holder.tv_name.setText(listData.get(position).getName());
        holder.tv_message.setText(listData.get(position).getMessage());
        holder.tv_lastMessageTime.setText(listData.get(position).getLastMessageTime());
        holder.tv_unreadMessage.setText(listData.get(position).getUnreadMessage());

        // Built up the onClick method
        holder.item_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("photo", listData.get(position).getPhoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_friends;
        private ImageView img_photo;
        private TextView tv_name;
        private TextView tv_message;
        private TextView tv_lastMessageTime;
        private TextView tv_unreadMessage;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            item_friends = (LinearLayout) itemView.findViewById(R.id.item_friends);
            img_photo = (ImageView)itemView.findViewById(R.id.img_friend);
            tv_name = (TextView) itemView.findViewById(R.id.friend_name);
            tv_message = (TextView) itemView.findViewById(R.id.friend_message);
            tv_lastMessageTime = (TextView) itemView.findViewById(R.id.friend_message_time);
            tv_unreadMessage = (TextView) itemView.findViewById(R.id.friend_message_num);

        }
    }
}
