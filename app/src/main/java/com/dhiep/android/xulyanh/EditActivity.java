package com.dhiep.android.xulyanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.dhiep.android.xulyanh.bitmap.BitmapProcessing;
import com.dhiep.android.xulyanh.bitmap.BitmapWriter;
import com.dhiep.android.xulyanh.custom.WrapContentViewPager;
import com.dhiep.android.xulyanh.fragments.AdjustFragment;
import com.dhiep.android.xulyanh.fragments.FilterFragment;
import com.dhiep.android.xulyanh.fragments.RotateFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    private Bitmap imageBitmap;
    private Bitmap changedBitmap;

    MainFragmentPagerAdapter adapter;

    private ImageView imageView;
    private WrapContentViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        imageView = findViewById(R.id.imageView);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.addFragment(new RotateFragment(this), "Xoay ảnh");
        adapter.addFragment(new AdjustFragment(this), "Điều chỉnh");
        adapter.addFragment(new FilterFragment(this), "Bộ lọc");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_undo) {
            new AlertDialog.Builder(this).setTitle("Hoàn tác")
                    .setMessage("Bạn có muốn hủy bỏ mọi chỉnh sửa của mình không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetBitmap();
                            adapter.resetAdjustments();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }
        if (id == R.id.edit_save) {
            try {
                BitmapWriter writer = new BitmapWriter(createImageFile(), changedBitmap, this);
                writer.execute();
            } catch (IOException ex) {
                new AlertDialog.Builder(this).setTitle("Đã có lỗi xảy ra khi lưu ảnh!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void writeBitmapCompleted() {
        new AlertDialog.Builder(this).setTitle("Đã lưu ảnh thành công!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public Bitmap getOriginalBitmap() {
        return imageBitmap.copy(imageBitmap.getConfig(), true);
    }

    public Bitmap getBitmap() {
        return changedBitmap;
    }

    public Bitmap getBitmapCopy() {
        return changedBitmap.copy(changedBitmap.getConfig(), true);
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

    @SuppressWarnings("deprecation")
    private File createImageFile() throws IOException {
        //Đặt tên ảnh theo thời gian
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(path, "XuLyAnh");
        try {
            storageDir.mkdirs();
        } catch(Exception e) {}

        File image = new File(storageDir, imageFileName + ".jpg");

        System.out.println(image.getAbsolutePath());
        return image;
    }
}
