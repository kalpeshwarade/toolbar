package com.hska.ebusiness.toolbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public User() {

    }

    public User(final String username, final String email, final String street, final String zipCode, final String country, final String description) {
        this.username = username;
        this.email = email;
        this.street = street;
        this.zipCode = zipCode;
        this.country = country;
        this.description = description;
    }

    private long id;

    private String username;

    private String email;

    private String street;

    private String zipCode;

    private String country;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel destination, final int flags) {
        destination.writeLong(id);
        destination.writeString(username);
        destination.writeString(email);
        destination.writeString(street);
        destination.writeString(zipCode);
        destination.writeString(country);
        destination.writeString(description);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(final Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(final int size) {
            return new User[size];
        }
    };


    private User(final Parcel source) {
        id = source.readLong();
        username = source.readString();
        email = source.readString();
        street = source.readString();
        zipCode = source.readString();
        country = source.readString();
        description = source.readString();
    }
}