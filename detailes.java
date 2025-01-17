package com.example.gym;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class detailes extends AppCompatActivity {
TextView details;
String[]  workoutsDetails ={"I. Upper Body Workout \n" +
        "1. Push-Ups – 3 sets of 10–15 reps  \n" +
        "2. Dumbbell Shoulder Press – 3 sets of 8–12 reps  \n" +
        "3. Dumbbell Rows (One Arm) – 3 sets of 10–12 per arm  \n" +
        "4. Bicep Curls – 3 sets of 12–15 reps  \n" +
        "5. Triceps Dips (Chair or Bench)– 3 sets of 12–15 reps  \n" +
        "6. Rest 60 seconds between sets, focusing on proper form.  \n",
        "II. Lower Body Workout \n" +
                "1.Bodyweight Squats – 3 sets of 10–12 reps  \n" +
                "2.Glute Bridges– 3 sets of 10–15 reps  \n" +
                "3. Lunges– 3 sets of 10 reps per leg  \n" +
                "4. Calf Raises – 3 sets of 15 reps  \n" +
                "5. Step-Ups – 3 sets of 10 reps per leg  \n" +
                "6. Rest 60 seconds between sets, focus on balance and depth.  \n",
        "\n" +
                "III. Pilates Routine \n" +
                "1. The Hundred – 1 set (10 breaths)  \n" +
                "2. Single-Leg Stretch – 2 sets of 10 reps  \n" +
                "3. Double-Leg Stretch – 2 sets of 8 reps  \n" +
                "4. Glute Bridges – 3 sets of 10 reps  \n" +
                "5. Plank Hold – 2 sets of 20–30 seconds  \n" +
                "6. Stretch at the end (Child’s Pose, Butterfly Stretch).  \n",
        "IIIV. Boxing Routine  \n" +
                "1. Warm-Up – 5 minutes (jump rope, shadow boxing)  \n" +
                "2. Jab-Cross Combos – 3 sets of 1 minute  \n" +
                "3. Hooks and Uppercuts – 3 sets of 1 minute  \n" +
                "4. Heavy Bag/Shadow Boxing – 3 rounds of 3 minutes  \n" +
                "5. Core (Plank Punches or Russian Twists)– 3 sets of 20 reps  \n" +
                "6. Cool down with stretching (shoulders, hips, legs).  \n"

        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        details=findViewById(R.id.tvdetailes);

        Intent p=getIntent();
        int i=p.getIntExtra("INDEX",0);
        details.setText(workoutsDetails[i]);
    }
}