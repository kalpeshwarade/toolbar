package com.hska.ebusiness.toolbar.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hska.ebusiness.toolbar.R;

/**
 * Created by bwpc on 31.05.2016.
 */
public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        listView=(ListView) findViewById(R.id.drawerList);
    }
}
