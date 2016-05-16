package com.hska.ebusiness.toolbar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.hska.ebusiness.toolbar.model.Offer;

import static com.hska.ebusiness.toolbar.dao.DatabaseSchema.*;

public class ToolbarDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "toolbar.db";

    private static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_USERNAME + " TEXT," +
                    UserEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    UserEntry.COLUMN_NAME_STREET + " TEXT," +
                    UserEntry.COLUMN_NAME_ZIP_CODE + " TEXT," +
                    UserEntry.COLUMN_NAME_COUNTRY + " TEXT," +
                    UserEntry.COLUMN_NAME_PHONE + " TEXT," +
                    UserEntry.COLUMN_NAME_DESCRIPTION + " TEXT" +
                    " );";

    private static final String SQL_CREATE_TABLE_CREDENTIALS =
            "CREATE TABLE " + CredentialsEntry.TABLE_NAME + " (" +
                    CredentialsEntry._ID + " INTEGER PRIMARY KEY," +
                    CredentialsEntry.COLUMN_NAME_PASSWORD + " TEXT," +
                    CredentialsEntry.COLUMN_NAME_USER_FK + " INTEGER," +
                    "FOREIGN KEY (" + CredentialsEntry.COLUMN_NAME_USER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    " );";

    private static final String SQL_CREATE_TABLE_BALANCE =
            "CREATE TABLE " + BalanceEntry.TABLE_NAME + " (" +
                    BalanceEntry._ID + " INTEGER PRIMARY KEY," +
                    BalanceEntry.COLUMN_NAME_AMOUNT + " INTEGER," +
                    BalanceEntry.COLUMN_NAME_USER_FK + " INTEGER," +
                    "FOREIGN KEY (" + BalanceEntry.COLUMN_NAME_USER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    " );";

    private static final String SQL_CREATE_TABLE_OFFER =
            "CREATE TABLE " + OfferEntry.TABLE_NAME + " (" +
                    OfferEntry._ID + " INTEGER PRIMARY KEY," +
                    OfferEntry.COLUMN_NAME_NAME + " NAME," +
                    OfferEntry.COLUMN_NAME_IMAGE + " TEXT," +
                    OfferEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    OfferEntry.COLUMN_NAME_ZIP_CODE + " TEXT," +
                    OfferEntry.COLUMN_NAME_PRICE + " INTEGER," +
                    OfferEntry.COLUMN_NAME_VALID_FROM + " DATE," +
                    OfferEntry.COLUMN_NAME_VALID_TO + " DATE," +
                    " );";


    private static final String SQL_CREATE_TABLE_RENTAL =
            "CREATE TABLE " + RentalEntry.TABLE_NAME + " (" +
                    RentalEntry._ID + " INTEGER PRIMARY KEY," +
                    RentalEntry.COLUMN_NAME_FROM + " DATE," +
                    RentalEntry.COLUMN_NAME_TO + " DATE," +
                    RentalEntry.COLUMN_NAME_STATUS + " INTEGER," +
                    RentalEntry.COLUMN_NAME_OFFER_FK + " INTEGER," +
                    RentalEntry.COLUMN_NAME_HIRER_FK + " INTEGER," +
                    RentalEntry.COLUMN_NAME_LENDER_FK + " INTEGER," +
                    "FOREIGN KEY (" + RentalEntry.COLUMN_NAME_OFFER_FK + ") REFERENCES " + OfferEntry.TABLE_NAME + "(" + OfferEntry._ID + ")" +
                    "FOREIGN KEY (" + RentalEntry.COLUMN_NAME_HIRER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    "FOREIGN KEY (" + RentalEntry.COLUMN_NAME_LENDER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    " );";


    private static final String SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_CREDENTIALS = "DROP TABLE IF EXISTS " + CredentialsEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_BALANCE = "DROP TABLE IF EXISTS " + BalanceEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_OFFER = "DROP TABLE IF EXISTS " + OfferEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_RENTAL = "DROP TABLE IF EXISTS " + RentalEntry.TABLE_NAME + ";";


    private static ToolbarDBHelper instance = null;
    private Context context;

    private ToolbarDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static ToolbarDBHelper getInstance(Context context) {
        if(instance == null) {
            return new ToolbarDBHelper(context.getApplicationContext());
        }

        return instance;
    }

    public ToolbarDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Cursor findAllUsers() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String sortOder = UserEntry.COLUMN_NAME_USERNAME + " ASC";
        queryBuilder.setTables(UserEntry.TABLE_NAME);
        return queryBuilder.query(getReadableDatabase(), null, null, null, null, null, sortOder);
    }


    public int updateOffer(Offer offer) {
        String whereClause = OfferEntry._ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(offer.getId())};

        ContentValues values = getOfferValues(offer);
        Log.d(ToolbarDBHelper.class.getSimpleName(), "updateOffer: " + offer.toString());

        return getWritableDatabase().update(OfferEntry.TABLE_NAME, values, whereClause, whereArgs);
    }

    private ContentValues getOfferValues(Offer offer) {
        ContentValues values = new ContentValues();
        values.put(OfferEntry.COLUMN_NAME_NAME, offer.getName());
        Uri image = offer.getImage();
        if (image == null) {
            offer.setImage(Uri.parse(""));
        }
        values.put(OfferEntry.COLUMN_NAME_IMAGE, offer.getImage().getPath());
        values.put(OfferEntry.COLUMN_NAME_DESCRIPTION, offer.getDescription());
        values.put(OfferEntry.COLUMN_NAME_ZIP_CODE, offer.getZipCode());
        values.put(OfferEntry.COLUMN_NAME_PRICE, offer.getPrice());
        values.put(OfferEntry.COLUMN_NAME_VALID_FROM, offer.getValidFrom().getMillis());
        values.put(OfferEntry.COLUMN_NAME_VALID_TO, offer.getValidTo().getMillis());

        return values;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_CREDENTIALS);
        db.execSQL(SQL_CREATE_TABLE_BALANCE);
        db.execSQL(SQL_CREATE_TABLE_OFFER);
        db.execSQL(SQL_CREATE_TABLE_RENTAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_RENTAL);
        db.execSQL(SQL_DROP_TABLE_OFFER);
        db.execSQL(SQL_DROP_TABLE_BALANCE);
        db.execSQL(SQL_DROP_TABLE_CREDENTIALS);
        db.execSQL(SQL_DROP_TABLE_USER);

        db.execSQL(SQL_CREATE_TABLE_RENTAL);
        db.execSQL(SQL_CREATE_TABLE_OFFER);
        db.execSQL(SQL_CREATE_TABLE_BALANCE);
        db.execSQL(SQL_CREATE_TABLE_CREDENTIALS);
        db.execSQL(SQL_CREATE_TABLE_USER);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}