package com.hska.ebusiness.toolbar.util;

import android.database.Cursor;
import android.util.Log;

import com.hska.ebusiness.toolbar.dao.DatabaseSchema;
import com.hska.ebusiness.toolbar.model.Rental;

/**
 * Created by Sebastian on 30.05.2016.
 */
public class RentalMapper {

    public static Rental map(final Cursor cursor) {
        Rental rental = new Rental();

        Log.d(RentalMapper.class.getSimpleName(), "Rental: " + String.valueOf(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_OFFER_FK)));

        rental.setId(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.RentalEntry._ID)));
        rental.setRentFrom(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_FROM)));
        rental.setRentTo(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_TO)));
        rental.setStatus(cursor.getInt(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_STATUS)));
        rental.setOffer(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_OFFER_FK)));
        rental.setHirer(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_HIRER_FK))));
        rental.setLender(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.RentalEntry.COLUMN_NAME_LENDER_FK)));

        //cursor.close();
        return rental;
    }
}
