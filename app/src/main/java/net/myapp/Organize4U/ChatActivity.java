package net.myapp.Organize4U;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.myapp.Organize4U.model.Adapter;
import net.myapp.Organize4U.model.AdapterChat;
import net.myapp.Organize4U.model.ModelChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity {


    Toolbar toolbar4;
    RecyclerView recyclerView;
    ImageView profileTV;
    TextView nameTV, userStatusTV;
    EditText messageEt;
    ImageButton sendBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;
    DatabaseReference userRefForSeen;
    ValueEventListener seenListener;
List<ModelChat> chatList;
AdapterChat adapterChat;
    String hisUid;
    String myUid;
    String hisImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar4 = findViewById(R.id.toolbar4);


recyclerView = findViewById(R.id.chat_recyclerView);
profileTV = findViewById(R.id.profileTV);
nameTV = findViewById(R.id.nameTV);
userStatusTV = findViewById(R.id.userStatusTV);
messageEt = findViewById(R.id.messageEt);
sendBtn = findViewById(R.id.sendBtn);
Intent intent = getIntent();

LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
linearLayoutManager.setStackFromEnd(true);
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(linearLayoutManager);
hisUid = intent.getStringExtra("hisUid");
firebaseAuth = FirebaseAuth.getInstance();
firebaseDatabase = firebaseDatabase.getInstance();
usersDbRef = firebaseDatabase.getReference("Users");

        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
String name = "" + ds.child("name").getValue();
hisImage = "" + ds.child("image").getValue();

                    nameTV.setText(name);
                    try {
                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_default_img_white).into(profileTV);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_img_violet).into(profileTV);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
String message = messageEt.getText().toString().trim();
if(TextUtils.isEmpty(message)){
    Toast.makeText(ChatActivity.this, "Cannot send the message...", Toast.LENGTH_SHORT).show();
}
else{
    sendMessage(message);
}
            }
            });

        readMessages();
        seenMessage();
    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {


        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                    chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                    chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

        messageEt.setText("");
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            myUid = user.getUid();
        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }}

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        menu.findItem((R.id.action_search)).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
