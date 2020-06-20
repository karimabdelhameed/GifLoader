package com.karim.gifloader.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.karim.gifloader.utils.GifDecoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by karim on 10,June,2020
 */
public class GifLoader implements Runnable {
    private ImageView imageView,gifIcon;
    private AnimationDrawable animationDrawable = new AnimationDrawable();
    private String gifURL;
    private Resources resources;

    public GifLoader(String gifURL, Resources resources,
                     ImageView imageView, ImageView gifIcon) {
        this.gifURL = gifURL;
        this.resources = resources;
        this.imageView = imageView;
        this.gifIcon = gifIcon;
    }

    public void controlGif() {
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
            gifIcon.setVisibility(View.VISIBLE);
        }
        else{
            animationDrawable.start();
            gifIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL(gifURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URLConnection conn = null;
        try {
            if (url != null) {
                conn = url.openConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            try (InputStream inputStream = conn.getInputStream()) {
                int n = 0;
                byte[] buffer = new byte[1024];
                while (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                final GifDecoder gifDecoder = new GifDecoder();
                gifDecoder.read(output.toByteArray());//data is a byte array
                final int frameCount = gifDecoder.getFrameCount();
                for (int i = 0; i < frameCount; i++) {
                    gifDecoder.advance();
                    Bitmap bitmap = Bitmap.createBitmap(gifDecoder.getNextFrame());
                    int delay = gifDecoder.getNextDelay();
                    animationDrawable.addFrame(new BitmapDrawable(resources, bitmap), delay);
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setBackground(animationDrawable);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}