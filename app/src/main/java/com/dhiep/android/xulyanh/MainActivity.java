package com.dhiep.android.xulyanh;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.widget.ImageViewCompat;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final int SELECT_IMAGE_REQUEST = 1;
    private final int CAPTURE_IMAGE_REQUEST = 2;

    private ConstraintLayout selectBtn;
    private ConstraintLayout captureBtn;

    private Uri capturedImageUri;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectBtn = findViewById(R.id.main_select);
        captureBtn = findViewById(R.id.main_capture);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tạo Intent với hành động chọn file
                Intent intent = new Intent(Intent.ACTION_PICK);
                //Chọn kiểu file là ảnh
                intent.setType("image/*");
                //Mở Activity sửa ảnh
                startActivityForResult(intent, SELECT_IMAGE_REQUEST);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_REQUEST) {
                Uri imageUri = data.getData();
                Intent editImage = new Intent(Intent.ACTION_VIEW, imageUri, this, EditActivity.class);
                editImage.putExtra("path", selectedImagePath);
                startActivity(editImage);
            }
            if (requestCode == CAPTURE_IMAGE_REQUEST) {
                if (capturedImageUri != null) {
                    Intent editImage = new Intent(Intent.ACTION_VIEW, capturedImageUri, this, EditActivity.class);
                    editImage.putExtra("path", selectedImagePath);
                    startActivity(editImage);
                }
            }
        } else {
            Toast.makeText(this, "Bạn chưa chọn ảnh!", Toast.LENGTH_LONG).show();
        }
    }

//
//

    private void captureImage() {
        //Tạo Intent với hành động chụp ảnh
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Mở Activity chụp ảnh
        if (intent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.dhiep.android.xulyanh.fileprovider", imageFile);
                capturedImageUri = imageUri;

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        //Đặt tên ảnh theo thời gian
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(path, "XuLyAnh");
        try {
            storageDir.mkdirs();
        } catch(Exception e) {}

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        System.out.println(image.getAbsolutePath());
        return image;
    }
}
