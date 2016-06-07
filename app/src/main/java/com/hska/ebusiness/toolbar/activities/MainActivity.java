<<<<<<< f547b90fc30579e5ae24b052ce541c1bcaccacac
package com.hska.ebusiness.toolbar.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.fragments.OfferListFragment;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;

import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.UserMapper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        Fragment offerListFragment = new OfferListFragment();
                 Bundle args = new Bundle();
                 args.putParcelableArrayList("offers", (ArrayList<? extends Parcelable>) getOfferByZip());
                 offerListFragment.setArguments(getIntent().getExtras());
                 offerListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, offerListFragment).commit();
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
}
=======
package com.hska.ebusiness.toolbar.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.fragments.FriendsFragment;
import com.hska.ebusiness.toolbar.fragments.HomeFragment;
import com.hska.ebusiness.toolbar.fragments.MessagesFragment;
import com.hska.ebusiness.toolbar.fragments.MyOffersFragment;

import sun.bob.mcalendarview.views.MonthExpFragment;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (id == R.id.action_offers) {
            Toast.makeText(getApplicationContext(), "Offers action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }


        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;  

            case 3:
                fragment = new MonthExpFragment();
                title = getString(R.string.title_offers);
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
<<<<<<< e82a5426d53e8ae8872d446fab012bf55376cd46
}
>>>>>>> Erweiterung NavBar
=======


}
>>>>>>> Implementierung Navbar
