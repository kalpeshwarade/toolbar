package com.hska.ebusiness.toolbar.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;
import android.util.Log;

public class InsertOfferTask extends AsyncTask<Offer, Void, Integer> {

    private Context context;
    private Offer offer;
    private static String TAG = InsertOfferTask.class.getSimpleName();

    public InsertOfferTask(final Context context) {
        this.context = context;
    }
    private ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(final Offer... params) {
        offer = params[0];
        return ((int) ToolbarDBHelper.getInstance(context).insertOffer(offer, ToolbarDBHelper.getInstance(context).getWritableDatabase()));
    }

    @Override
    protected void onPostExecute(final Integer integer) {
        super.onPostExecute(integer);

        Log.d(TAG, "Post Execute");
        return;
    }
}

