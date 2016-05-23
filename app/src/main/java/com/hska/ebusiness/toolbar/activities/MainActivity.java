package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, EditOfferActivity.class);
        intent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
        startActivity(intent);
    }
}
