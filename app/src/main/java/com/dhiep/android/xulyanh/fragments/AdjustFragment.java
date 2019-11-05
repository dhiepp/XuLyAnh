package com.dhiep.android.xulyanh.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.media.MediaBrowserCompatUtils;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dhiep.android.xulyanh.EditActivity;
import com.dhiep.android.xulyanh.R;
import com.dhiep.android.xulyanh.bitmap.BitmapProcessing;

public class AdjustFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private EditActivity editActivity;

    private TextView bTxt;
    private TextView cTxt;
    private TextView sTxt;
    private SeekBar brightness;
    private SeekBar contrast;
    private SeekBar saturation;

    public AdjustFragment(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_adjust, container, false);

        bTxt = root.findViewById(R.id.adjust_txt1);
        cTxt = root.findViewById(R.id.adjust_txt2);
        sTxt = root.findViewById(R.id.adjust_txt3);
        brightness = root.findViewById(R.id.adjust_brightness);
        contrast = root.findViewById(R.id.adjust_contrast);
        saturation = root.findViewById(R.id.adjust_saturation);

        brightness.setOnSeekBarChangeListener(this);
        contrast.setOnSeekBarChangeListener(this);
        saturation.setOnSeekBarChangeListener(this);

        return root;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.equals(brightness)) {
            int value = brightness.getProgress();
            int adj = (value/2) - 100;
            bTxt.setText("Độ sáng: " + (adj>0?"+":"") + adj);
        }
        if (seekBar.equals(contrast)) {
            int value = contrast.getProgress();
            int adj = value - 100;
            cTxt.setText("Độ tương phản: " + (adj>0?"+":"") + adj);
        }
        if (seekBar.equals(saturation)) {
            int value = saturation.getProgress();
            int adj = value - 100;
            sTxt.setText("Độ bão hòa: " + (adj>0?"+":"") + adj);

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.equals(brightness)) {
            int value = brightness.getProgress();

            Bitmap bitmap = editActivity.getBitmap();
            editActivity.setBitmap(BitmapProcessing.brightness(bitmap, value-100));
        }
        if (seekBar.equals(contrast)) {
            int value = contrast.getProgress();

            Bitmap bitmap = editActivity.getBitmap();
            editActivity.setBitmap(BitmapProcessing.contrast(bitmap, value-100));
        }
        if (seekBar.equals(saturation)) {
            int value = saturation.getProgress();

            Bitmap bitmap = editActivity.getBitmap();
            editActivity.setBitmap(BitmapProcessing.saturation(bitmap, value));
        }
    }
}
