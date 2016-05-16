package com.hska.ebusiness.toolbar.activities;

import android.app.AlertDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.fragments.DatePickerFragment;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.tasks.UpdateOfferTask;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.*;

public class EditOfferActivity extends AppCompatActivity {

    private Offer offer;
    private Offer updatedOffer;
    private AlertDialog.Builder builder;

    private EditText offerFrom;
    private EditText offerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        offer = getIntent().getParcelableExtra(TOOLBAR_OFFER);
        updatedOffer = offer;
        initContent();

        builder = new AlertDialog.Builder(this);

        ImageView offerImage = (ImageView) findViewById(R.id.image_offer_image);
        assert offerImage != null;
        offerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(DIALOG_IMAGE_TITLE);
                builder.setItems(DIALOG_IMAGE_OPTIONS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = (String) DIALOG_IMAGE_OPTIONS[which];
                        switch (selected) {
                            case DIALOG_CAPTURE_IMAGE:
                                try {
                                    captureImage();
                                } catch (IOException e) {
                                    e.printStackTrace();
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

        offerFrom = (EditText) findViewById(R.id.input_offer_from);
        assert offerFrom != null;
        offerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        offerTo = (EditText) findViewById(R.id.input_offer_to);
        assert offerTo != null;
        offerTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_edit_offer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_offer_edit_save:
                updateOffer();
                Intent mainIntentSave = new Intent(this, MainActivity.class);
                mainIntentSave.putExtra(TOOLBAR_OFFER, updatedOffer);
                startActivity(mainIntentSave);
            case R.id.action_offer_edit_cancel:
                Intent mainIntentCancel = new Intent(this, MainActivity.class);
                mainIntentCancel.putExtra(TOOLBAR_OFFER, offer);
                startActivity(mainIntentCancel);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initContent() {
        ((EditText) findViewById(R.id.edit_input_offer_name)).setText(updatedOffer.getName());
        Uri image = updatedOffer.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(updatedOffer.getImage().getPath());
            ((ImageView) findViewById(R.id.image_offer_image)).setImageBitmap(myBitmap);
        }
        ((EditText) findViewById(R.id.input_offer_description)).setText(updatedOffer.getDescription());
        ((EditText) findViewById(R.id.input_offer_from)).setText(updatedOffer.getValidFrom().toString("YYYY-MM-DD"));
        ((EditText) findViewById(R.id.input_offer_to)).setText(updatedOffer.getValidTo().toString("YYYY-MM-DD"));
        ((EditText) findViewById(R.id.input_zip_code)).setText(updatedOffer.getZipCode());
    }

    private void updateOffer() {
        updatedOffer.setName(getStringValue(R.id.edit_input_offer_name));
        updatedOffer.setDescription(getStringValue(R.id.input_offer_description));
        updatedOffer.setZipCode(getStringValue(R.id.input_zip_code));
        updatedOffer.setValidFrom(DateTime.parse(offerFrom.getText().toString()));
        updatedOffer.setValidTo(DateTime.parse(offerTo.getText().toString()));

        UpdateOfferTask updateOfferTask = new UpdateOfferTask(this);
        updateOfferTask.execute(updatedOffer);
    }

    private String getStringValue(int id) {
        View field = findViewById(id);
        if (field instanceof EditText) {
            EditText textField = (EditText) field;
            return textField.getText().toString();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    ImageView preview = (ImageView) findViewById(R.id.image_offer_image);
                    Bitmap tmp = data.getExtras().getParcelable("data");
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    private void captureImage() throws IOException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isIntentSupported(cameraIntent)) {
            updatedOffer.setImage(Uri.fromFile(createImageFile()));
            if (updatedOffer.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, updatedOffer.getImage());
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Seems like you have no camera!", Toast.LENGTH_SHORT).show();
        }
    }

    private void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
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
            Toast.makeText(this, "Seems like your device can not rop images!", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "OFFER_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        return new File(storageDir, imageFileName + ".jpg");
    }

    private boolean isIntentSupported(Intent cameraIntent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_ALL);
        return !activities.isEmpty();
    }

}