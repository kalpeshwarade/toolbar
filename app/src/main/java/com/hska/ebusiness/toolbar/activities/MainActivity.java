package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.tasks.InsertOfferTask;

import org.joda.time.DateTime;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Offer offer = new Offer("Offer", "desc", "12345", 123, DateTime.now().getMillis(), DateTime.now().getMillis());

        final InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(offer);

        final Intent intent = new Intent(this, EditOfferActivity.class);
        intent.putExtra(TOOLBAR_OFFER_IS_EDIT_MODE, true);
        intent.putExtra(TOOLBAR_OFFER, offer);
        startActivity(intent);

        //final Intent intent = new Intent(this, ShowOfferActivity.class);
        //intent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
        //startActivity(intent);
    }
}
