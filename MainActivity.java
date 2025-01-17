package com.example.gym;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button go;
    TextView welcome;
        VideoView video;

        MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        video=findViewById(R.id.vvShow);

        mc=new MediaController(this);
        Uri ur1= Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.videowelcom);
        video.setVideoURI(ur1);
        video.setMediaController(mc);
        video.start();
    }

    public void letsgo(View view) {
        Intent  i = new Intent(this, dashboard.class);
        startActivity(i);
    }
}