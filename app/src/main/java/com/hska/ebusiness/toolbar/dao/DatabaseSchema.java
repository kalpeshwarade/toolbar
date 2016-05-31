package com.hska.ebusiness.toolbar.dao;

import android.provider.BaseColumns;

public final class DatabaseSchema {

    private DatabaseSchema() {

    }

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static abstract class CredentialsEntry implements BaseColumns {
        public static final String TABLE_NAME = "credentials";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_USER_FK = "user_fk";
    }

    public static abstract class BalanceEntry implements BaseColumns {
        public static final String TABLE_NAME = "balance";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_USER_FK = "user_fk";
    }

    public static abstract class OfferEntry implements BaseColumns {
        public static final String TABLE_NAME = "offer";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_VALID_FROM = "valid_from";
        public static final String COLUMN_NAME_VALID_TO = "valid_to";
        public static final String COLUMN_NAME_LENDER_FK = "lender_fk";
    }

    public static abstract class RentalEntry implements BaseColumns {
        public static final String TABLE_NAME = "rental";
        public static final String COLUMN_NAME_FROM = "rental_from";
        public static final String COLUMN_NAME_TO = "rental_to";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_OFFER_FK = "offer_fk";
        public static final String COLUMN_NAME_LENDER_FK = "lender_fk";
        public static final String COLUMN_NAME_HIRER_FK = "hirer_fk";
    }
}
