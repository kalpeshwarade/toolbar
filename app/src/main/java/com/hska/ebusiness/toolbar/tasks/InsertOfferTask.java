package com.hska.ebusiness.toolbar.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;

public class InsertOfferTask extends AsyncTask<Offer, Void, Integer> {

    private Context context;
    private Offer offer;

    public InsertOfferTask(final Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(final Offer... params) {
        offer = params[0];
        final ToolbarDBHelper dbHelper = ToolbarDBHelper.getInstance(context);
        return (int) dbHelper.insertOffer(offer, dbHelper.getWritableDatabase());
    }

    @Override
    protected void onPostExecute(final Integer integer) {
        super.onPostExecute(integer);
    }
}

