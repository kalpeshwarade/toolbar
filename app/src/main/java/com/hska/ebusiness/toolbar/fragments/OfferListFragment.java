package com.hska.ebusiness.toolbar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private static final String TAG = "ListViewFragment";
    static List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
    static SimpleAdapter listViewAdapter;
    int positionElement;
    private static int MENU_ADD = 1;
    private static int MENU_DETAILS = 2;
    private static View rootView;

    ListView listView;
    List<Offer> offerList;

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
        }

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] from = { "flag","price","name","detail","date" };

        // Ids of views in listview_layout
        int[] to = { R.id.flag, R.id.price,R.id.name,R.id.detail,R.id.date};

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        offerList = getArguments().getParcelableArrayList("offers");

        aList.clear();

        for (Offer offer: offerList) {

            Date f = new DateTime(offer.getValidFrom()).toDate();
            Date t = new DateTime(offer.getValidTo()).toDate();

            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name",offer.getName());
            hm.put("price", offer.getPrice() + "€");
            hm.put("detail",offer.getDescription());
            hm.put("flag", Integer.toString(R.drawable.ic_home_black_24dp));

            String time_from = df.format(f);
            String time_to = df.format(t);
            String from_to= "From: " + time_from + "   " + "To: " + time_to;

            hm.put("date", from_to);
            hm.put("id", offer.getId() + "");
            aList.add(hm);
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
                Offer selectedOfferItem = getOfferById(Integer.parseInt(selectedItemID));

                Intent intent = new Intent(getContext(), ShowOfferActivity.class);
                intent.putExtra(ToolbarConstants.TOOLBAR_OFFER, selectedOfferItem);

                startActivity(intent);
            }
        });

    }

    private Offer getOfferById(long id){
        for (Offer offer: offerList ) {
            if(offer.getId() == id)
                return offer;
        }
        return null;
    }
    //floting Action Button

    //Listner

    //Angebot klick --> Intent mitgeben

}
