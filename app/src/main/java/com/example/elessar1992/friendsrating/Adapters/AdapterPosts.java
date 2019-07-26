package com.example.elessar1992.friendsrating.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elessar1992.friendsrating.Models.PostModel;
import com.example.elessar1992.friendsrating.R;
import com.squareup.picasso.Picasso;


import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by elessar1992 on 7/24/19.
 */

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder>
{
    Context context;
    List<PostModel> postModels;

    public AdapterPosts(Context context, List<PostModel> postModels)
    {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        //inflate layout row post
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts,viewGroup,false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i)
    {
        //getting data
        String uid = postModels.get(i).getUid();
        String uEmail = postModels.get(i).getuEmail();
        String uName = postModels.get(i).getuName();
        String uDp = postModels.get(i).getuDp();
        String pId = postModels.get(i).getpId();
        String pTitle = postModels.get(i).getpTitle();
        String pDescription = postModels.get(i).getpDescr();
        String pImage = postModels.get(i).getpImage();
        String pTimeStamp = postModels.get(i).getpTime();

        //convert timeStamp
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set data
        myHolder.uNamePv.setText(uName);
        myHolder.pTimePv.setText(pTime);
        myHolder.pTitle.setText(pTitle);
        myHolder.pDescriptionPv.setText(pDescription);

        //set user dp
        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_def_pic).into(myHolder.uPicturePv);
        }
        catch (Exception e)
        {

        }
        if(pImage.equals("noImage"))
        {
            //hiding imageview
            myHolder.pImagePv.setVisibility(View.GONE);

        }
        else
        {
            try {
                Picasso.get().load(pImage).into(myHolder.pImagePv);
            }
            catch (Exception e)
            {

            }
        }
        //set post image

        //handdle button clicks
        myHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"More", Toast.LENGTH_SHORT).show();
            }
        });
        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Like", Toast.LENGTH_SHORT).show();
            }
        });
        myHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Comment", Toast.LENGTH_SHORT).show();
            }
        });
        myHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Share", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView uPicturePv, pImagePv;
        TextView uNamePv, pTimePv, pDescriptionPv, pLikesPv, pTitle;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, shareBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            uPicturePv = itemView.findViewById(R.id.uploadPic);
            pImagePv = itemView.findViewById(R.id.imageP);
            uNamePv = itemView.findViewById(R.id.userNameP);
            pTimePv = itemView.findViewById(R.id.timeP);
            pTitle = itemView.findViewById(R.id.titleP);
            pDescriptionPv = itemView.findViewById(R.id.descriptionP);
            pLikesPv = itemView.findViewById(R.id.likesP);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            likeBtn = itemView.findViewById(R.id.LikeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
        }
    }

}
