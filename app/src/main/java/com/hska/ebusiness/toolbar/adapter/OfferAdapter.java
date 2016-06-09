package com.hska.ebusiness.toolbar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.activities.MainActivity;
import com.hska.ebusiness.toolbar.model.Offer;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private List<Offer> offerList;

    public OfferAdapter(List<Offer> offerList) {
        this.offerList = offerList;
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    @Override
    public void onBindViewHolder(final OfferViewHolder offerViewHolder, final int position) {
        Offer offer = offerList.get(position);
        offerViewHolder.offerName.setText(offer.getName());
        try {
            offerViewHolder.offerImage.setImageBitmap(resizeImage(Uri.parse(offer.getImage()), context));
        } catch (final IOException e) {
            Log.e(TAG, " Error while loading image: " + e.getMessage());
        }
        offerViewHolder.offerFrom.setText(new DateTime(offer.getValidFrom()).toLocalDate().toString());
        offerViewHolder.offerTo.setText(new DateTime(offer.getValidTo()).toLocalDate().toString());
        offerViewHolder.offerPrice.setText(Long.toString(offer.getPrice()));

    }

    @Override
    public OfferViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int position) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_offer_list, viewGroup, false);

        return new OfferViewHolder(itemView);
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {

        protected ImageView offerImage;
        protected TextView offerName;
        protected TextView offerPrice;
        protected TextView offerFrom;
        protected TextView offerTo;

        public OfferViewHolder(final View itemView) {
            super(itemView);
            offerImage = (ImageView) itemView.findViewById(R.id.show_image_offer_image);
            offerName = (TextView) itemView.findViewById(R.id.label_offer_name);
            offerPrice = (TextView) itemView.findViewById(R.id.show_input_offer_price);
            offerFrom = (TextView) itemView.findViewById(R.id.show_input_offer_from);
            offerTo = (TextView) itemView.findViewById(R.id.show_input_offer_to);
        }
    }

    /**
     * Resizes image to fit in ImageView
     *
     * @param file Uri to the image path
     * @return the resized bitmap
     * @throws IOException if access to external storage fails
     */
    private Bitmap resizeImage(final Uri file, final Context context) throws IOException {
        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), file);

        final float width = bitmap.getWidth();
        final float height = bitmap.getHeight();

        final int aimedHeight = 1024;
        final int aimedWidth = (int) (width / height * (float) aimedHeight);

        return Bitmap.createScaledBitmap(bitmap, aimedWidth, aimedHeight, false);
    }

}
