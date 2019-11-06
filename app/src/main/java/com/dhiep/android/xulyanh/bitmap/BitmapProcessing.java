package com.dhiep.android.xulyanh.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ExifInterface;

import java.io.IOException;
import java.util.Random;

public class BitmapProcessing {

	// [-360, +360] -> Default = 0
	public static Bitmap rotate(Bitmap bitmap, float degrees) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
		Matrix matrix = new Matrix();
		matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public static Bitmap gaussian(Bitmap src) {
		double[][] GaussianBlurConfig = new double[][] {
				{ 1, 2, 1 },
				{ 2, 4, 2 },
				{ 1, 2, 1 }
		};
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(GaussianBlurConfig);
		convMatrix.Factor = 16;
		convMatrix.Offset = 0;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}

	public static Bitmap sharpen(Bitmap src) {
		double[][] SharpConfig = new double[][] {
				{ 0 , -2    , 0  },
				{ -2, 11, -2 },
				{ 0 , -2    , 0  }
		};
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
		convMatrix.applyConfig(SharpConfig);
		convMatrix.Factor = 3;
		return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}

	public static Bitmap noise(Bitmap source) {
		final int COLOR_MAX = 0xFF;

		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);
		// a random object
		Random random = new Random();

		int index = 0;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// get random color
				int randColor = Color.rgb(random.nextInt(COLOR_MAX),
						random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
				// OR
				pixels[index] |= randColor;
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);

		source.recycle();
		source = null;

		return bmOut;
	}

	// [-255, +255] -> Default = 0
	public static Bitmap brightness(Bitmap src, int value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				// increase/decrease each channel
				R += value;
				if(R > 255) { R = 255; }
				else if(R < 0) { R = 0; }

				G += value;
				if(G > 255) { G = 255; }
				else if(G < 0) { G = 0; }

				B += value;
				if(B > 255) { B = 255; }
				else if(B < 0) { B = 0; }

				// apply new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		src.recycle();
		src = null;

		return bmOut;
	}

	public static Bitmap sepia(Bitmap src) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// constant grayscale
		final double GS_RED = 0.3;
		final double GS_GREEN = 0.59;
		final double GS_BLUE = 0.11;
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				// get color on each channel
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// apply grayscale sample
				B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

				// apply intensity level for sepid-toning on each channel
				R += 110;
				if(R > 255) { R = 255; }

				G += 65;
				if(G > 255) { G = 255; }

				B += 20;
				if(B > 255) { B = 255; }

				// set new pixel color to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		src.recycle();
		src = null;

		return bmOut;
	}

	// [-100, +100] -> Default = 0
	public static Bitmap contrast(Bitmap src, double value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap

		// create a mutable empty bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		// create a canvas so that we can draw the bmOut Bitmap from source bitmap
		Canvas c = new Canvas();
		c.setBitmap(bmOut);

		// draw bitmap to bmOut from src bitmap so we can modify it
		c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


		// color information
		int A, R, G, B;
		int pixel;
		// get contrast value
		double contrast = Math.pow((100 + value) / 100, 2);

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// apply filter contrast for every channel R, G, B
				R = Color.red(pixel);
				R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if(R < 0) { R = 0; }
				else if(R > 255) { R = 255; }

				G = Color.green(pixel);
				G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if(G < 0) { G = 0; }
				else if(G > 255) { G = 255; }

				B = Color.blue(pixel);
				B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if(B < 0) { B = 0; }
				else if(B > 255) { B = 255; }

				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		src.recycle();
		src = null;
		return bmOut;
	}

	// [0, 200] -> Default = 100
	public static Bitmap saturation(Bitmap src, int value) {
		float f_value = (float) (value / 100.0);

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap bitmapResult =
				Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvasResult = new Canvas(bitmapResult);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(f_value);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
		paint.setColorFilter(filter);
		canvasResult.drawBitmap(src, 0, 0, paint);

		src.recycle();
		src = null;

		return bitmapResult;
	}

	public static Bitmap grayscale(Bitmap src) {
		//Array to generate Gray-Scale image
		float[] GrayArray = {
				0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
				0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
				0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f,  
		};

		ColorMatrix colorMatrixGray = new ColorMatrix(GrayArray);

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap bitmapResult = Bitmap
				.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvasResult = new Canvas(bitmapResult);
		Paint paint = new Paint();

		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrixGray);
		paint.setColorFilter(filter);
		canvasResult.drawBitmap(src, 0, 0, paint);

		src.recycle();
		src = null;

		return bitmapResult;
	}
	
	public static Bitmap vignette(Bitmap image) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		
	    float radius = (float) (width/1.2);
	    int[] colors = new int[] { 0, 0x55000000, 0xff000000 };
        float[] positions = new float[] { 0.0f, 0.5f, 1.0f };
	    
	    RadialGradient gradient = new RadialGradient(width / 2, height / 2, radius, colors, positions, Shader.TileMode.CLAMP);
	    
	    //RadialGradient gradient = new RadialGradient(width / 2, height / 2, radius, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);

	    Canvas canvas = new Canvas(image);
	    canvas.drawARGB(1, 0, 0, 0);

	    final Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(Color.BLACK);
	    paint.setShader(gradient);

	    final Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
	    final RectF rectf = new RectF(rect);

	    canvas.drawRect(rectf, paint);
	    
	    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    	canvas.drawBitmap(image, rect, rect, paint);
	    
	    return image;
	}

	public static Bitmap invert(Bitmap src) {
		Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		int A, R, G, B;
		int pixelColor;
		int height = src.getHeight();
		int width = src.getWidth();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixelColor = src.getPixel(x, y);
				A = Color.alpha(pixelColor);

				R = 255 - Color.red(pixelColor);
				G = 255 - Color.green(pixelColor);
				B = 255 - Color.blue(pixelColor);

				output.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		src.recycle();
		src = null;

		return output;
	}
}
