package net.myapp.Organize4U.note;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import net.myapp.Organize4U.R;

public class Main3Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

