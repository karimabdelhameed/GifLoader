package com.karim.gifloader;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.karim.gifloader.helpers.GifLoader;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView gifImageView,gifIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifImageView = findViewById(R.id.gif_imageView);
        gifIcon = findViewById(R.id.gif_icon);
        handleGif("https://i.pinimg.com/originals/d5/44/ff/d544ffca4ecb461fc19da7e384cbc6d5.gif");
    }


    private void handleGif(String mediaUrl) {
        final GifLoader gifLoader = new GifLoader(mediaUrl,
                getResources(), gifImageView, gifIcon);
        gifIcon.setVisibility(View.VISIBLE);
        Thread thread = new Thread(gifLoader);
        thread.start();
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gifLoader.controlGif();
            }
        });
    }
}