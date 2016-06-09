package com.hska.ebusiness.toolbar.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.fragments.MyOffersFragment;
import com.hska.ebusiness.toolbar.fragments.OfferListFragment;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.OfferMapper;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    private User currentUser;
    private Fragment fragment = null;

    /**
     * @param savedInstanceState to restore activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentUser = ((ToolbarApplication) getApplication()).getCurrentUser();

        fragment = new OfferListFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("offers", (ArrayList<? extends Parcelable>) getOfferByZip(currentUser.getZipCode()));
        fragment.setArguments(arguments);
        setTitle(getString(R.string.title_search));

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            fragment = new OfferListFragment();
            final Bundle arguments = new Bundle();
            if(getOfferByZip(currentUser.getZipCode()) != null)
                arguments.putParcelableArrayList("offers", (ArrayList<? extends Parcelable>) getOfferByZip(currentUser.getZipCode()));
            fragment.setArguments(arguments);
            setTitle(getString(R.string.title_search));

        } else if (id == R.id.nav_my_offers) {
            fragment = new MyOffersFragment();
            setTitle(getString(R.string.title_offers));
        } else if (id == R.id.nav_my_requests) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_logout) {

        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
        }

        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Offer> getOfferByZip(final String zip) {
        Log.d(TAG, "getOfferByZip: " + zip);
        final Cursor cursor = ToolbarDBHelper.getInstance(this).findOfferByZIP(zip);
        if (zip != null && cursor != null && cursor.moveToFirst()) {
            final ArrayList<Offer> offerList = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Offer offer = OfferMapper.map(cursor);
                offer.setLender(currentUser.getId());
                offerList.add(offer);
            }
            cursor.close();
            return offerList;
        }

        if (cursor != null)
            cursor.close();
        return null;
    }
}