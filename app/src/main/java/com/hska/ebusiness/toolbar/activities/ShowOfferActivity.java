package com.hska.ebusiness.toolbar.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sun.bob.mcalendarview.MCalendarView;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.dao.ToolbarDBHelper;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.model.Rental;
import com.hska.ebusiness.toolbar.model.User;
import com.hska.ebusiness.toolbar.util.ToolbarApplication;
import com.hska.ebusiness.toolbar.util.RentalMapper;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.w3c.dom.Text;

import android.net.Uri;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE;

/**
 * Created by Sebastian on 16.05.2016.
 */
public class ShowOfferActivity extends AppCompatActivity {

    private Offer offer;
    private User user;

    private ImageView offerImage;
    private TextView offerName;
    private TextView offerDescription;
    private TextView offerFrom;
    private TextView offerTo;
    private TextView offerZipCode;
    private TextView offerPrice;

    private final String TAG = this.getClass().getSimpleName();

    /**
     * Used to initialize the layout and field of the Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);
        offer = getIntent().getParcelableExtra(TOOLBAR_OFFER);
        user = ((ToolbarApplication) getApplication()).getCurrentUser();

        offerImage = (ImageView) findViewById(R.id.show_image_offer_image);
        offerName = (TextView) findViewById(R.id.show_input_offer_name);
        offerDescription = (TextView) findViewById(R.id.show_input_offer_description);
        offerFrom = (TextView) findViewById(R.id.show_input_offer_from);
        offerTo = (TextView) findViewById(R.id.show_input_offer_to);
        offerZipCode = (TextView) findViewById(R.id.show_input_zip_code);
        offerPrice = (TextView) findViewById(R.id.show_input_offer_price);

        Log.d(TAG, offer.toString());

        initContent();
    }

    /**
     * Initialize content if edit mode is activated.
     * Pre-fill fields with order values.
     * Create placeholder for image if there is none.
     */
    private void initContent() {
        if(offer.getName() != null)
            offerName.setText(offer.getName());
        if(offer.getPrice() > 0)
            offerPrice.setText(String.valueOf(offer.getPrice()));
        if(offer.getDescription() != null)
            offerDescription.setText(offer.getDescription());
        if(offer.getZipCode() != null)
            offerZipCode.setText(offer.getZipCode());

        offerFrom.setText(new DateTime(offer.getValidFrom()).toLocalDate().toString());
        offerTo.setText(new DateTime(offer.getValidTo()).toLocalDate().toString());


        if(offer.getImage() != null) {
            final Uri image = Uri.parse(offer.getImage());
            if (image != null && new File(image.getPath()).exists()) {
                try {
                    offerImage.setImageBitmap(this.resizeImage(image));
                } catch (IOException e) {
                    Log.e(TAG, "Error while initializing image: " + e.getMessage());
                    return;
                }
            }
        } else {
            offerImage.setImageResource(R.drawable.ic_insert_photo_black_48dp);
        }


        MCalendarView calendarView = ((MCalendarView) findViewById(R.id.calendar));

        if(getRentals(offer.getId()) != null) {
            for (Rental rental : getRentals(offer.getId())) {
                if (rental.getStatus() == 1) {
                    DateTime fromDate = new DateTime(rental.getRentFrom());
                    DateTime toDate = new DateTime(rental.getRentTo());

                    for (DateTime date : getRentalDays(fromDate, toDate)) {
                        calendarView.markDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                    }
                } else
                    continue;
            }
        }

    }

    /**
     * Returns all rental Days from all rentals to mark them in the calendar
     * @param fromDate
     * @param toDate
     * @return
     */
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

                Intent intent = new Intent(this, EditOfferActivity.class);
                intent.putExtra(TOOLBAR_OFFER_IS_EDIT_MODE, true);
                intent.putExtra(TOOLBAR_OFFER, offer);
                startActivity(intent);

                return true;

            case R.id.offer_show_delete:
                if(user.getId() == offer.getLender_fk()) {
                    ToolbarDBHelper.getInstance(this).deleteOffer(offer);
                    Intent menuIntent = new Intent(this, MainActivity.class);
                    startActivity(menuIntent);
                }
                else
                    Toast.makeText(ShowOfferActivity.this, "Falscher User!", Toast.LENGTH_LONG).show();

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

    /**
     * Returns a List with all Rentals of an offer
     * @param id
     * @return
     */
    public ArrayList<Rental> getRentals(long id){
        Cursor cursor = ToolbarDBHelper.getInstance(this).findAllRentalsToOffer(id);
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

    /**
     * Resizes image to fit in ImageView
     *
     * @param file Uri to the image path
     * @return the resized bitmap
     * @throws IOException if access to external storage fails
     */
    private Bitmap resizeImage(final Uri file) throws IOException {
        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);

        final float width = bitmap.getWidth();
        final float height = bitmap.getHeight();

        final int aimedHeight = 1024;
        final int aimedWidth = (int) (width / height * (float) aimedHeight);

        return Bitmap.createScaledBitmap(bitmap, aimedWidth, aimedHeight, false);
    }

}
