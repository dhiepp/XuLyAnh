package com.dhiep.android.xulyanh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.dhiep.android.xulyanh.bitmap.BitmapProcessing;
import com.dhiep.android.xulyanh.custom.WrapContentViewPager;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {
    private Bitmap imageBitmap;
    private Bitmap changedBitmap;

    private ImageView imageView;
    private WrapContentViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);


        imageView = findViewById(R.id.imageView);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        //Load ảnh đã chọn
        try {
            Uri imageUri = getIntent().getData();
            System.out.println(imageUri.getPath());
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            imageBitmap = BitmapFactory.decodeStream(imageStream);
            changedBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
            imageView.setImageBitmap(imageBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Đã có lỗi xảy ra!", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getOriginalBitmap() {
        return imageBitmap.copy(imageBitmap.getConfig(), true);
    }

    public Bitmap getBitmap() {
        return changedBitmap;
    }

    public void resetBitmap() {
        changedBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
        imageView.setImageBitmap(imageBitmap);
    }

    public void showBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        changedBitmap = bitmap.copy(bitmap.getConfig(), true);
        imageView.setImageBitmap(bitmap);
    }
}
