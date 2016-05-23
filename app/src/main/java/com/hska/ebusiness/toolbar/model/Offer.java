package com.hska.ebusiness.toolbar.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

public class Offer implements Parcelable {

    public Offer() {

    }

    public Offer(final String name, final String description, final String zipCode,
                 final long price, final DateTime validFrom, final DateTime validTo) {
        this.name = name;
        this.description = description;
        this.zipCode = zipCode;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    private long id;
    private String name;
    private Uri image;
    private String description;
    private String zipCode;
    private long price;
    private DateTime validFrom;
    private DateTime validTo;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(final Uri image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(final long price) {
        this.price = price;
    }

    public DateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(final DateTime validFrom) {
        this.validFrom = validFrom;
    }

    public DateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(final DateTime validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", name=" + name +
                ", image=" + image +
                ", description='" + description + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", price=" + price +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel destination, final int flags) {
        destination.writeLong(id);
        destination.writeString(name);
        if(image != null)
            destination.writeString(image.getPath());
        destination.writeString(description);
        destination.writeString(zipCode);
        destination.writeLong(price);
        destination.writeLong(validFrom.getMillis());
        destination.writeLong(validTo.getMillis());
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(final Parcel source) {
            return new Offer(source);
        }

        @Override
        public Offer[] newArray(final int size) {
            return new Offer[0];
        }
    };

    private Offer(final Parcel source) {
        id = source.readLong();
        name = source.readString();
        image = Uri.parse(source.readString());
        description = source.readString();
        zipCode = source.readString();
        price = source.readLong();
        validFrom = new DateTime(source.readLong());
        validTo = new DateTime(source.readLong());
    }
}
