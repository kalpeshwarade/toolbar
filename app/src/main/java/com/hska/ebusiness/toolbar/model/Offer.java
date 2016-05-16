package com.hska.ebusiness.toolbar.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

public class Offer implements Parcelable {

    public Offer() {

    }

    public Offer(String description, String zipCode, long price, DateTime validFrom, DateTime validTo) {
        this.description = description;
        this.zipCode = zipCode;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    private long id;
    private Uri image;
    private String description;
    private String zipCode;
    private long price;
    private DateTime validFrom;
    private DateTime validTo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public DateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(DateTime validFrom) {
        this.validFrom = validFrom;
    }

    public DateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(DateTime validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
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
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeLong(id);
        destination.writeString(image.getPath());
        destination.writeString(description);
        destination.writeString(zipCode);
        destination.writeLong(price);
        destination.writeLong(validFrom.getMillis());
        destination.writeLong(validTo.getMillis());
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel source) {
            return new Offer(source);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[0];
        }
    };

    private Offer(Parcel source) {
        id = source.readLong();
        image = Uri.parse(source.readString());
        description = source.readString();
        zipCode = source.readString();
        price = source.readLong();
        validFrom = new DateTime(source.readLong());
        validTo = new DateTime(source.readLong());
    }
}
