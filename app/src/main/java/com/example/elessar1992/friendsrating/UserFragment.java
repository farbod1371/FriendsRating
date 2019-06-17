package com.example.elessar1992.friendsrating;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.elessar1992.friendsrating.Adapters.AdapterUsers;
import com.example.elessar1992.friendsrating.Models.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends android.support.v4.app.Fragment
{
    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<UsersModel> userList;

    FirebaseAuth firebaseAuth;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.userRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();
        gettingAllUsers();

        return view;
    }
    private void gettingAllUsers()
    {
        //get current User
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //get the address of database named "Users" containing users info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    UsersModel usersModel = ds.getValue(UsersModel.class);
                    if(!usersModel.getUid().equals(firebaseUser.getUid()))
                    {
                        userList.add(usersModel);
                    }
                    adapterUsers = new AdapterUsers(getActivity(), userList);
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void searchUsers(final String query)
    {
        //get current User
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //get the address of database named "Users" containing users info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    UsersModel usersModel = ds.getValue(UsersModel.class);
                    //getting all the search users except the signed in one
                    if(!usersModel.getUid().equals(firebaseUser.getUid()))
                    {
                        if(usersModel.getName().toLowerCase().contains(query.toLowerCase())
                           || usersModel.getEmail().toLowerCase().contains(query.toLowerCase()))
                        {
                            userList.add(usersModel);
                        }
                    }

                    adapterUsers = new AdapterUsers(getActivity(), userList);
                    //refresh adapter
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkingUsers()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            //myProfile.setText(user.getEmail());
        }
        else
        {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true); //in order to show menu in option Fragment
        super.onCreate(savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main,menu);

        //Searchview
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                //when user press search button, if search query is not empty then search
                if (!TextUtils.isEmpty(s.trim()))
                {
                    //searching based on text
                    searchUsers(s);
                }
                else
                {
                    gettingAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                if (!TextUtils.isEmpty(s.trim()))
                {
                    //searching based on text
                    searchUsers(s);
                }
                else
                {
                    gettingAllUsers();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkingUsers();
        }
        return super.onOptionsItemSelected(item);
    }

}
