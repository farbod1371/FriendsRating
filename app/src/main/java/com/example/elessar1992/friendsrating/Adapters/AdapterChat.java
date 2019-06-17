package com.example.elessar1992.friendsrating.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elessar1992.friendsrating.Models.ChatModel;
import com.example.elessar1992.friendsrating.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by elessar1992 on 6/11/19.
 */

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatHolder>
{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<ChatModel> chatModels;
    private String imageURL;

    FirebaseUser firebaseUser;

    public AdapterChat(Context context, List<ChatModel> chatModels, String imageURL) {
        this.context = context;
        this.chatModels = chatModels;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        if(i == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_row_second,viewGroup,false);
            return new ChatHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_row_first,viewGroup,false);
            return new ChatHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder chatHolder, int i)
    {
        //getting data
        String message = chatModels.get(i).getMessage();
        String timeStamp = chatModels.get(i).getTimestamp();

        //convert time stamp to calendef form
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        chatHolder.messageId.setText(message);
        chatHolder.timeId.setText(dateTime);
        try
        {
            Picasso.get().load(imageURL).into(chatHolder.profileImageId);
        }
        catch (Exception e)
        {

        }
        /*if(i == chatModels.size() - 1)
        {
            if(chatModels.get(i).isSeen())
                chatHolder.seenId.setText("Seen");
            else
                chatHolder.seenId.setText("Delivered");
        }
        else
            chatHolder.seenId.setVisibility(View.GONE);*/
    }

    @Override
    public int getItemCount()
    {
        return chatModels.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        //currently signed in user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatModels.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }

    }

    class ChatHolder extends RecyclerView.ViewHolder
    {
        ImageView profileImageId;
        TextView messageId;
        TextView timeId;
        //TextView seenId;
        public ChatHolder(@Nullable View itemView)
        {
            super(itemView);

            profileImageId = itemView.findViewById(R.id.profilePic);
            messageId = itemView.findViewById(R.id.messageID);
            timeId = itemView.findViewById(R.id.timeID);
            //seenId = itemView.findViewById(R.id.seenID);
        }
    }
}
