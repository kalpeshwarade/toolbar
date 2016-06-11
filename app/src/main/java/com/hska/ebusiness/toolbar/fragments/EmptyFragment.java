package com.hska.ebusiness.toolbar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hska.ebusiness.toolbar.R;

public class EmptyFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        Log.d(TAG, " create view");

        return inflater.inflate(R.layout.fragment_empty, container, false);
    }
}
