package net.myapp.Organize4U;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import net.myapp.Organize4U.note.AddNote;

import java.util.Calendar;

public class Main4Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle2;
    NavigationView nav_view2;


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


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

