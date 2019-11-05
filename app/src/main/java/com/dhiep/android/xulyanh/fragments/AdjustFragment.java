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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dhiep.android.xulyanh.EditActivity;
import com.dhiep.android.xulyanh.R;
import com.dhiep.android.xulyanh.bitmap.BitmapProcessing;

public class AdjustFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private EditActivity editActivity;
    private Bitmap editBitmap;

    private Button saveBtn;
    private TextView bTxt;
    private TextView cTxt;
    private TextView sTxt;
    private SeekBar brightness;
    private SeekBar contrast;
    private SeekBar saturation;

    private int brightnessValue = 0;
    private int contrastValue = 0;
    private int saturationValue = 100;

    public AdjustFragment(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_adjust, container, false);

        saveBtn = root.findViewById(R.id.adjust_save);
        bTxt = root.findViewById(R.id.adjust_txt1);
        cTxt = root.findViewById(R.id.adjust_txt2);
        sTxt = root.findViewById(R.id.adjust_txt3);
        brightness = root.findViewById(R.id.adjust_brightness);
        contrast = root.findViewById(R.id.adjust_contrast);
        saturation = root.findViewById(R.id.adjust_saturation);

        saveBtn.setOnClickListener(this);
        brightness.setOnSeekBarChangeListener(this);
        contrast.setOnSeekBarChangeListener(this);
        saturation.setOnSeekBarChangeListener(this);

        return root;
    }

    public void reset() {
        brightness.setProgress(200);
        contrast.setProgress(100);
        saturation.setProgress(100);

        brightnessValue = 0;
        contrastValue = 0;
        saturationValue = 100;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(saveBtn) && editBitmap!=null) {
            saveBtn.setText("Đã lưu");
            editActivity.setBitmap(editBitmap);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.equals(brightness)) {
            int value = brightness.getProgress();
            int adj = (value/2) - 100;
            bTxt.setText("Độ sáng: " + (adj>0?"+":"") + adj);

            //Range: [0, 400] -> [-200, 200]
            brightnessValue = value - 200;
        }
        if (seekBar.equals(contrast)) {
            int value = contrast.getProgress();
            int adj = value - 100;
            cTxt.setText("Độ tương phản: " + (adj>0?"+":"") + adj);

            //Range: [0, 200] -> [-100, 100]
            contrastValue = value - 100;
        }
        if (seekBar.equals(saturation)) {
            int value = saturation.getProgress();
            int adj = value - 100;
            sTxt.setText("Độ bão hòa: " + (adj>0?"+":"") + adj);

            //Range: [0, 200] -> [0, 200]
            saturationValue = value;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        editBitmap = editActivity.getBitmapCopy();
        editBitmap = BitmapProcessing.brightness(editBitmap, brightnessValue);
        editBitmap = BitmapProcessing.contrast(editBitmap, contrastValue);
        editBitmap = BitmapProcessing.saturation(editBitmap, saturationValue);
        editActivity.showBitmap(editBitmap);
        saveBtn.setText("Chưa lưu...");
    }
}
