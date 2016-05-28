package com.hska.ebusiness.toolbar.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.TextView;
import sun.bob.mcalendarview.MCalendarView;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;


import java.util.Date;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;

/**
 * Created by Sebastian on 16.05.2016.
 */
public class ShowOfferActivity extends AppCompatActivity {

    private Offer offer;
    private Rental rental;
    private Offer updatedOffer;
    private AlertDialog.Builder builder;
    private int firstDayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        //offer = getIntent().getParcelableExtra(TOOLBAR_OFFER);
        //updatedOffer = offer;

        offer = new Offer();
        offer.setName("Hammer");
        offer.setPrice(5);
        offer.setDescription("Ein Hammer für besondere Schrauben...HammiHam :)");



        rental = new Rental();
        rental.setRentFrom(new DateTime(2016, 5, 12, 23, 25));
        rental.setRentTo(new DateTime(2016, 5, 25, 23, 29));
        rental.setOffer(offer);

        initContent();
    }

    private void initContent() {
        ((TextView) findViewById(R.id.show_input_offer_name)).setText(offer.getName());
        ((TextView) findViewById(R.id.show_input_offer_price)).setText(String.valueOf(offer.getPrice()));
        ((TextView) findViewById(R.id.show_input_offer_description)).setText(offer.getDescription());

        MCalendarView calendarView = ((MCalendarView) findViewById(R.id.calendar));

        for(DateTime date : getRentalDays(rental.getRentFrom(), rental.getRentTo())){
            calendarView.markDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_show_offer, menu);
        return true;
    }
}
