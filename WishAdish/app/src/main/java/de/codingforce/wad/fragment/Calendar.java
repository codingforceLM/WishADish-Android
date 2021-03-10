package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.codeingforce.wad.R;
import de.codingforce.wad.fragment.NameAwareFragment;

public class Calendar extends NameAwareFragment {
    private static final String LOG_TAG = "Calender";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_calender, parent, false);
    }
}
