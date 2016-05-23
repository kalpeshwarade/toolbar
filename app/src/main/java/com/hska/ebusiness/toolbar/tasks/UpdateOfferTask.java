package com.hska.ebusiness.toolbar.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;

public class UpdateOfferTask extends AsyncTask<Offer, Void, Integer> {

    private Context context;
    private Offer offer;

    public UpdateOfferTask(final Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        ProgressDialog.show(context, "", "Please wait...", true);
    }

    @Override
    protected Integer doInBackground(final Offer... params) {
        offer = params[0];

        return ToolbarDBHelper.getInstance(context).updateOffer(offer);
    }

    @Override
    protected void onPostExecute(final Integer integer) {
        super.onPostExecute(integer);
    }
}