package com.hska.ebusiness.toolbar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.activities.EditOfferActivity;
import com.hska.ebusiness.toolbar.activities.ShowOfferActivity;
import com.hska.ebusiness.toolbar.model.Offer;
import com.hska.ebusiness.toolbar.util.ToolbarConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.joda.time.DateTime;

public class OfferListFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    static List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
    static SimpleAdapter listViewAdapter;
    private static View rootView;
    List<Offer> offerList;

    /**
     *  Used to initialize the Fragment ListFragment
     *
     * @param inflater to inject the Fragment
     * @param container contains the Fragment
     * @param savedInstanceState bundle with data for re-initialization
     * @return returns the View (fragment)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.offerlist_view, container, false);
        }
        catch (InflateException e) {
            Log.e(TAG, "Error while initializing rootView for ListFragment: " + e.getMessage());
        }

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] from = { "flag","price","name","detail","date" };

        // Ids of views in listview_layout
        final int[] to = { R.id.flag, R.id.price,R.id.name,R.id.detail,R.id.date};

        final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        offerList = getArguments().getParcelableArrayList("offers");

        aList.clear();

        for (Offer offer: offerList) {

            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name",offer.getName());
            hm.put("price", offer.getPrice() + "€");
            hm.put("detail",offer.getDescription());
            hm.put("flag", Integer.toString(R.drawable.ic_home_black_24dp));

            String time_from = df.format(new DateTime(offer.getValidFrom()).toDate());
            String time_to = df.format(new DateTime(offer.getValidTo()).toDate());
            String from_to= "From: " + time_from + "   " + "To: " + time_to;

            hm.put("date", from_to);
            hm.put("id", offer.getId() + "");
            aList.add(hm);
            Log.d(TAG, ": " + offer.getName() + " - Added to ListFragment");
        }

        listViewAdapter = new SimpleAdapter(getActivity(), aList, R.layout.list_item, from, to);

        final ListView listView = ( ListView ) getActivity().findViewById(R.id.listview);

        // Setting the adapter to the listView
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                v.setLongClickable(false);

                HashMap<String, String> selectedItem = (HashMap<String, String>) listView.getItemAtPosition(position);

                String selectedItemID = selectedItem.get("id");
                final Offer selectedOfferItem = getOfferById(Integer.parseInt(selectedItemID));

                Intent intent = new Intent(getContext(), ShowOfferActivity.class);
                intent.putExtra(ToolbarConstants.TOOLBAR_OFFER, selectedOfferItem);

                startActivity(intent);
            }
        });

    }

    /**
     *Search for offers by ID.
     * @param id the selected
     * @return returns an offer or null
     */
    private Offer getOfferById(long id){
        for (Offer offer: offerList ) {
            if(offer.getId() == id)
                return offer;
        }
        return null;
    }
}
