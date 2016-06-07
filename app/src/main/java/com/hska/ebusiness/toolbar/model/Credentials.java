package com.hska.ebusiness.toolbar.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;

import com.hska.ebusiness.toolbar.dao.DatabaseSchema;

/*
Class for the Credentials Entity
 */
public class Credentials implements Parcelable {

    /*
    Standard Constructor
     */
    public Credentials() {
        super();
    }

    /**
     * Constructor
     */
    public Credentials(String password, long userId) {
        this.password = password;
        this.userId = userId;
    }

    // id of the Credentials
    private long id;

    // Password
    private String password;

    // ID of the User
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                ", id=" + id +
                ", userId=" + userId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel destination, final int flags) {
        destination.writeLong(id);
        // HASH PASSWORD HERE
        destination.writeString(password);
        destination.writeLong(userId);
    }

    public static final Parcelable.Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(final Parcel parcel) {
            return new Credentials(parcel);
        }

        @Override
        public Credentials[] newArray(final int size) {
            return new Credentials[0];
        }
    };

    /*
    Constructor for the database transaction
     */
    private Credentials(final Parcel source) {
        id = source.readLong();
        password = source.readString();
        userId = source.readLong();
    }
}
