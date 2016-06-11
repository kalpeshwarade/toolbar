package com.hska.ebusiness.toolbar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hska.ebusiness.toolbar.R;
import com.hska.ebusiness.toolbar.activities.ShowOfferActivity;
import com.hska.ebusiness.toolbar.adapter.OfferAdapter;
import com.hska.ebusiness.toolbar.model.Offer;

import java.util.List;

import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER;
import static com.hska.ebusiness.toolbar.util.ToolbarConstants.TOOLBAR_OFFER_LIST;

public class OfferListFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * Used to initialize the Fragment ListFragment
     *
     * @param inflater           to inject the Fragment
     * @param container          contains the Fragment
     * @param savedInstanceState bundle with data for re-initialization
     * @return returns the view (fragment)
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        Log.d(TAG, " create view");

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        final View rootView = inflater.inflate(R.layout.fragment_offer_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        final List<Offer> offerList = getArguments().getParcelableArrayList(TOOLBAR_OFFER_LIST);

        if (offerList != null && !offerList.isEmpty()) {
            final OfferAdapter offerAdapter = new OfferAdapter(offerList, this.getContext());
            recyclerView.setAdapter(offerAdapter);

            offerAdapter.setOnItemClickListener(new OfferAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(final View view, final int position) {
                    final Intent showOfferIntent = new Intent(getActivity(), ShowOfferActivity.class);
                    showOfferIntent.putExtra(TOOLBAR_OFFER, offerList.get(position));
                    startActivity(showOfferIntent);
                }
            });
        }

        return recyclerView;
    }
}
