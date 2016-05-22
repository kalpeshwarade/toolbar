package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.tasks.InsertOfferTask;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

import org.joda.time.DateTime;

public class MainActivity extends AppCompatActivity {

    private final String LOG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Code zum Testen der Detailansicht eines Angebots
        Offer offer = new Offer("Testname", "Description", "12345", 5, DateTime.now(), DateTime.now());

        Log.d(LOG, "insert");
        InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(offer);
        Log.d(LOG, "insert done");

        Intent intent = new Intent(this, EditOfferActivity.class);
        intent.putExtra(ToolbarConstants.TOOLBAR_OFFER, offer);
        startActivity(intent);
        // Test Ende ;)
    }
}
