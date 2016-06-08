package com.hska.ebusiness.toolbar.activities;

import android.content.Context;
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
import com.hska.ebusiness.toolbar.fragments.LogoutFragment;
import com.hska.ebusiness.toolbar.fragments.MyRequestsFragment;
import com.hska.ebusiness.toolbar.fragments.OfferListFragment;
import com.hska.ebusiness.toolbar.fragments.ProfilFragment;
import com.hska.ebusiness.toolbar.fragments.SearchFragment;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;
import com.hska.ebusiness.toolbar.util.UserMapper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private Context context;
    private final String TAG = this.getClass().getSimpleName();
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

    private List<Offer> getOfferByZip(){

        Cursor cursor = ToolbarDBHelper.getInstance(context).findUserByUsername("aaa");
        User user1 = UserMapper.map(cursor);

        Offer offer = new Offer();
        offer.setName("Hammer");
        offer.setImage(null);
        offer.setDescription("HamHam");
        offer.setPrice(5);
        offer.setZipCode("12345");
        offer.setValidFrom(DateTime.now().minusDays(2).getMillis());
        offer.setValidTo(DateTime.now().getMillis());
        offer.setLender_fk(user1.getId());

        Rental rental1 = new Rental();
        rental1.setStatus(0);
        rental1.setOffer(offer.getId());
        rental1.setRentFrom(DateTime.now().minusDays(5).getMillis());
        rental1.setRentTo(DateTime.now().getMillis());

        Rental rental2 = new Rental();
        rental2.setStatus(1);
        rental2.setOffer(offer.getId());
        rental2.setRentFrom(DateTime.now().minusMonths(1).minusDays(5).getMillis());
        rental2.setRentTo(DateTime.now().minusMonths(1).getMillis());

        Offer offer2 = new Offer();
        offer2.setImage(null);
        offer2.setName("Hammer2");
        offer2.setPrice(5);
        offer2.setDescription("HamHam");
        offer2.setZipCode("12345");
        offer2.setValidFrom(DateTime.now().minusDays(2).getMillis());
        offer2.setValidTo(DateTime.now().getMillis());
        offer2.setLender_fk(user1.getId());

        /*
        final InsertRentalTask insertRentalTask1 = new InsertRentalTask(this);
        insertRentalTask1.execute(rental1, rental2);

        final InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(offer, offer2);
        */

        List<Offer> offerList = new ArrayList<Offer>();
        offerList.add(offer);
        offerList.add(offer2);

        return offerList;
    }

    //----------------------------------------


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new SearchFragment();
                title = getString(R.string.title_search);
                break;

            case 1:
                fragment = new OfferListFragment();

                Bundle args = new Bundle();
                args.putParcelableArrayList("offers", (ArrayList<? extends Parcelable>) getOfferByZip());
                fragment.setArguments(getIntent().getExtras());
                fragment.setArguments(args);
                title = getString(R.string.title_offers);
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

            /**
             * Set ActionBar title
             */
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle(title);
        }
    }
}