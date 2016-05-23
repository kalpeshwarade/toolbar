package com.hska.ebusiness.toolbar.activities;

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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CAPTURE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CHOOSE;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.REQUEST_IMAGE_CROP;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE;

public class EditOfferActivity extends AppCompatActivity implements TextWatcher {

    private Offer offer;
    private Offer updatedOffer;

    private Intent intent;
    private AlertDialog.Builder builder;

    private EditText offerFrom;
    private EditText offerTo;

    private final Calendar calendarFrom = Calendar.getInstance();
    private final Calendar calendarTo = Calendar.getInstance();

    private final String LOG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        intent = getIntent();

        final Boolean isEditMode = intent.getBooleanExtra(TOOLBAR_OFFER_IS_EDIT_MODE, false);

        if(isEditMode) {
            offer = intent.getParcelableExtra(TOOLBAR_OFFER);
            updatedOffer = offer;
            initContent();
        } else {
            updatedOffer = new Offer();
        }

        builder = new AlertDialog.Builder(this);

        final ImageView offerImage = (ImageView) findViewById(R.id.image_offer_image);
        assert offerImage != null;
        offerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(DIALOG_IMAGE_TITLE);
                builder.setItems(DIALOG_IMAGE_OPTIONS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String selected = (String) DIALOG_IMAGE_OPTIONS[which];
                        switch (selected) {
                            case DIALOG_CAPTURE_IMAGE:
                                try {
                                    captureImage();
                                } catch (IOException e) {
                                    Log.e(LOG, ": " + e.getMessage());
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

        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarFrom.set(Calendar.YEAR, year);
                calendarFrom.set(Calendar.MONTH, monthOfYear);
                calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }
        };

        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarTo.set(Calendar.YEAR, year);
                calendarTo.set(Calendar.MONTH, monthOfYear);
                calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDate();
            }
        };

        offerFrom = (EditText) findViewById(R.id.input_offer_from);
        offerFrom.setRawInputType(InputType.TYPE_NULL);
        assert offerFrom != null;
        offerFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == true)
                    new DatePickerDialog(EditOfferActivity.this, fromDate, calendarFrom.get(Calendar.YEAR),
                            calendarTo.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        offerTo = (EditText) findViewById(R.id.input_offer_to);
        offerTo.setRawInputType(InputType.TYPE_NULL);
        assert offerTo != null;
        offerTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if(hasFocus == true)
                    new DatePickerDialog(EditOfferActivity.this, toDate, calendarTo.get(Calendar.YEAR),
                            calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH)).show();
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_edit_offer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final Boolean isEditMode = intent.getBooleanExtra(TOOLBAR_OFFER_IS_EDIT_MODE, false);
        switch (item.getItemId()) {
            case R.id.action_offer_edit_save:
                if(isEditMode)
                    updateOffer();
                else
                    insertOffer();
                final Intent mainIntentSave = new Intent(this, MainActivity.class);
                mainIntentSave.putExtra(TOOLBAR_OFFER, updatedOffer);
                startActivity(mainIntentSave);
            case R.id.action_offer_edit_cancel:
                final Intent mainIntentCancel = new Intent(this, MainActivity.class);
                mainIntentCancel.putExtra(TOOLBAR_OFFER, offer);
                startActivity(mainIntentCancel);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initContent() {
        Log.d(LOG, ": Initialize Content");
        ((EditText) findViewById(R.id.edit_input_offer_name)).setText(updatedOffer.getName());
        final Uri image = updatedOffer.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            final Bitmap myBitmap = BitmapFactory.decodeFile(updatedOffer.getImage().getPath());
            ((ImageView) findViewById(R.id.image_offer_image)).setImageBitmap(myBitmap);
        }
        ((EditText) findViewById(R.id.input_offer_description)).setText(updatedOffer.getDescription());
        ((EditText) findViewById(R.id.input_offer_from)).setText(updatedOffer.getValidFrom().toString("YYYY-MM-DD"));
        ((EditText) findViewById(R.id.input_offer_to)).setText(updatedOffer.getValidTo().toString("YYYY-MM-DD"));
        ((EditText) findViewById(R.id.input_zip_code)).setText(updatedOffer.getZipCode());
    }

    private void updateOffer() {
        Log.d(LOG, ": Update offer " + updatedOffer.getId());

        updatedOffer.setName(getStringValue(R.id.edit_input_offer_name));
        updatedOffer.setDescription(getStringValue(R.id.input_offer_description));
        updatedOffer.setZipCode(getStringValue(R.id.input_zip_code));
        updatedOffer.setValidFrom(DateTime.parse(offerFrom.getText().toString()));
        updatedOffer.setValidTo(DateTime.parse(offerTo.getText().toString()));

        final UpdateOfferTask updateOfferTask = new UpdateOfferTask(this);
        updateOfferTask.execute(updatedOffer);
    }

    private void insertOffer() {
        Log.d(LOG, ": Insert offer");

        final Offer insertOffer = new Offer();
        insertOffer.setName(getStringValue(R.id.edit_input_offer_name));
        insertOffer.setDescription(getStringValue(R.id.input_offer_description));
        insertOffer.setZipCode(getStringValue(R.id.input_zip_code));
        insertOffer.setValidFrom(DateTime.parse(offerFrom.getText().toString()));
        insertOffer.setValidTo(DateTime.parse(offerTo.getText().toString()));

        updatedOffer = insertOffer;

        final InsertOfferTask insertOfferTask = new InsertOfferTask(this);
        insertOfferTask.execute(updatedOffer);
    }

    private String getStringValue(final int id) {
        final View field = findViewById(id);
        if (field instanceof EditText) {
            final EditText textField = (EditText) field;
            return textField.getText().toString();
        }
        return "";
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    cropImage();
                    break;
                case REQUEST_IMAGE_CHOOSE:
                    offer.setImage(data.getData());
                    cropImage();
                    break;
                case REQUEST_IMAGE_CROP:
                    final ImageView preview = (ImageView) findViewById(R.id.image_offer_image);
                    final Bitmap tmp = data.getExtras().getParcelable("data");
                    preview.setImageBitmap(tmp);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    private void captureImage() throws IOException {
        Log.d(LOG, " : Capture image");
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isIntentSupported(cameraIntent)) {
            updatedOffer.setImage(Uri.fromFile(createImageFile()));
            if (updatedOffer.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, updatedOffer.getImage());
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
                    Log.e(LOG, ": " + e.getMessage());
                }
            }
        } else {
            Toast.makeText(this, "Seems like you have no camera!", Toast.LENGTH_SHORT).show();
        }
    }

    private void cropImage() {
        Log.d(LOG, " : Crop image");
        final Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(updatedOffer.getImage(), "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 500);
        cropIntent.putExtra("outputY", 500);
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, updatedOffer.getImage());
        try {
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Seems like your device can not crop images!", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() {
        Log.d(LOG, " : Create image file");

        final String dateFormat = ToolbarConstants.TOOLBAR_FILE_DATE_SUFFIX;
        final SimpleDateFormat timeStamp = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        final String imageFileName = "OFFER_" + timeStamp + "_";
        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        return new File(storageDir, imageFileName + ".jpg");
    }

    private boolean isIntentSupported(final Intent cameraIntent) {
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_ALL);
        return !activities.isEmpty();
    }

    private void validateInput() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}