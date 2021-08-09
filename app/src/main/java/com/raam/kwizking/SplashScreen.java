package com.raam.kwizking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3500;

    ImageView imageView;
    TextView textView;
    Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen_activity);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);

        imageView.setAnimation(top);
        textView.setAnimation(bottom);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
}