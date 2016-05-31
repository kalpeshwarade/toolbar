package com.hska.ebusiness.toolbar.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

public class Rental implements Parcelable {

    public Rental() {

    }

    public Rental(final long rentFrom, final long rentTo, Integer status, Long offer_fk, Long lender_fk, Long hirer_fk) {
        this.rentFrom = rentFrom;
        this.rentTo = rentTo;
        this.status = status;
        this.offer_fk = offer_fk;
        this.lender_fk = lender_fk;
        this.hirer_fk = hirer_fk;
    }

    private Long id;
    private Long rentFrom;
    private Long rentTo;
    private Integer status;
    private Long offer_fk;
    private Long lender_fk;
    private Long hirer_fk;


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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Long getOffer_fk(){
        return offer_fk;
    }

    public void setOffer_fk(final Long offer_fk){
        this.offer_fk = offer_fk;
    }

    public Long getLender_fk(){
        return lender_fk;
    }

    public  void setLender_fk(final Long lender_fk){
        this.lender_fk = lender_fk;
    }

    public Long getHirer_fk(){
        return hirer_fk;
    }

    public  void setHirer_fk(final Long hirer_fk) {
        this.hirer_fk = hirer_fk;
    }


        @Override
    public String toString() {
        return "Rental{" +
                "rentFrom=" + rentFrom +
                ", rentTo=" + rentTo +
                ", status=" + status +
                ", offer=" + offer_fk +
                ", lender=" + lender_fk +
                ", hirer=" + hirer_fk +
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
        destination.writeLong(offer_fk);
        destination.writeLong(lender_fk);
        destination.writeLong(hirer_fk);
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
        rentFrom = source.readLong();
        rentTo = source.readLong();
        status = new Integer(source.readInt());
        offer_fk = source.readParcelable(Offer.class.getClassLoader());
        lender_fk = source.readParcelable(User.class.getClassLoader());
        hirer_fk = source.readParcelable(User.class.getClassLoader());
    }
}
