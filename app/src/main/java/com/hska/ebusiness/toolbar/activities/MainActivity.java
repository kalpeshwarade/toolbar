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
