package com.hska.ebusiness.toolbar.util;

import android.database.Cursor;

import com.hska.ebusiness.toolbar.model.Credentials;

/**
 * Created by bettinakuhefuss on 30.05.16.
 */
/**
 * Class to Map a Cursor to a Credentials Object
 */
public class CredentialsMapper {

    /**
     * Creates a Credential object
     * @param cursor Database object
     * @return Credentials Object
     */
    public static Credentials map(final Cursor cursor) {
        Credentials credentials = new Credentials();
        credentials.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        credentials.setUserId(cursor.getLong(cursor.getColumnIndex("user_fk")));
        credentials.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        cursor.close();

        return credentials;
    }
}
