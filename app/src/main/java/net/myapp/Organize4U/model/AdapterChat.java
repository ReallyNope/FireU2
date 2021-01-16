package net.myapp.Organize4U.model;

import android.content.Context;
import android.icu.text.UFormat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import net.myapp.Organize4U.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {


    private  static final int MSG_TYPE_LEFT = 0;
    private  static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
FirebaseUser fuser;
    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == MSG_TYPE_RIGHT){
View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, viewGroup, false);
return new MyHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, viewGroup, false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        try {
            cal.setTimeInMillis(Long.parseLong(timeStamp));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        String dateTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        myHolder.messageTv.setText(message);
        myHolder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(myHolder.profileTv);
        } catch (Exception e) {

        }
        if (position == chatList.size() - 1) {
            if (chatList.get(position).isSeen()) {
                myHolder.isSeenTv.setText("Seen");
            } else {
                myHolder.isSeenTv.setText("Delivered");
            }
        } else {
myHolder.isSeenTv.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }


    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }

    }

    class MyHolder extends RecyclerView.ViewHolder{


        ImageView profileTv;
        TextView messageTv, timeTv, isSeenTv;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            profileTv = itemView.findViewById(R.id.profileTV);
            messageTv = itemView.findViewById(R.id.messageEt);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
        }
    }
}
