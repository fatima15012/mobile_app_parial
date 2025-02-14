package com.example.final_project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

     SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "GroceryAppPrefs";
    private static final String KEY_USER_NAME = "userName";
     DatabaseReference databaseReference;
     RecyclerView recyclerView;
     Button buttonAdd;
    Button buttonMarkAll;
    Button buttonClearAll;
     GroceryAdapter adapter;
     List<GroceryItem> groceryList;
     Spinner spinnerCategory;
     TextView textViewWelcome;
     TextView textViewMessage;
     TextView textViewTotalItems;
    private boolean isManualAdd = false;


    @SuppressLint("MissingInflatedId")
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
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewMessage = findViewById(R.id.textViewMessage);
        textViewTotalItems = findViewById(R.id.textViewTotalItems);


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userName = sharedPreferences.getString(KEY_USER_NAME, null);

        if (userName == null) {
            showNameInputDialog();
        } else {
            textViewWelcome.setText("Welcome, " + userName + "!");
            textViewMessage.setText("Add the items you need to the list:");

        }

        databaseReference = FirebaseDatabase.getInstance().getReference("groceryItems");


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groceryList = new ArrayList<>();

        adapter = new GroceryAdapter(groceryList, databaseReference, this);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCategory.setAdapter(adapter1);

        recyclerView.setAdapter(adapter);

        loadData();

        findViewById(R.id.buttonAdd).setOnClickListener(v -> {
            String itemName = ((EditText) findViewById(R.id.editTextItem)).getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (!itemName.isEmpty()) {
                String id = databaseReference.push().getKey();
                GroceryItem item = new GroceryItem(id, itemName, false,category);
                isManualAdd = true;


                databaseReference.child(id).setValue(item);
                groceryList.add(item);
                adapter.notifyItemInserted(groceryList.size() - 1);

                updateTotalItemsCounter();
                ((EditText) findViewById(R.id.editTextItem)).setText("");

            }else{
                Toast.makeText(MainActivity.this, "Please enter an item name", Toast.LENGTH_SHORT).show();

            }

        });
        findViewById(R.id.buttonMarkAll).setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Mark All as Purchased");
            builder.setMessage("Are you sure you want to mark all items as purchased?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                for (GroceryItem item : groceryList) {
                    item.setPurchased(true);
                    databaseReference.child(item.getId()).child("purchased").setValue(true);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "All items marked as purchased", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        findViewById(R.id.buttonClearAll).setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear All Items");
            builder.setMessage("Are you sure you want to delete all items?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                databaseReference.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        groceryList.clear();
                        adapter.notifyDataSetChanged();


                        updateTotalItemsCounter();

                        Toast.makeText(MainActivity.this, "All items deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to delete items", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

    }

    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome!");
        builder.setMessage("Please enter your name:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String userName = input.getText().toString().trim();
            if (!userName.isEmpty()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_USER_NAME, userName);
                editor.apply();

                textViewWelcome.setText("Welcome, " + userName + "!");
                textViewMessage.setText("Add the items you need to the list:");
                          }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isManualAdd) {
                    isManualAdd = false;
                    return;
                }
                groceryList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GroceryItem item = dataSnapshot.getValue(GroceryItem.class);
                        if (item != null) {
                            groceryList.add(item);
                            Log.d("MainActivity", "Item loaded: " + item.getItemName());
                        } else {
                            Log.e("MainActivity", "Failed to parse item: " + dataSnapshot.toString());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "No data found in Firebase");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Failed to load data: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updateTotalItemsCounter() {
        textViewTotalItems.setText("Total Items: " + groceryList.size());
    }
}


