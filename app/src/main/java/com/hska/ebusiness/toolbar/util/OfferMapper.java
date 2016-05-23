package com.hska.ebusiness.toolbar.util;

import android.database.Cursor;
import android.net.Uri;

import com.hska.ebusiness.toolbar.dao.DatabaseSchema;
import com.hska.ebusiness.toolbar.model.Offer;

import org.joda.time.DateTime;

public class OfferMapper {

    public static Offer map(final Cursor cursor) {
        final Offer offer = new Offer();
        offer.setId(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.OfferEntry._ID)));
        offer.setName(cursor.getString(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_NAME)));
        offer.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_DESCRIPTION)));
        offer.setImage(Uri.parse(cursor.getString(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_IMAGE))));
        offer.setZipCode(cursor.getString(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_ZIP_CODE)));
        offer.setPrice(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_PRICE))));
        offer.setValidFrom(new DateTime(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_VALID_FROM))));
        offer.setValidTo(new DateTime(cursor.getLong(cursor.getColumnIndex(DatabaseSchema.OfferEntry.COLUMN_NAME_VALID_TO))));

        return offer;
    }

}
