package com.example.elessar1992.friendsrating.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elessar1992.friendsrating.ChatActivity;
import com.example.elessar1992.friendsrating.R;
import com.example.elessar1992.friendsrating.Models.UsersModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elessar1992 on 6/6/19.
 */

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.UsersHolder>
{
    Context context;
    List<UsersModel> listOfUsers;

    public AdapterUsers(Context context, List<UsersModel> listOfUsers) {
        this.context = context;
        this.listOfUsers = listOfUsers;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.users_list,viewGroup,false);
        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder usersHolder, final int i)
    {
        //getting the data
        final String hisUID = listOfUsers.get(i).getUid();
        String userPicture = listOfUsers.get(i).getImage();
        String userName = listOfUsers.get(i).getName();
        final String userEmail = listOfUsers.get(i).getEmail();
        //setting the data
        usersHolder.userName.setText(userName);
        usersHolder.userEmail.setText(userEmail);
        try
        {
            Picasso.get().load(userPicture)
                .placeholder(R.drawable.ic_user_def_pic)
                .into(usersHolder.userImage);
        }
        catch (Exception e)
        {
            //Picasso.get().load(R.drawable.ic_def_picture);
        }
        usersHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(context,""+userEmail,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid", hisUID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }

    class UsersHolder extends RecyclerView.ViewHolder
    {
        ImageView userImage;
        TextView userName;
        TextView userEmail;

        public UsersHolder(@NonNull View itemView)
        {
            super(itemView);
            userImage = itemView.findViewById(R.id.avatarId);
            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email);
        }
    }

}
