package net.myapp.Organize4U.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.myapp.Organize4U.CallingActivity;
import net.myapp.Organize4U.ChatActivity;
import net.myapp.Organize4U.R;


import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {


    Context context;
    List<ModelUsers> userList;

    public AdapterUsers(Context context, List<ModelUsers> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String hisUID = userList.get(position).getUid();
String userImage = userList.get(position).getImage();
final String userName = userList.get(position).getName();
final String userEmail = userList.get(position).getEmail();

holder.mNameTV.setText(userName);
holder.mEmailTV.setText(userEmail);
try{
    Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img_violet).into(holder.mAvatarTV);
}catch (Exception e){
}
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String options[] = {"Chat","Video Chat"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which ==0) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("hisUid", hisUID);
                    context.startActivity(intent);
                }
                else if (which==1){
                    Intent intent = new Intent(context, CallingActivity.class);
                    intent.putExtra("visit_user_id", hisUID);
                    intent.putExtra("userName", userName);
                    context.startActivity(intent);
                }


            }

        });
        builder.create().show();
    }
});




    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
ImageView mAvatarTV;
TextView mNameTV, mEmailTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarTV = itemView.findViewById(R.id.avatarTV);
            mNameTV = itemView.findViewById(R.id.nameTV);
            mEmailTV = itemView.findViewById(R.id.emailTV);
        }
    }

}
