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

public class RotateFragment extends Fragment implements View.OnClickListener {
    private EditActivity editActivity;
    private Bitmap editBitmap;

    private Button rotateLeft;
    private Button rotateRight;
    private Button flipH;
    private Button flipV;

    public RotateFragment(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rotate, container, false);

        rotateLeft = root.findViewById(R.id.rotate_left);
        rotateRight = root.findViewById(R.id.rotate_right);
        flipH = root.findViewById(R.id.flip_horizontally);
        flipV = root.findViewById(R.id.flip_vertically);

        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        flipH.setOnClickListener(this);
        flipV.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        editBitmap = editActivity.getBitmap();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(rotateLeft)) {
            editBitmap = BitmapProcessing.rotate(editBitmap,-90);
            editActivity.setBitmap(editBitmap);
        }
        if (v.equals(rotateRight)) {
            editBitmap = BitmapProcessing.rotate(editBitmap,90);
            editActivity.setBitmap(editBitmap);
        }
        if (v.equals(flipH)) {
            editBitmap = BitmapProcessing.flip(editBitmap, true, false);
            editActivity.setBitmap(editBitmap);
        }
        if (v.equals(flipV)) {
            editBitmap = BitmapProcessing.flip(editBitmap, false, true);
            editActivity.setBitmap(editBitmap);
        }
    }
}
