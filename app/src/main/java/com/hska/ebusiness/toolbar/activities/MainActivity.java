package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.hska.ebusiness.toolbar.model.Offer;
import org.joda.time.DateTime;

import com.hska.ebusiness.toolbar.util.ToolbarConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, EditOfferActivity.class);
        Offer offer = new Offer("Testname", "Description", "12345", 5, DateTime.now(), DateTime.now());
        intent.putExtra( ToolbarConstants.TOOLBAR_OFFER, offer );
        startActivity(intent);

    }
}
