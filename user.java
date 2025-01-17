package com.example.gym;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class user extends AppCompatActivity {
EditText name,weight;
    RadioButton male,female;
    CheckBox LW,BM,SF;
    Spinner favorite;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        male=findViewById(R.id.rbMale);
        female=findViewById(R.id.rbFemale);
        LW=findViewById(R.id.chlw);
       BM=findViewById(R.id.chbm);
        SF=findViewById(R.id.chsf);

        favorite=findViewById(R.id.spSpinner);


    }

    public void done(View view) {
        Toast.makeText(user.this, "Your information is added", Toast.LENGTH_LONG).show();
    }
}