package com.hska.ebusiness.toolbar.util;

import android.database.Cursor;

import com.hska.ebusiness.toolbar.model.User;

import static com.hska.ebusiness.toolbar.dao.DatabaseSchema.UserEntry;

/**
 * Class to Map a Cursor to a User Object
 */
public class UserMapper {

    /**
     * Creates a user from database
     * @param cursor Database object
     * @return User object
     */
    public static User map(final Cursor cursor) {
        User user = new User();

        user.setId(cursor.getLong(cursor.getColumnIndex(UserEntry._ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_EMAIL)));
        user.setStreet(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_STREET)));
        user.setZipCode(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_ZIP_CODE)));
        user.setCountry(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_COUNTRY)));
        user.setDescription(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_DESCRIPTION)));

        return user;
    }
}
