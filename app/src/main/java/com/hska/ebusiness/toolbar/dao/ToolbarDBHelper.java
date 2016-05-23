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

    private final String TAG = this.getClass().getSimpleName();

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
                    ");";

    private static final String SQL_CREATE_TABLE_CREDENTIALS =
            "CREATE TABLE " + CredentialsEntry.TABLE_NAME + " (" +
                    CredentialsEntry._ID + " INTEGER PRIMARY KEY," +
                    CredentialsEntry.COLUMN_NAME_PASSWORD + " TEXT," +
                    CredentialsEntry.COLUMN_NAME_USER_FK + " INTEGER," +
                    "FOREIGN KEY (" + CredentialsEntry.COLUMN_NAME_USER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    ")";

    private static final String SQL_CREATE_TABLE_BALANCE =
            "CREATE TABLE " + BalanceEntry.TABLE_NAME + " (" +
                    BalanceEntry._ID + " INTEGER PRIMARY KEY," +
                    BalanceEntry.COLUMN_NAME_AMOUNT + " INTEGER," +
                    BalanceEntry.COLUMN_NAME_USER_FK + " INTEGER," +
                    "FOREIGN KEY (" + BalanceEntry.COLUMN_NAME_USER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    ")";

    private static final String SQL_CREATE_TABLE_OFFER =
            "CREATE TABLE " + OfferEntry.TABLE_NAME + " (" +
                    OfferEntry._ID + " INTEGER PRIMARY KEY," +
                    OfferEntry.COLUMN_NAME_NAME + " NAME," +
                    OfferEntry.COLUMN_NAME_IMAGE + " TEXT," +
                    OfferEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    OfferEntry.COLUMN_NAME_ZIP_CODE + " TEXT," +
                    OfferEntry.COLUMN_NAME_PRICE + " INTEGER," +
                    OfferEntry.COLUMN_NAME_VALID_FROM + " STRING," +
                    OfferEntry.COLUMN_NAME_VALID_TO + " STRING" +
                    ");";


    private static final String SQL_CREATE_TABLE_RENTAL =
            "CREATE TABLE " + RentalEntry.TABLE_NAME + " (" +
                    RentalEntry._ID + " INTEGER PRIMARY KEY, " +
                    RentalEntry.COLUMN_NAME_FROM + " DATE, " +
                    RentalEntry.COLUMN_NAME_TO + " DATE, " +
                    RentalEntry.COLUMN_NAME_STATUS + " INTEGER, " +
                    RentalEntry.COLUMN_NAME_OFFER_FK + " INTEGER, " +
                    RentalEntry.COLUMN_NAME_HIRER_FK + " INTEGER, " +
                    RentalEntry.COLUMN_NAME_LENDER_FK + " INTEGER, " +
                    "FOREIGN KEY (" + RentalEntry.COLUMN_NAME_OFFER_FK + ") REFERENCES " + OfferEntry.TABLE_NAME + "(" + OfferEntry._ID + "), " +
                    "FOREIGN KEY (" + RentalEntry.COLUMN_NAME_HIRER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "), " +
                    "FOREIGN KEY (" + RentalEntry.COLUMN_NAME_LENDER_FK + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                    ")";


    private static final String SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_CREDENTIALS = "DROP TABLE IF EXISTS " + CredentialsEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_BALANCE = "DROP TABLE IF EXISTS " + BalanceEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_OFFER = "DROP TABLE IF EXISTS " + OfferEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_RENTAL = "DROP TABLE IF EXISTS " + RentalEntry.TABLE_NAME + ";";

    private static ToolbarDBHelper instance = null;
    private Context context;

    private ToolbarDBHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static ToolbarDBHelper getInstance(final Context context) {
        if(instance == null) {
            return new ToolbarDBHelper(context.getApplicationContext());
        }

        return instance;
    }

    public ToolbarDBHelper(final Context context, final String name,
                           final SQLiteDatabase.CursorFactory factory, final int version) {
        super(context, name, factory, version);
    }

    public Cursor findAllUsers() {
        Log.d(TAG, ": findAllUsers");
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final String sortOder = UserEntry.COLUMN_NAME_USERNAME + " ASC";
        queryBuilder.setTables(UserEntry.TABLE_NAME);
        return queryBuilder.query(getReadableDatabase(), null, null, null, null, null, sortOder);
    }

    public Cursor findOfferById(final long id) {
        Log.d( TAG, ": findOfferById " + id );

        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(OfferEntry.TABLE_NAME);
        return queryBuilder.query(getReadableDatabase(),
                new String[]{OfferEntry._ID, OfferEntry.COLUMN_NAME_NAME}, OfferEntry._ID + "=?",
                    new String[]{Long.toString(id)}, null, null, null);
    }

    public long insertOffer(final Offer offer) {
        Log.d(TAG, ": insertOffer: " + offer.getName());

        final ContentValues values = getOfferValues(offer);
        return getWritableDatabase().insert(OfferEntry.TABLE_NAME, null, values);
    }

    public int updateOffer(final Offer offer) {
        Log.d(TAG, ": updateOffer: " + offer.getId());

        final String whereClause = OfferEntry._ID + "=?";
        final String[] whereArgs = new String[]{String.valueOf(offer.getId())};

        final ContentValues values = getOfferValues(offer);

        return getWritableDatabase().update(OfferEntry.TABLE_NAME, values, whereClause, whereArgs);
    }

    private ContentValues getOfferValues(final Offer offer) {
        final ContentValues values = new ContentValues();
        values.put(OfferEntry.COLUMN_NAME_NAME, offer.getName());
        final Uri image = offer.getImage();
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
    public void onCreate(final SQLiteDatabase db) {
        Log.d(TAG, ": onCreate Database with version" + db.getVersion());

        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_CREDENTIALS);
        db.execSQL(SQL_CREATE_TABLE_BALANCE);
        db.execSQL(SQL_CREATE_TABLE_OFFER);
        db.execSQL(SQL_CREATE_TABLE_RENTAL);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.d(TAG, ": onUpgrade database from version" + oldVersion + " to" + newVersion);

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
    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.d(TAG, ": onDowngrade database from version" + newVersion + " to" + oldVersion);

        onUpgrade(db, oldVersion, newVersion);
    }
}