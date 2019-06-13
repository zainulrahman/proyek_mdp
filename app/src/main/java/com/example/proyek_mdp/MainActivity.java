package com.example.proyek_mdp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rview;
    private BeritaAdapter adapterBerita;

    private ArrayList<Berita> listBerita;

    private BottomNavigationView navBar;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ProfilFragment profilFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rview = findViewById(R.id.rview);
        navBar =  findViewById(R.id.navBar);
        mMainFrame = findViewById(R.id.mainFrame);
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        profilFragment = new ProfilFragment();
        setFragment(homeFragment,"");
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home :
                        setFragment(homeFragment,"");
                        setTitle("Liputan 9");
                        return true;
                    case R.id.nav_detail :
                        setFragment(searchFragment,"");
                        setTitle("Search");
                        return true;
                    case R.id.nav_score :
                        SharedPreferences sp1 = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                        String id_user = sp1.getString("currentUser",null);
                        if (id_user!=null){
                            setFragment(profilFragment,"");
                            setTitle("My Profile");
                        }else{
                            navBar.setSelectedItemId(R.id.nav_home);
                            Toast.makeText(MainActivity.this, "Harus Login Dulu!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this,LoginForm.class);
                            startActivity(i);
                        }
                        return true;
                    default:
                        setFragment(homeFragment,"");
                        return false;
                }
            }
        });

    }

    public void setFragment(Fragment fragment,String message){

        if (!message.equals("")){
            Bundle bundle  = new Bundle();

            bundle.putString("message",message);
            fragment.setArguments(bundle);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }
}
