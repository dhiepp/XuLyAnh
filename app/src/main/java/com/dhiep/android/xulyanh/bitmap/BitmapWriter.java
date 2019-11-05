package com.dhiep.android.xulyanh.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;

import com.dhiep.android.xulyanh.EditActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapWriter extends AsyncTask<Void, Void, Boolean> {

	private final int BUFFER_SIZE = 1024 * 10;
	private File file;
	private Bitmap bitmap;

	private EditActivity editActivity;

	public BitmapWriter(File input_file, Bitmap input_bitmap, EditActivity editActivity) {
		file = input_file;
		bitmap = input_bitmap;
		this.editActivity = editActivity;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, true);
			final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		try {
			bitmap.recycle();
			bitmap = null;
		} catch (Exception e) {}

		editActivity.writeBitmapCompleted();
	}
}