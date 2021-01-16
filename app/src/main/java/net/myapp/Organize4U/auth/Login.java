package net.myapp.Organize4U.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import net.myapp.Organize4U.MainActivity;
import net.myapp.Organize4U.R;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    EditText lEmail,lPassword;
    Button loginNow;
    TextView forgetPass,createAcc;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    ProgressBar spinner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "MainActivity";
    private int RC_SIGN_IN = 100;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login to Organize4u");

        lEmail = findViewById(R.id.email);
        lPassword = findViewById(R.id.lPassword);
        loginNow = findViewById(R.id.loginBtn);

        spinner = findViewById(R.id.progressBar3);

        forgetPass = findViewById(R.id.forgotPasword);
        createAcc = findViewById(R.id.createAccount);
        user = FirebaseAuth.getInstance().getCurrentUser();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        SignInButton google_btn = (SignInButton) findViewById(R.id.google_btn);
        mAuth = FirebaseAuth.getInstance();

        showWarning();
        /*forgetPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }

            private void showRecoverPasswordDialog() {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle("Recover Password");
                    LinearLayout LinearLayout = new LinearLayout(this);
                    final EditText emailet =new EditText(this);
                    emailet.setHint("Email");
                    emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    emailet.setMinEms(16);
                    LinearLayout.addView(emailet);
                    LinearLayout.setPadding(10,10,10,10);
                    builder.setView(LinearLayout);
                    builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String email=emailet.getText().toString().trim();
                            beginRecovery(email);
                        }

                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    builder.create().show();
                };
            private void beginRecovery(String email){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Email sent",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Login.this,"Email sent",Toast.LENGTH_SHORT).show();
                        }}
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

*/


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                } else { } }};
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }


        });
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = lEmail.getText().toString();
                String mPassword = lPassword.getText().toString();

                if(mEmail.isEmpty() || mPassword.isEmpty()){
                    Toast.makeText(Login.this, "Fields Are Required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // delete notes first

                spinner.setVisibility(View.VISIBLE);

                if(fAuth.getCurrentUser().isAnonymous()){
                    FirebaseUser user = fAuth.getCurrentUser();

                    fStore.collection("notes").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "All Temp Notes are Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    });


                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "Temp user Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                fAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if(authResult.getAdditionalUserInfo().isNewUser()) {
                            FirebaseUser user2 = fAuth.getCurrentUser();
                            String email = user2.getEmail();
                            String Uid = user2.getUid();
                            String name = user2.getDisplayName();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", Uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            hashMap.put("cover", "");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(Uid).setValue(hashMap);


                        }
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(Login.this, "Success !", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Login Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(getApplicationContext(), "Sign in Failed!", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        String personName = account.getDisplayName();
                        String personGivenName = account.getGivenName();
                        String personFamilyName = account.getFamilyName();
                        String email = account.getEmail();
                        String Uid2 = user.getUid();
                        Uri personPhoto = account.getPhotoUrl();
                        String str = personPhoto.toString();
                        UpdateUI(user);
                        HashMap<Object, String> hashMap = new HashMap<>();
                        hashMap.put("email", email);
                        hashMap.put("uid", Uid2);
                        hashMap.put("name", personName);
                        hashMap.put("phone", "");
                        hashMap.put("image", str);
                        hashMap.put("cover", "");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Users");
                        reference.child(Uid2).setValue(hashMap);
                    }
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
                    UpdateUI(null);
                }
            }


        });
    }
    private void UpdateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String email = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            Toast.makeText(Login.this, "Hello" + " " + personName, Toast.LENGTH_LONG).show();

        }



    }

    private void showWarning() {
        final AlertDialog.Builder warning = new AlertDialog.Builder(this)
                .setTitle("Are you sure ?")
                .setMessage("Linking Existing Account Will delete all the temp notes. Create New Account To Save them.")
                .setPositiveButton("Save Notes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),Register.class));
                        finish();
                    }
                }).setNegativeButton("Its Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        warning.show();
    }

}
