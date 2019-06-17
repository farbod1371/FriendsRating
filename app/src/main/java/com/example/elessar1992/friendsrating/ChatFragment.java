package com.example.elessar1992.friendsrating;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by elessar1992 on 6/9/19.
 */

public class ChatFragment extends android.support.v4.app.Fragment
{

    public ChatFragment()
    {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }
}
