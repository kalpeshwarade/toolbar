package com.hska.ebusiness.toolbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rental implements Parcelable {

    public Rental() {

    }

    public Rental(final long rentFrom, final long rentTo, final int status, final long offer_fk,
                  final long lender_fk, final long hirer_fk) {
        this.rentFrom = rentFrom;
        this.rentTo = rentTo;
        this.status = status;
        this.offer = offer_fk;
        this.lender = lender_fk;
        this.hirer = hirer_fk;
    }

    private long id;
    private long rentFrom;
    private long rentTo;
    private int status;
    private long offer;
    private long lender;
    private long hirer;


    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getRentFrom() {
        return rentFrom;
    }

    public void setRentFrom(final long rentFrom) {
        this.rentFrom = rentFrom;
    }

    public long getRentTo() {
        return rentTo;
    }

    public void setRentTo(final long rentTo) {
        this.rentTo = rentTo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public long getOffer(){
        return offer;
    }

    public void setOffer(final long offer){
        this.offer = offer;
    }

    public long getLender(){
        return lender;
    }

    public  void setLender(final long lender){
        this.lender = lender;
    }

    public long getHirer(){
        return hirer;
    }

    public  void setHirer(final long hirer) {
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
        destination.writeLong(rentFrom);
        destination.writeLong(rentTo);
        destination.writeInt(status);
        destination.writeLong(offer);
        destination.writeLong(lender);
        destination.writeLong(hirer);
    }

    public static final Parcelable.Creator<Rental> CREATOR = new Creator<Rental>() {
        @Override
        public Rental createFromParcel(final Parcel source) {
            return new Rental(source);
        }

        @Override
        public Rental[] newArray(final int size) {
            return new Rental[size];
        }
    };

    private Rental(final Parcel source) {
        rentFrom = source.readLong();
        rentTo = source.readLong();
        status = source.readInt();
        offer = source.readLong();
        lender = source.readLong();
        hirer = source.readLong();
    }
}
