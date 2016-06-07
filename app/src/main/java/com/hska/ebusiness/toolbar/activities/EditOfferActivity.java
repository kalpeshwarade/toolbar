package com.hska.ebusiness.toolbar.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.tasks.InsertOfferTask;
import com.hska.ebusiness.toolbar.tasks.UpdateOfferTask;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_CAPTURE_IMAGE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_CHOOSE_IMAGE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_IMAGE_OPTIONS;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_IMAGE_TITLE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CAPTURE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CHOOSE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE;

public class EditOfferActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Boolean isEditMode = false;
    private Uri file;
    private Offer offer;
    private AlertDialog.Builder builder;
    private ImageView offerImage;
    private EditText offerName;
    private EditText offerDescription;
    private EditText offerFrom;
    private EditText offerTo;
    private EditText offerZipCode;

    private final Calendar calendarFrom = Calendar.getInstance();
    private final Calendar calendarTo = Calendar.getInstance();

    /**
     * Used to initialize the layout and field of the Activity
     *
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        offerName = (EditText) findViewById(R.id.edit_input_offer_name);
        offerDescription = (EditText) findViewById(R.id.input_offer_description);
        offerFrom = (EditText) findViewById(R.id.input_offer_from);
        offerTo = (EditText) findViewById(R.id.input_offer_to);
        offerZipCode = (EditText) findViewById(R.id.input_zip_code);
        offerImage = (ImageView) findViewById(R.id.image_offer_image);

        isEditMode = getIntent().getExtras().getBoolean(TOOLBAR_OFFER_IS_EDIT_MODE);
        builder = new AlertDialog.Builder(this);

        if(isEditMode) {
            offer = getIntent().getParcelableExtra(TOOLBAR_OFFER);
            initContent();
        } else {
            offer = new Offer();
        }

        /**
         * Asks user for required permissions.
         * If permissions are not granted, it will not be possible to choose or capture an image.
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                offerImage.setOnClickListener(null);
        } else {
            addImageClickListener();
        }

        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year,
                                  final int monthOfYear, final int dayOfMonth) {
                calendarFrom.set(Calendar.YEAR, year);
                calendarFrom.set(Calendar.MONTH, monthOfYear);
                calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }
        };

        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year,
                                  final int monthOfYear, final int dayOfMonth) {
                calendarTo.set(Calendar.YEAR, year);
                calendarTo.set(Calendar.MONTH, monthOfYear);
                calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDate();
            }
        };

        if (offerFrom != null)
            offerFrom.setRawInputType(InputType.TYPE_NULL);
        offerFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if(hasFocus)
                    new DatePickerDialog(EditOfferActivity.this, fromDate,
                            calendarFrom.get(Calendar.YEAR),
                            calendarTo.get(Calendar.MONTH),
                            calendarFrom.get(Calendar.DAY_OF_MONTH))
                            .show();
            }
        });

        if (offerTo != null)
            offerTo.setRawInputType(InputType.TYPE_NULL);
        offerTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if(hasFocus)
                    new DatePickerDialog(EditOfferActivity.this, toDate,
                            calendarTo.get(Calendar.YEAR),
                            calendarTo.get(Calendar.MONTH),
                            calendarTo.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
        });
    }


    /**
     * Waiting for user permissions and allow image view click if permissions are granted
     *
     * @param requestCode the request code
     * @param permissions all the permissions to ask for
     * @param grantResults grant results by user
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        addImageClickListener();
            }
        }
    }

    /**
     * Add the image click listener once the permissions are granted
     */
    private void addImageClickListener() {
        offerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                builder.setTitle(DIALOG_IMAGE_TITLE);
                builder.setItems(DIALOG_IMAGE_OPTIONS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final String selected = (String) DIALOG_IMAGE_OPTIONS[which];
                        switch (selected) {
                            case DIALOG_CAPTURE_IMAGE:
                                try {
                                    captureImage();
                                } catch (IOException e) {
                                    Log.e(TAG, "Error while taking picture: " + e.getMessage());
                                }
                                break;
                            case DIALOG_CHOOSE_IMAGE:
                                chooseImage();
                                break;
                            default:
                                dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }


    /**
     * Sets the picked date to the text field 'from'
     */
    private void updateFromDate() {
        final String dateFormat = ToolbarConstants.TOOLBAR_DATE_FORMAT;
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        offerFrom.setText(sdf.format(calendarFrom.getTime()));
    }

    /**
     * Sets the picked date to the text field 'to'
     */
    private void updateToDate() {
        final String dateFormat = ToolbarConstants.TOOLBAR_DATE_FORMAT;
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        offerTo.setText(sdf.format(calendarTo.getTime()));
    }

    /**
     * Used to inflate the Activity's specific menu
     *
     * @param menu the Menu
     * @return whether to show menu
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_edit_offer, menu);
        return true;
    }

    /**
     * Gets called when an item from the menu gets selected
     *
     * @param item the selected menu item
     * @return true if event was handled successfully
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        isEditMode = getIntent().getBooleanExtra(TOOLBAR_OFFER_IS_EDIT_MODE, false);

        switch (item.getItemId()) {
            case R.id.action_offer_edit_save:
                if(isEditMode)
                    updateOffer();
                else
                    insertOffer();
                final Intent showIntentSave = new Intent(this, ShowOfferActivity.class);
                showIntentSave.putExtra(TOOLBAR_OFFER, offer);
                startActivity(showIntentSave);
                return true;
            case R.id.action_offer_edit_cancel:
                final Intent showIntentCancel = new Intent(this, ShowOfferActivity.class);
                showIntentCancel.putExtra(TOOLBAR_OFFER, offer);
                startActivity(showIntentCancel);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialize content if edit mode is activated.
     * Pre-fill fields with order values.
     * Create placeholder for image if there is none.
     */
    private void initContent() {
        Log.d(TAG, ": Initialize Content");

        if(offer.getImage() != null) {
            final Uri image = Uri.parse(offer.getImage());
            if (image != null && new File(image.getPath()).exists()) {
                try {
                    offerImage.setImageBitmap(this.resizeImage(image));
                } catch (IOException e) {
                    Log.e(TAG, "Error while initializing image: " + e.getMessage());
                    return;
                }
            }
        } else {
            offerImage.setImageResource(R.drawable.ic_insert_photo_black_48dp);
        }

        offerName.setText(offer.getName());
        offerDescription.setText(offer.getDescription());
        offerFrom.setText(new DateTime(offer.getValidFrom()).toLocalDate().toString());
        offerTo.setText(new DateTime(offer.getValidTo()).toLocalDate().toString());
        offerZipCode.setText(offer.getZipCode());
    }

    /**
     * Retrieves oder fields from UI and starts AsyncTask for database insert
     */
    private void insertOffer() {
        Log.d(TAG, ": Insert offer");

        offer.setName(offerName.getText().toString());
        offer.setDescription(offerDescription.getText().toString());
        offer.setZipCode(offerZipCode.getText().toString());
        offer.setValidFrom(DateTime.parse(offerFrom.getText().toString()).getMillis());
        offer.setValidTo(DateTime.parse(offerTo.getText().toString()).getMillis());

        final InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(offer);
    }

    /**
     * Retrieves oder fields from UI and starts AsyncTask for database update
     */
    private void updateOffer() {
        Log.d(TAG, ": Update offer " + offer.getId());

        offer.setName(offerName.getText().toString());
        offer.setDescription(offerDescription.getText().toString());
        offer.setZipCode(offerZipCode.getText().toString());
        offer.setValidFrom(DateTime.parse(offerFrom.getText().toString()).getMillis());
        offer.setValidTo(DateTime.parse(offerTo.getText().toString()).getMillis());

        final UpdateOfferTask updateOfferTask = new UpdateOfferTask(this);
        updateOfferTask.execute(offer);
    }

    /**
     * Callback for startActivityForResult gets used to process result of intents
     *
     * @param requestCode request code of the started intent
     * @param resultCode  status code to determine whether intent was successful
     * @param data        returned intent data (which is null when taking picture
     *                    with camera since the bitmap is stored to file URI)
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @NonNull final Intent data) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK) {
                        offer.setImage(file.toString());
                        try {
                            offerImage.setImageBitmap(resizeImage(file));
                        } catch (IOException e) {
                            Log.e(TAG, "Error while getting captured image: " + e.getMessage());
                        }
                        break;
                    }
                case REQUEST_IMAGE_CHOOSE:
                    if (resultCode == RESULT_OK && data.getData() != null) {
                        file = data.getData();
                        offer.setImage(file.toString());
                        try {
                            offerImage.setImageBitmap(resizeImage(file));
                        } catch (IOException e) {
                            Log.e(TAG, "Error while getting chosen image: " + e.getMessage());
                        }
                        break;
                    }
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
    }

    /**
     * Starts Intent to choose existing image from gallery
     */
    private void chooseImage() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    /**
     * Starts camera to take picture
     *
     * @throws IOException if there was an error while accessing the internal storage.
     */
    private void captureImage() throws IOException {
        Log.d(TAG, " : Capture image");
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isIntentSupported(cameraIntent)) {
            try {
                file = Uri.fromFile(createImageFile());
                offer.setImage(file.toString());
            } catch (IOException e) {
                Log.e(TAG, "Error while capturing image: " + e.getMessage());
                Toast.makeText(this, "Could not access external storage!", Toast.LENGTH_SHORT).show();
            }
            if (file != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Error since there is no such activity: " + e.getMessage());
                    Toast.makeText(this, "No such feature found!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Seems like there is no camera!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Resizes image to fit in ImageView
     *
     * @param file Uri to the image path
     * @return the resized bitmap
     * @throws IOException if access to external storage fails
     */
    private Bitmap resizeImage(final Uri file) throws IOException {
        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);

        final float width = bitmap.getWidth();
        final float height = bitmap.getHeight();

        final int aimedHeight = 1024;
        final int aimedWidth = (int) (width / height * (float) aimedHeight);

        return Bitmap.createScaledBitmap(bitmap, aimedWidth, aimedHeight, false);
    }

    /**
     * Creates File for picture in external file directory
     *
     * @return new File in environment's public directory
     * @throws IOException if access to external storage fails
     */
    private File createImageFile() throws IOException {
        Log.d(TAG, " : Create image file");

        final String dateFormat = ToolbarConstants.TOOLBAR_DATE_FORMAT;
        final String timeStamp = new SimpleDateFormat(dateFormat, Locale.GERMAN).format(new Date());
        final File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Toolbar");

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                return null;
            }
        }

        return new File(storageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    /**
     * Helper method to check if a Intent is supported by device
     *
     * @param supportIntent Intent to be checked
     * @return true if Intent is safe to use
     */
    private boolean isIntentSupported(final Intent supportIntent) {
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryIntentActivities(supportIntent, PackageManager.MATCH_ALL);
        return !activities.isEmpty();
    }
}