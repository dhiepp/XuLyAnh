package com.dhiep.android.xulyanh.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dhiep.android.xulyanh.EditActivity;
import com.dhiep.android.xulyanh.R;
import com.dhiep.android.xulyanh.bitmap.BitmapProcessing;

public class FilterFragment extends Fragment implements View.OnClickListener{

    private EditActivity editActivity;
    private Bitmap editBitmap;

    private Button gaussian;
    private Button grayScale;
    private Button invert;
    private Button noise;
    private Button sepia;
    private Button sharpen;
    private Button vignette;


    public FilterFragment(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_filter, container, false);

        gaussian = root.findViewById(R.id.filter_gaussian);
        grayScale = root.findViewById(R.id.filter_grayScale);
        invert = root.findViewById(R.id.filter_invert);
        noise = root.findViewById(R.id.filter_noise);
        sepia = root.findViewById(R.id.filter_sepia);
        sharpen = root.findViewById(R.id.filter_sharpen);
        vignette = root.findViewById(R.id.filter_vignette);

        gaussian.setOnClickListener(this);
        grayScale.setOnClickListener(this);
        invert.setOnClickListener(this);
        noise.setOnClickListener(this);
        sepia.setOnClickListener(this);
        sharpen.setOnClickListener(this);
        vignette.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        editBitmap = editActivity.getBitmap();
    }


    @Override
    public void onClick(View view) {
       if(view.equals(gaussian)){
           editBitmap = BitmapProcessing.gaussian(editBitmap);
           editActivity.setBitmap(editBitmap);
       }
        if(view.equals(grayScale)){
            editBitmap = BitmapProcessing.grayscale(editBitmap);
            editActivity.setBitmap(editBitmap);
        }
        if(view.equals(invert)){
            editBitmap = BitmapProcessing.invert(editBitmap);
            editActivity.setBitmap(editBitmap);
        }
        if(view.equals(noise)){
            editBitmap = BitmapProcessing.noise(editBitmap);
            editActivity.setBitmap(editBitmap);
        }
        if(view.equals(sepia)){
            editBitmap = BitmapProcessing.sepia(editBitmap);
            editActivity.setBitmap(editBitmap);
        }
        if(view.equals(sharpen)){
            editBitmap = BitmapProcessing.sharpen(editBitmap);
            editActivity.setBitmap(editBitmap);
        }
        if(view.equals(vignette)){
            editBitmap = BitmapProcessing.vignette(editBitmap);
            editActivity.setBitmap(editBitmap);
        }

    }
}
