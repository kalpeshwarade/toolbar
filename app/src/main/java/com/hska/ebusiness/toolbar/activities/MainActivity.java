package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.fragments.AccountFragment;
import com.hska.ebusiness.toolbar.fragments.MyRequestsFragment;
import com.hska.ebusiness.toolbar.fragments.OfferListFragment;
import com.hska.ebusiness.toolbar.fragments.ProfilFragment;
import com.hska.ebusiness.toolbar.fragments.SearchFragment;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.OfferMapper;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private final String TAG = this.getClass().getSimpleName();

    private User currentUser;

    /**
     * @param savedInstanceState to restore activity state
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ((ToolbarApplication) getApplication()).getCurrentUser();
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        final FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);

    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(final int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new OfferListFragment();
                final Bundle args = new Bundle();
                args.putParcelableArrayList("offers", (ArrayList<? extends Parcelable>) getOfferByZip(currentUser.getZipCode()));
                fragment.setArguments(getIntent().getExtras());
                fragment.setArguments(args);
                title = getString(R.string.title_offers);
                break;

            case 1:
                fragment = new SearchFragment();
                title = getString(R.string.title_search);
                break;

            case 2:
                fragment = new MyRequestsFragment();
                Intent intent = new Intent(this, EditOfferActivity.class);
                intent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
                startActivity(intent);
                title = getString(R.string.title_requests);
                break;

            case 3:
                fragment = new ProfilFragment();
                title = getString(R.string.title_profile);
                break;

            case 4:
                fragment = new AccountFragment();
                title = getString(R.string.title_account);
                break;

            case 5:
                final ToolbarApplication application = (ToolbarApplication) getApplication();
                application.setCurrentUser(null);

                final Intent logoutIntent = new Intent(this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            /**
             * Set ActionBar title
             */
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(title);
        }
    }

    private List<Offer> getOfferByZip(final String zip) {
        final Cursor cursor = ToolbarDBHelper.getInstance(this).findOfferByZIP(zip);
        if (zip != null && cursor != null && cursor.moveToFirst()) {
            final ArrayList<Offer> offerList = new ArrayList<>();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                offerList.add(OfferMapper.map(cursor));
            }
            cursor.close();
            return offerList;
        }

        if(cursor != null)
            cursor.close();
        return null;
    }
}