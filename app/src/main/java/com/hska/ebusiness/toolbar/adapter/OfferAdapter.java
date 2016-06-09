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
import com.hska.ebusiness.toolbar.model.Offer;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private OnItemClickListener onItemClickListener;
    private List<Offer> offerList;
    private Context context;

    public OfferAdapter(List<Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(final View view, final int position);
    }

    /**
     * Set onItemClickListener for RecyclerView
     *
     * @param onItemClickListener the listener
     */
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    /**
     * Called when an item is bound to the view (one card in the layout)
     *
     * @param offerViewHolder the view holder
     * @param position        the position of the element in the view
     */
    @Override
    public void onBindViewHolder(final OfferViewHolder offerViewHolder, final int position) {
        final Offer offer = offerList.get(position);
        offerViewHolder.offerName.setText(offer.getName());
        try {
            offerViewHolder.offerImage.setImageBitmap(resizeImage(Uri.parse(offer.getImage()), context));
        } catch (final IOException e) {
            Log.e(TAG, " Error while loading image: " + e.getMessage());
        }
        offerViewHolder.offerFrom.setText(context.getString(R.string.label_offer_from, new DateTime(offer.getValidFrom()).toLocalDate().toString()));
        offerViewHolder.offerTo.setText(context.getString(R.string.label_offer_to, new DateTime(offer.getValidTo()).toLocalDate().toString()));
        offerViewHolder.offerPrice.setText(context.getString(R.string.label_offer_price, offer.getPrice()));
    }

    /**
     * Called when view holder is created
     *
     * @param viewGroup the view the holder is shown in
     * @param position  the item position
     * @return the holder of the view
     */
    @Override
    public OfferViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int position) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_item, viewGroup, false);

        return new OfferViewHolder(itemView);
    }

    /**
     * Called once the adapter is attached to the view
     *
     * @param recyclerView the recycler view
     */
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);
        }

        /**
         * Handle item clicks
         *
         * @param view the view containing the view holders
         */
        @Override
        public void onClick(final View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getAdapterPosition());
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
