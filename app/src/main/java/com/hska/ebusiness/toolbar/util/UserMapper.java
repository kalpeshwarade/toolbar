package com.hska.ebusiness.toolbar.util;

import android.database.Cursor;

import com.hska.ebusiness.toolbar.model.User;

/**
 * Created by bettinakuhefuss on 30.05.16.
 */

/**
 * Class to Map a Cursor to a User Object
 */
public class UserMapper {

    /**
     * Creates an User
     * @param cursor Database object
     * @return User Object
     */
    public static User map(final Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
        user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        user.setStreet(cursor.getString(cursor.getColumnIndex("street")));
        user.setZipCode(cursor.getString(cursor.getColumnIndex("zip_code")));
        user.setCountry(cursor.getString(cursor.getColumnIndex("country")));
        user.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        //cursor.close();

        return user;
    }
}
