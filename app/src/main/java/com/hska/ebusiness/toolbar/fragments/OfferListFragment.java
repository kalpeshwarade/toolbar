package com.hska.ebusiness.toolbar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.activities.EditOfferActivity;
import com.hska.ebusiness.toolbar.activities.ShowOfferActivity;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OfferListFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private static List<HashMap<String,String>> aList = new ArrayList<>();
    private static View rootView;

    private List<Offer> offerList;

    /**
     * Used to initialize the Fragment ListFragment
     *
     * @param inflater to inject the Fragment
     * @param container contains the Fragment
     * @param savedInstanceState bundle with data for re-initialization
     * @return returns the view (fragment)
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (rootView != null) {
            final ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }

        try {
            rootView = inflater.inflate(R.layout.fragment_offer_list, container, false);
        }
        catch (final InflateException e) {
            Log.e(TAG, "Error while initializing rootView for ListFragment: " + e.getMessage());
        }

        return rootView;
    }

    /**
     *
     * @param savedInstanceState to restore instance state
     */
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] from = {"flag", "price", "name", "detail", "date"};
        final int[] to = {R.id.flag, R.id.price, R.id.name, R.id.detail, R.id.date};

        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        offerList = getArguments().getParcelableArrayList("offers");

        aList.clear();

        for (final Offer offer: offerList) {
            final HashMap<String, String> hm = new HashMap<>();

            hm.put("name",offer.getName());
            hm.put("price", offer.getPrice() + "€");
            hm.put("detail", offer.getDescription());
            hm.put("flag", Integer.toString(R.drawable.ic_home_black_24dp));

            final String time_from = dateFormat.format(new DateTime(offer.getValidFrom()).toDate());
            final String time_to = dateFormat.format(new DateTime(offer.getValidTo()).toDate());
            final String from_to= "From: " + time_from + "   " + "To: " + time_to;

            hm.put("date", from_to);
            hm.put("id", offer.getId() + "");

            aList.add(hm);

            Log.d(TAG, ": " + offer.getName() + " - Added to ListFragment");
        }

        final SimpleAdapter listViewAdapter = new SimpleAdapter(getActivity(), aList, R.layout.list_item, from, to);

        final ListView listView = (ListView) getActivity().findViewById(R.id.listview);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, final View v,
                                    final int position, final long id) {

                v.setLongClickable(false);

                final HashMap<String, String> selectedItem = (HashMap<String, String>) listView.getItemAtPosition(position);

                final String selectedItemID = selectedItem.get("id");
                final Offer selectedOfferItem = getOfferById(Integer.parseInt(selectedItemID));

                final Intent intent = new Intent(getContext(), ShowOfferActivity.class);
                intent.putExtra(ToolbarConstants.TOOLBAR_OFFER, selectedOfferItem);

                startActivity(intent);
            }
        });

        final FloatingActionButton buttonNewOffer = (FloatingActionButton) getView().findViewById(R.id.add_offer_floating_button);
        buttonNewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent newOfferIntent = new Intent(getContext(), EditOfferActivity.class);
                newOfferIntent.putExtra(ToolbarConstants.TOOLBAR_OFFER_IS_EDIT_MODE, false);
                startActivity(newOfferIntent);
            }
        });

    }

    /**
     * Search for offers by ID.
     * @param id the selected
     * @return returns an offer or null
     */
    private Offer getOfferById(final long id){
        for (final Offer offer: offerList) {
            if (offer.getId() == id)
                return offer;
        }
        return null;
    }
}
