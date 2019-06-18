package com.example.elessar1992.friendsrating;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elessar1992.friendsrating.Adapters.AdapterChat;
import com.example.elessar1992.friendsrating.Models.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by elessar1992 on 6/9/19.
 */

public class ChatActivity extends AppCompatActivity
{
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileCID;
    TextView nameCID;
    TextView userStatusCID;
    EditText messageCID;
    ImageButton sendBtn;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    DatabaseReference dataForSeen;

    List<ChatModel> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recyclerView);
        profileCID = findViewById(R.id.profileID);
        nameCID = findViewById(R.id.nameCu);
        userStatusCID = findViewById(R.id.statusCu);
        messageCID = findViewById(R.id.messageId);
        sendBtn = findViewById(R.id.sendId);

        //Layout for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");

        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //searching user to get the users informatiom
        Query userQuery = databaseReference.orderByChild("uid").equalTo(hisUid);

        //get the picture and name of the user
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until require info is recieved
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //getting data
                    String name =""+ ds.child("name").getValue();
                    hisImage =""+ ds.child("image").getValue();
                    String typingStatus =""+ ds.child("typingTo").getValue();
                    if(typingStatus.equals(myUid))
                        userStatusCID.setText("Typing...");
                    else
                    {
                        //getting the value of onlineStatus
                        String onlineStatus = ""+ ds.child("onlineStatus").getValue();
                        if(onlineStatus.equals("online"))
                        {
                            userStatusCID.setText(onlineStatus);
                        }
                        else
                        {
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                            calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                            userStatusCID.setText("Last Seen at: "+ dateTime);
                        }
                    }

                    //setting data
                    nameCID.setText(name);


                    try
                    {
                        //already image recieved , making it to imageview in toolbar
                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_user_def_pic).into(profileCID);
                    }
                    catch (Exception e)
                    {
                        Picasso.get().load(R.drawable.ic_user_def_pic).into(profileCID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String message = messageCID.getText().toString().trim();
                //checking if text is empty
                if(TextUtils.isEmpty(message))
                {
                    Toast.makeText(ChatActivity.this,"message cant be empty...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    sendingMessage(message);
                }
            }
        });
        messageCID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0)
                    checkingTypeStatus("noOne");
                else
                    checkingTypeStatus(hisUid);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readMessages();
        seenMessage();

    }

    private void seenMessage()
    {
        dataForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        valueEventListener = dataForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ChatModel chatModel = ds.getValue(ChatModel.class);
                    if(chatModel.getReceiver().equals(myUid) && chatModel.getSender().equals(hisUid))
                    {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages()
    {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ChatModel chatModel = ds.getValue(ChatModel.class);
                    if(chatModel.getReceiver().equals(myUid) && chatModel.getSender().equals(hisUid)
                            || chatModel.getReceiver().equals(hisUid) && chatModel.getSender().equals(myUid))
                    {
                        chatList.add(chatModel);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this,chatList,hisImage);
                    //setting adapter to recyvlerview
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendingMessage(String message)
    {
        String timestamp = String.valueOf(System.currentTimeMillis());
        //everytime user send message, a new child will be created in chats note that
        // will contain key values, either UID of sender or UID of receiver
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);
        messageCID.setText("");
    }

    private void checkingUsers()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            //signed in User uid
            myUid = user.getUid();
        }
        else
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private void checkingOnlineStatus(String status)
    {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        //updating value of online status of current user
        dbReference.updateChildren(hashMap);
    }

    private void checkingTypeStatus(String typing)
    {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo", typing);
        //updating value of online status of current user
        dbReference.updateChildren(hashMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //hidding searchview
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
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

    @Override
    protected void onStart()
    {
        checkingUsers();
        checkingOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        checkingOnlineStatus(timeStamp);
        checkingTypeStatus("noOne");
        //dataForSeen.removeEventListener(valueEventListener);
    }

    @Override
    protected void onResume()
    {
        checkingOnlineStatus("online");
        super.onResume();
    }
}
