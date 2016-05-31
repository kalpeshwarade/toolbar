package com.hska.ebusiness.toolbar.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;

import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.tasks.InsertOfferTask;
import com.hska.ebusiness.toolbar.tasks.InsertRentalTask;
import com.hska.ebusiness.toolbar.util.UserMapper;

import org.joda.time.DateTime;

import java.util.Date;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.*;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        Cursor cursor = ToolbarDBHelper.getInstance(context).findUserByUsername("aaa");
        User user1 = UserMapper.map(cursor);

        //cursor = ToolbarDBHelper.getInstance(context).findUserByUsername("bbb");
        //User user2 = UserMapper.map(cursor);

        Offer offer = new Offer();
        offer.setName("Hammer");
        offer.setPrice(5);
        offer.setDescription("HamHam");
        offer.setZipCode("12345");
        offer.setValidFrom(DateTime.now().minusDays(2).getMillis());
        offer.setValidTo(DateTime.now().getMillis());
        offer.setLender_fk(user1.getId());

        final InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(offer);

        Rental rental1 = new Rental();
        rental1.setStatus(0);
        rental1.setOffer_fk(offer.getId());
        rental1.setRentFrom(DateTime.now().minusDays(5).getMillis());
        rental1.setRentTo(DateTime.now().getMillis());

        final InsertRentalTask insertRentalTask1 = new InsertRentalTask(this);
        insertRentalTask1.execute(rental1);

        Rental rental2 = new Rental();
        rental2.setStatus(1);
        rental2.setOffer_fk(offer.getId());
        rental2.setRentFrom(DateTime.now().minusMonths(1).minusDays(5).getMillis());
        rental2.setRentTo(DateTime.now().minusMonths(1).getMillis());

        final InsertRentalTask insertRentalTask2 = new InsertRentalTask(this);
        insertRentalTask2.execute(rental2);

        Offer offer2 = new Offer();
        offer2.setName("Hammer2");
        offer2.setPrice(5);
        offer2.setDescription("HamHam");
        offer2.setZipCode("12345");
        offer2.setValidFrom(DateTime.now().minusDays(2).getMillis());
        offer2.setValidTo(DateTime.now().getMillis());
        offer2.setLender_fk(user1.getId());


        Intent intent = new Intent(this, ShowOfferActivity.class);
        //intent.putExtra(TOOLBAR_OFFER_IS_EDIT_MODE, true);
        intent.putExtra(TOOLBAR_OFFER, offer);
        startActivity(intent);

        //final Intent intent = new Intent(this, ShowOfferActivity.class);
        //intent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
        //startActivity(intent);
    }
}
