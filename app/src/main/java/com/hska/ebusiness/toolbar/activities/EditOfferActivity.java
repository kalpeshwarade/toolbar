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
import android.graphics.BitmapFactory;
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
import java.util.List;
import java.util.Locale;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_CAPTURE_IMAGE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_CHOOSE_IMAGE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_IMAGE_OPTIONS;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.DIALOG_IMAGE_TITLE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.PERMISSIONS_REQUEST_CAMERA;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.PERMISSIONS_REQUEST_WRITE_STORAGE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CAPTURE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CHOOSE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CROP;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE;

public class EditOfferActivity extends AppCompatActivity {

    private boolean isEditMode = false;
    private boolean permissionsGranted = false;
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
    private final String TAG = this.getClass().getSimpleName();

    /**
     * Used to initialize the layout and field of the Activity
     *
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        checkPermissions();

        builder = new AlertDialog.Builder(this);
        isEditMode = getIntent().getBooleanExtra(TOOLBAR_OFFER_IS_EDIT_MODE, false);

        if(isEditMode) {
            offer = getIntent().getParcelableExtra(TOOLBAR_OFFER);
            initContent();
        } else {
            offer = new Offer();
        }

        offerName = (EditText) findViewById(R.id.edit_input_offer_name);
        offerDescription = (EditText) findViewById(R.id.input_offer_description);
        offerZipCode = (EditText) findViewById(R.id.input_zip_code);
        offerImage = (ImageView) findViewById(R.id.image_offer_image);

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
                                    Log.e(TAG, ": " + e.getMessage());
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
                if (permissionsGranted)
                    builder.show();
                else
                    Toast.makeText(EditOfferActivity.this,
                            "You have not granted the permissions", Toast.LENGTH_SHORT).show();
            }
        });

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

        offerFrom = (EditText) findViewById(R.id.input_offer_from);
        if (offerFrom != null)
            offerFrom.setRawInputType(InputType.TYPE_NULL);
        offerFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if(hasFocus == true)
                    new DatePickerDialog(EditOfferActivity.this, fromDate,
                            calendarFrom.get(Calendar.YEAR),
                            calendarTo.get(Calendar.MONTH),
                            calendarFrom.get(Calendar.DAY_OF_MONTH))
                            .show();
            }
        });

        offerTo = (EditText) findViewById(R.id.input_offer_to);
        if (offerTo != null)
            offerTo.setRawInputType(InputType.TYPE_NULL);
        offerTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if(hasFocus == true)
                    new DatePickerDialog(EditOfferActivity.this, toDate,
                            calendarTo.get(Calendar.YEAR),
                            calendarTo.get(Calendar.MONTH),
                            calendarTo.get(Calendar.DAY_OF_MONTH))
                            .show();
            }
        });
    }

    private void updateFromDate() {
        final String dateFormat = ToolbarConstants.TOOLBAR_DATE_FORMAT;
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        offerFrom.setText(sdf.format(calendarFrom.getTime()));
    }

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
     * Gets called when item from menu gets selected
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
                final Intent mainIntentSave = new Intent(this, MainActivity.class);
                mainIntentSave.putExtra(TOOLBAR_OFFER, offer);
                startActivity(mainIntentSave);
                return true;
            case R.id.action_offer_edit_cancel:
                final Intent mainIntentCancel = new Intent(this, MainActivity.class);
                startActivity(mainIntentCancel);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialize content if edit mode is activated.
     * Pre-fill fields with order values.
     */
    private void initContent() {
        Log.d(TAG, ": Initialize Content");

        offerName.setText(offer.getName());

        final Uri image = offer.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            final Bitmap offerBitmap = BitmapFactory.decodeFile(offer.getImage().getPath());
            offerImage.setImageBitmap(offerBitmap);
        }

        offerDescription.setText(offer.getDescription());
        offerFrom.setText(offer.getValidFrom().toString("YYYY-MM-DD"));
        offerTo.setText(offer.getValidTo().toString("YYYY-MM-DD"));
        offerZipCode.setText(offer.getZipCode());
    }

    /**
     * Retrieves oder fields from UI and starts AsyncTask for database insert
     */
    private void insertOffer() {
        Log.d( TAG, ": Insert offer");

        offer.setName(getStringValue(R.id.edit_input_offer_name));
        offer.setDescription(getStringValue(R.id.input_offer_description));
        offer.setZipCode(getStringValue(R.id.input_zip_code));
        offer.setValidFrom(DateTime.parse(offerFrom.getText().toString()));
        offer.setValidTo(DateTime.parse(offerTo.getText().toString()));

        final InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(offer);
    }

    /**
     * Retrieves oder fields from UI and starts AsyncTask for database update
     */
    private void updateOffer() {
        Log.d(TAG, ": Update offer " + offer.getId());

        offer.setName(getStringValue(R.id.edit_input_offer_name));
        offer.setDescription(getStringValue(R.id.input_offer_description));
        offer.setZipCode(getStringValue(R.id.input_zip_code));
        offer.setValidFrom(DateTime.parse(offerFrom.getText().toString()));
        offer.setValidTo(DateTime.parse(offerTo.getText().toString()));

        final UpdateOfferTask updateOfferTask = new UpdateOfferTask(this);
        updateOfferTask.execute(offer);
    }

    /**
     * Helper method to extract String values from input fields
     *
     * @param id id of the EditText
     * @return text value of EditText
     */
    @NonNull
    private String getStringValue(final int id) {
        final View field = findViewById(id);
        if (field instanceof EditText) {
            final EditText textField = (EditText) field;
            return textField.getText().toString();
        }
        return "";
    }

    /**
     * Check permissions since this is not only done in the AndroidManifest.xml anymore.
     * This needs to be done for API 23 and coming API levels.
     */
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    /**
     * Sets the permissions for this activity. If the permission is denied
     * for camera or storage access, the user will not be able to specify an image for the offer.
     *
     * @param requestCode   which is the permission request
     * @param permissions   which contains the permissions asked from the user
     * @param grantResults  which contains the user picked permissions
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String permissions[],
                                           @NonNull final int[] grantResults) {
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = true;
                } else {
                    permissionsGranted = false;
                    return;
                }
            }
            return;
        }
        permissionsGranted = false;
    }

    /**
     * Callback for startActivityForResult gets used to process result of intents
     *
     * @param requestCode request code of the started intent
     * @param resultCode  status code to determine whether intent was successful
     * @param data        returned data of the intent
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (data != null && data.getData() != null) {
                        offer.setImage(data.getData());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), offer.getImage());
                            final float width = bitmap.getWidth();
                            final float height = bitmap.getHeight();
                            final int aimedHeight = 300;
                            final int aimedWidth = (int) (width / height * (float) aimedHeight);
                            Bitmap savedBitmap = Bitmap.createScaledBitmap(bitmap, aimedWidth, aimedHeight, false);
                            offerImage.setImageBitmap(savedBitmap);
                            break;
                        } catch (IOException e) {
                            Log.e(TAG, " : setBitmap " + e.getMessage());
                        }
                    }
                case REQUEST_IMAGE_CHOOSE:
                    if (data != null && data.getData() != null) {
                        offer.setImage(data.getData());
                        offerImage.setImageURI(data.getData());
                        cropImage();
                        break;
                    }
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
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
     * Starts Intent to open camera and take picture
     */
    private void captureImage() throws IOException {
        Log.d(TAG, " : Capture image");
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isIntentSupported(cameraIntent)) {
            offer.setImage(Uri.fromFile(createImageFile()));
            if (offer.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, offer.getImage());
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, ": " + e.getMessage());
                }
            }
        } else {
            Toast.makeText(this, "Seems like you have no camera!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts Intent to crop image
     */
    private void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(offer.getImage(), "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 500);
        cropIntent.putExtra("outputY", 500);
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, offer.getImage());
        try {
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Whoops - your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates File for picture in external file directory
     *
     * @return new File in App's private file storage
     * @throws IOException if access to external storage fails
     */
    private File createImageFile() throws IOException {
        Log.d(TAG, " : Create image file");

        final String dateFormat = ToolbarConstants.TOOLBAR_FILE_DATE_SUFFIX;
        final SimpleDateFormat timeStamp = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        final String imageFileName = "OFFER_" + timeStamp + "_";
        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdir();
        }

        return new File(storageDir, imageFileName + ".jpg");
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