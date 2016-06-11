package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.fragments.EmptyFragment;
import com.hska.ebusiness.toolbar.fragments.OfferListFragment;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.OfferMapper;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    private User currentUser;
    private Fragment fragment = null;
    private NavigationView navigationView;
    private int backButtonCount = 0;

    /**
     * onCreate method
     *
     * @param savedInstanceState to restore activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(0);

        currentUser = ((ToolbarApplication) getApplication()).getCurrentUser();
        final View navigationHeader = navigationView.inflateHeaderView(R.layout.nav_header_main);
        final TextView userLabel = (TextView) navigationHeader.findViewById(R.id.label_username);
        userLabel.setText(getString(R.string.label_username, currentUser.getUsername()));

        fragment = new OfferListFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(ToolbarConstants.TOOLBAR_OFFER_LIST, (ArrayList<? extends Parcelable>) getOfferByZip(currentUser.getZipCode()));
        fragment.setArguments(arguments);
        setTitle(R.string.action_search);

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
        }

        final FloatingActionButton createOfferActionButton = (FloatingActionButton) findViewById(R.id.add_offer_floating_button);
        createOfferActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent createOfferIntent = new Intent(MainActivity.this, EditOfferActivity.class);
                createOfferIntent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
                startActivity(createOfferIntent);
            }
        });
    }

    /**
     * Handle navigation item selections
     *
     * @param item the selected item
     * @return true if success
     */
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.nav_search) {
            fragment = new OfferListFragment();
            final Bundle arguments = new Bundle();
            if (getOfferByZip(currentUser.getZipCode()) != null)
                arguments.putParcelableArrayList(ToolbarConstants.TOOLBAR_OFFER_LIST, (ArrayList<? extends Parcelable>) getOfferByZip(currentUser.getZipCode()));
            fragment.setArguments(arguments);
            setTitle(getString(R.string.action_search));
        } else if (id == R.id.nav_logout) {
            final Intent logoutIntent = new Intent(this, LoginActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            ((ToolbarApplication) getApplication()).setCurrentUser(null);
            startActivity(logoutIntent);
        } else {
            fragment = new EmptyFragment();
            setTitle(item.getTitle());
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Get all offers for a specific zip code
     *
     * @param zip the zip code
     * @return the list of offers for this zip code
     */
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

    /**
     * Handle back button press events
     */
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            backButtonCount = 0;
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press again to exit the application", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}