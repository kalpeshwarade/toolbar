package com.hska.ebusiness.toolbar.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Rental;

public class InsertRentalTask extends AsyncTask<Rental, Void, Integer> {

    private Context context;
    private Rental rental;

    public InsertRentalTask(final Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(final Rental... params) {
        rental = params[0];
        return ((int) ToolbarDBHelper.getInstance(context).insertRental(rental, ToolbarDBHelper.getInstance(context).getWritableDatabase()));
    }

    @Override
    protected void onPostExecute(final Integer integer) {
        super.onPostExecute(integer);
    }
}