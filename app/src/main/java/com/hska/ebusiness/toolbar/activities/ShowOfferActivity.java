package com.hska.ebusiness.toolbar.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import sun.bob.mcalendarview.MCalendarView;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.DatabaseSchema;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;
import com.hska.ebusiness.toolbar.tasks.InsertRentalTask;
import com.hska.ebusiness.toolbar.util.RentalMapper;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;


import java.util.Date;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE;

/**
 * Created by Sebastian on 16.05.2016.
 */
public class ShowOfferActivity extends AppCompatActivity {

    private Offer offer;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);
        context = getApplicationContext();
        offer = getIntent().getParcelableExtra(TOOLBAR_OFFER);

        initContent();
    }

    private void initContent() {
        ((TextView) findViewById(R.id.show_input_offer_name)).setText(offer.getName());
        ((TextView) findViewById(R.id.show_input_offer_price)).setText(String.valueOf(offer.getPrice()));
        ((TextView) findViewById(R.id.show_input_offer_description)).setText(offer.getDescription());

        MCalendarView calendarView = ((MCalendarView) findViewById(R.id.calendar));

        for(Rental rental : getRentals(offer.getId())){

            if(rental.getStatus() == 1){

                DateTime fromDate = new DateTime(rental.getRentFrom());
                DateTime toDate = new DateTime(rental.getRentTo());

                for(DateTime date : getRentalDays(fromDate, toDate)) {
                    calendarView.markDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                }
            }
            else
                continue;
        }

    }

    public List<DateTime> getRentalDays(DateTime fromDate, DateTime toDate){
        List<DateTime> daysList = new ArrayList<DateTime>();
        int days = Days.daysBetween(fromDate, toDate).getDays();

        for (DateTime date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            daysList.add(date);
        }
        return daysList;
    }


    /**
     * Gets called when item from menu gets selected
     *
     * @param item the selected menu item
     * @return true if event was handled successfully
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.offer_show_edit:

                Toast.makeText(getApplicationContext(), String.valueOf(getRentals(offer.getId()).size()),
                        Toast.LENGTH_LONG).show();

                final Intent mainIntentSave = new Intent(this, EditOfferActivity.class);
                mainIntentSave.putExtra(TOOLBAR_OFFER, offer);
                mainIntentSave.putExtra(TOOLBAR_OFFER_IS_EDIT_MODE, true);
                startActivity(mainIntentSave);
                return true;

            case R.id.offer_show_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_show_offer, menu);
        return true;
    }

    public ArrayList<Rental> getRentals(long id){
        Cursor cursor = ToolbarDBHelper.getInstance(context).findAllRentalsToOffer(id);
        if(cursor != null && cursor.moveToFirst()) {
            ArrayList <Rental> rentalList = new ArrayList<Rental>();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                       rentalList.add(RentalMapper.map(cursor));
                }
            cursor.close();
            return rentalList;
        }
        cursor.close();
        return null;
    }

}
