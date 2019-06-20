package com.example.elessar1992.friendsrating.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elessar1992.friendsrating.Models.ChatModel;
import com.example.elessar1992.friendsrating.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
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
    public void onBindViewHolder(@NonNull ChatHolder chatHolder,final int i)
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
        chatHolder.mLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        deleteMessage(i);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

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

    private void deleteMessage(int i)
    {
        final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        /* getting timestamp of the message that user clink
           compare that timestamp of clinked message with all messages in chats
           which both values matvhes delete the message
           and this let sender to delete and his and receivers message
        */
        String mesTimsStamp = chatModels.get(i).getTimestamp();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = databaseReference.orderByChild("timestamp").equalTo(mesTimsStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //wanna alow sender to delete his message, then compare sender value with
                    //current Signed in user's uid! if its match, then they can delete
                    if (ds.child("sender").getValue().equals(currentUid))
                    {
                        //either 1) remove the message from the chat
                        //2) set the value of message "this message was deleted"

                        // method 1:
                        //ds.getRef().removeValue();
                        // method 2:
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "This message was deleted...");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context,"Message Deleted...",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(context,"You can only delete your message...",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
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
        LinearLayout mLayout;
        //TextView seenId;
        public ChatHolder(@Nullable View itemView)
        {
            super(itemView);

            profileImageId = itemView.findViewById(R.id.profilePic);
            messageId = itemView.findViewById(R.id.messageID);
            timeId = itemView.findViewById(R.id.timeID);
            mLayout = itemView.findViewById(R.id.messageLayout);
            //seenId = itemView.findViewById(R.id.seenID);
        }
    }
}
