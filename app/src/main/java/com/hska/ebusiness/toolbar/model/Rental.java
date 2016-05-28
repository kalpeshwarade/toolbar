package com.hska.ebusiness.toolbar.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

public class Rental implements Parcelable {

    public Rental() {

    }

    public Rental(final DateTime rentFrom, final DateTime rentTo, Integer status, Offer offer, User lender, User hirer) {
        this.rentFrom = rentFrom;
        this.rentTo = rentTo;
        this.status = status;
        this.offer = offer;
        this.lender = lender;
        this.hirer = hirer;
    }

    private Long id;
    private DateTime rentFrom;
    private DateTime rentTo;
    private Integer status;
    private Offer offer;
    private User lender;
    private User hirer;


    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public DateTime getRentFrom() {
        return rentFrom;
    }

    public void setRentFrom(final DateTime rentFrom) {
        this.rentFrom = rentFrom;
    }

    public DateTime getRentTo() {
        return rentTo;
    }

    public void setRentTo(final DateTime rentTo) {
        this.rentTo = rentTo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Offer getOffer(){
        return offer;
    }

    public void setOffer(final Offer offer){
        this.offer = offer;
    }

    public User getLender(){
        return lender;
    }

    public  void setLender(final User lender){
        this.lender = lender;
    }

    public User getHirer(){
        return hirer;
    }

    public  void setHirer(final User hirer) {
        this.hirer = hirer;
    }


        @Override
    public String toString() {
        return "Rental{" +
                "rentFrom=" + rentFrom +
                ", rentTo=" + rentTo +
                ", status=" + status +
                ", offer=" + offer +
                ", lender=" + lender +
                ", hirer=" + hirer +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel destination, final int flags) {
        destination.writeLong(rentFrom.getMillis());
        destination.writeLong(rentTo.getMillis());
        destination.writeInt(status);
        destination.writeParcelable(offer, flags);
        destination.writeParcelable(lender, flags);
        destination.writeParcelable(hirer, flags);
    }

    public static final Parcelable.Creator<Rental> CREATOR = new Creator<Rental>() {
        @Override
        public Rental createFromParcel(final Parcel source) {
            return new Rental(source);
        }

        @Override
        public Rental[] newArray(final int size) {
            return new Rental[0];
        }
    };

    private Rental(final Parcel source) {
        rentFrom = new DateTime(source.readLong());
        rentTo = new DateTime(source.readLong());
        status = new Integer(source.readInt());
        offer = source.readParcelable(Offer.class.getClassLoader());
        lender = source.readParcelable(User.class.getClassLoader());
        hirer = source.readParcelable(User.class.getClassLoader());
    }
}
