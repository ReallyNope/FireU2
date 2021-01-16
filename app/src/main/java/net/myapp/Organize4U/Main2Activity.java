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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.myapp.Organize4U.auth.Profile;
import net.myapp.Organize4U.auth.Register;
import net.myapp.Organize4U.note.AddNote;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle2;
    NavigationView nav_view2;
    TimePicker myTimePicker;
    TextView buttonstartSetDialog,buttonCancel;
    TextView textAlarmPrompt;
    TimePickerDialog timePickerDialog;
    FirebaseUser user;
    FirebaseAuth fAuth;

    final static int RQS_1 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        drawerLayout = findViewById(R.id.drawer2);
        nav_view2 = findViewById(R.id.nav_view2);
        toggle2 = new ActionBarDrawerToggle(this, drawerLayout, toolbar2, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle2);
        toggle2.setDrawerIndicatorEnabled(true);
        toggle2.syncState();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        nav_view2.setNavigationItemSelectedListener(this);
        textAlarmPrompt = (TextView) findViewById(R.id.alarmprompt);

        buttonstartSetDialog = (TextView) findViewById(R.id.startAlarm);
        buttonstartSetDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textAlarmPrompt.setText("");
                openTimePickerDialog(false);

            }
        });
   /*     buttonCancel = (TextView) findViewById(R.id.cancelAlarm);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelAlarm();

            }

            private void cancelAlarm(Context k1) {
                Intent intent = new Intent(k1, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(k1, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) k1.getSystemService(ALARM_SERVICE);
                am.cancel(pendingIntent);
            }
       });
       */

    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(Main2Activity.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {

                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {

        textAlarmPrompt.setText("\n\n***\n" + "Alarm is set "
                + targetCal.getTime() + "\n" + "***\n");

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.alarm:
                startActivity(new Intent(this,Main2Activity.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;
            case R.id.calendar:
                buttonstartSetDialog.setVisibility(View.GONE);
                textAlarmPrompt.setVisibility(View.GONE);
                Fragment m4Fragment;
                m4Fragment = new net.myapp.Organize4U.note.Calendar();
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                fragmentManager3.beginTransaction()
                        .replace(R.id.content, m4Fragment).commit();
                break;
            case R.id.Events:
                buttonstartSetDialog.setVisibility(View.GONE);
                textAlarmPrompt.setVisibility(View.GONE);
                Fragment m5Fragment;
                m5Fragment = new net.myapp.Organize4U.Event();
                FragmentManager fragmentManager5 = getSupportFragmentManager();
                fragmentManager5.beginTransaction()
                        .replace(R.id.content, m5Fragment).commit();
                break;
            case R.id.logout:
                checkUser();
                break;
            default:
                Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }}

