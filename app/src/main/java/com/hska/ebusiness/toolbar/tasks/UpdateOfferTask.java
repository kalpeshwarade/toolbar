package com.hska.ebusiness.toolbar.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;

public class UpdateOfferTask extends AsyncTask<Offer, Void, Integer> {

    private ProgressDialog dialog;
    private Context context;
    private Offer offer;

    public UpdateOfferTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "", "Please wait...", true);
    }

    @Override
    protected Integer doInBackground(Offer... params) {
        offer = params[0];

        return ToolbarDBHelper.getInstance(context).updateOffer(offer);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}
