package com.hska.ebusiness.toolbar.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.fragments.AccountFragment;
import com.hska.ebusiness.toolbar.fragments.LogoutFragment;
import com.hska.ebusiness.toolbar.fragments.MyRequestsFragment;
import com.hska.ebusiness.toolbar.fragments.OfferListFragment;
import com.hska.ebusiness.toolbar.fragments.ProfilFragment;
import com.hska.ebusiness.toolbar.fragments.SearchFragment;
import com.hska.ebusiness.toolbar.model.Credentials;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;

import com.hska.ebusiness.toolbar.tasks.InsertOfferTask;
import com.hska.ebusiness.toolbar.tasks.InsertRentalTask;

import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.OfferMapper;
import com.hska.ebusiness.toolbar.util.UserMapper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private Context context;
    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    /**
     *
     * @param savedInstanceState to restore activity state
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Log.d(TAG, "displayView");
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                Log.d(TAG, "case0");
                fragment = new SearchFragment();
                title = getString(R.string.title_search);
                break;

            case 1:
                Log.d(TAG, "case1");

                fragment = new OfferListFragment();

                Bundle args = new Bundle();
                args.putParcelableArrayList("offers", (ArrayList<? extends Parcelable>) getOffersByZip("12345"));
                fragment.setArguments(getIntent().getExtras());
                fragment.setArguments(args);
                title = getString(R.string.title_offers);
                break;

            case 2:
                Log.d(TAG, "case2");

                fragment = new MyRequestsFragment();
                title = getString(R.string.title_requests);
                break;

            case 3:
                Log.d(TAG, "case3");

                fragment = new ProfilFragment();
                title = getString(R.string.title_profile);
                break;

            case 4:
                Log.d(TAG, "case4");

                fragment = new AccountFragment();
                title = getString(R.string.title_account);
                break;

            case 5:
                Log.d(TAG, "case5");

                fragment = new LogoutFragment();
                title = getString(R.string.title_logout);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            Log.d(TAG, "fragmentCheck");


            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public List<Offer> getOffersByZip(String zip){
        Cursor cursor = ToolbarDBHelper.getInstance(this).findOfferByZIP(zip);
        if(cursor != null && cursor.moveToFirst()) {
            ArrayList <Offer> offerList = new ArrayList<Offer>();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                Log.d(TAG, "Offers -----------------------");
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