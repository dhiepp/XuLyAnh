package com.dhiep.android.xulyanh.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhiep.android.xulyanh.EditActivity;
import com.dhiep.android.xulyanh.R;

public class FilterFragment extends Fragment {
    private EditActivity editActivity;

    public FilterFragment(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

}
