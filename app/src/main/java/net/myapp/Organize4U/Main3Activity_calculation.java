package net.myapp.Organize4U;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.myapp.Organize4U.Frags_prof.Chat;

import net.myapp.Organize4U.auth.Profile;
import net.myapp.Organize4U.auth.Register;


public class Main3Activity_calculation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private  ActionBarDrawerToggle toggle3;
    private  NavigationView nav_view3;
    FirebaseUser user;
    FirebaseAuth fAuth;

    DatabaseReference userRef;
    FirebaseAuth mAuth;
    String currentUserId;
    private String calledBy="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);
        mAuth=FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        drawerLayout = findViewById(R.id.drawer3);
        nav_view3 = findViewById(R.id.nav_view3);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        nav_view3 = findViewById(R.id.nav_view3);

        toggle3 = new ActionBarDrawerToggle(this, drawerLayout, toolbar3, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle3);
        toggle3.setDrawerIndicatorEnabled(true);
        toggle3.syncState();
        nav_view3.setNavigationItemSelectedListener(this);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    private void displayAlert() {
        AlertDialog.Builder warning = new AlertDialog.Builder(this)
                .setTitle("Are you sure ?")
                .setMessage("You are logged in with Temporary Account. Logging out will Delete All the notes.")
                .setPositiveButton("Sync Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        finish();
                    }
                }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),Splash.class));
                                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                            }
                        });
                    }
                });

        warning.show();
    }
    private void checkUser() {
        // if user is real or not
        if(user.isAnonymous()){
            displayAlert();
        }else {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Splash.class));
            overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(this, Main3Activity_calculation.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.alarm){
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.notes){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.profiles:
                Fragment mFragment;
                mFragment = new Profile();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content, mFragment).commit();
                break;
            case R.id.users:

                Fragment m2Fragment;
                m2Fragment = new users();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.content, m2Fragment).commit();
                break;
            case R.id.logout:
                checkUser();
                break;
            default:
                Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public void onStart() {
        super.onStart();
        checkForReceivingCall();
    }

    private void checkForReceivingCall() {
        userRef.child(currentUserId)
                .child("Ringing")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("ringing")){
                            calledBy = dataSnapshot.child("ringing").getValue().toString();

                            Intent callingIntent = new Intent(Main3Activity_calculation.this, CallingActivity.class);
                            callingIntent.putExtra("visit_user_id", calledBy);
                            startActivity(callingIntent);
                            finish();
                        }}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});
    }
}

