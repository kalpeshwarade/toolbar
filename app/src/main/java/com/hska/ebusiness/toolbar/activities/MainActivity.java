package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.util.OfferMapper;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = ToolbarDBHelper.getInstance(this).findOfferById(1);
        // TODO: CursorOutOfBoundsException
        Log.d(this.getClass().getSimpleName(), cursor.getPosition() + "");
        Offer offer = OfferMapper.map(cursor);
        final Intent intent = new Intent(this, EditOfferActivity.class);
        intent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
        intent.putExtra(ToolbarConstants.TOOLBAR_OFFER, offer);
        startActivity(intent);
    }
}
