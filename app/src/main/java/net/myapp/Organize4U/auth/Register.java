package net.myapp.Organize4U.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.myapp.Organize4U.MainActivity;
import net.myapp.Organize4U.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText rUserName,rUserEmail,rUserPass,rUserConfPass;
    Button syncAccount;
    TextView loginAct;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseUser user2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Connect to FireNotes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rUserName = findViewById(R.id.userName);
        rUserEmail = findViewById(R.id.userEmail);
        rUserPass = findViewById(R.id.password);
        rUserConfPass = findViewById(R.id.passwordConfirm);

        syncAccount = findViewById(R.id.createAccount);
        loginAct = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar4);

        fAuth = FirebaseAuth.getInstance();

        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        syncAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uUsername = rUserName.getText().toString();
                String uUserEmail = rUserEmail.getText().toString();
                String uUserPass = rUserPass.getText().toString();
                String uConfPass = rUserConfPass.getText().toString();

                if(uUserEmail.isEmpty() || uUsername.isEmpty() || uUserPass.isEmpty() || uConfPass.isEmpty()){
                    Toast.makeText(Register.this, "All Fields Are Required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!uUserPass.equals(uConfPass)){
                    rUserConfPass.setError("Password Do not Match.");
                }

                progressBar.setVisibility(View.VISIBLE);




                AuthCredential credential = EmailAuthProvider.getCredential(uUserEmail,uUserPass);
                fAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Register.this, "Registered", Toast.LENGTH_SHORT).show();
                       FirebaseUser user2 = fAuth.getCurrentUser();
                       String email = user2.getEmail();
                       String name =uUsername;
                        String Uid = user2.getUid();
                        HashMap<Object, String> hashMap = new HashMap<>();
                        hashMap.put("email",email);
                        hashMap.put("uid",Uid);
                        hashMap.put("name",name);
                        hashMap.put("phone","");
                        hashMap.put("image","");
                        hashMap.put("cover","");
                        FirebaseDatabase database =FirebaseDatabase.getInstance();
                        DatabaseReference reference =database.getReference("Users");
                        reference.child(Uid).setValue(hashMap);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        FirebaseUser usr = fAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uUsername)
                                .build();
                        usr.updateProfile(request);

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to Connect. Try Again.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

            }
        });






    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
