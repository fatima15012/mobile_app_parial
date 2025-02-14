package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private List<GroceryItem> groceryList;
    private DatabaseReference databaseReference;
    private Context context;
    private MainActivity mainActivity;

    public GroceryAdapter(Context context, DatabaseReference databaseReference, List<GroceryItem> groceryList, MainActivity mainActivity) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.groceryList = groceryList;
        this.mainActivity = mainActivity;
    }

    public GroceryAdapter(List<GroceryItem> groceryList, DatabaseReference databaseReference, Context context) {
        this.groceryList = groceryList;
        this.databaseReference = databaseReference;
        this.context = context;
    }





    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        GroceryItem item = groceryList.get(position);
        holder.textViewItem.setText(item.getItemName());
        holder.textViewCategory.setText(item.getCategory());
        holder.checkBoxPurchased.setChecked(item.isPurchased());


        holder.checkBoxPurchased.setOnCheckedChangeListener((buttonView, isChecked) -> {
            databaseReference.child(item.getId()).child("purchased").setValue(isChecked);
            if (isChecked) {
                playPurchaseSound();
            }
        });


        holder.buttonDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure you want to delete this item?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                databaseReference.child(item.getId()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        groceryList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).updateTotalItemsCounter();
                        }

                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        holder.buttonUpdate.setOnClickListener(v -> {
            showUpdateDialog(item);
        });
    }
    private void showUpdateDialog(GroceryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Item");


        final EditText inputName = new EditText(context);
        inputName.setHint("Enter new item name");
        inputName.setText(item.getItemName());

        final Spinner inputCategory = new Spinner(context);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputCategory.setAdapter(adapter);
        inputCategory.setSelection(adapter.getPosition(item.getCategory()));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(inputCategory);
        builder.setView(layout);


        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString();
            String newCategory = inputCategory.getSelectedItem().toString();

            // Update the item in Firebase
            databaseReference.child(item.getId()).child("itemName").setValue(newName);
            databaseReference.child(item.getId()).child("category").setValue(newCategory);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    @Override
    public int getItemCount() {
        return groceryList.size();
    }



    private void playPurchaseSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.purchase_sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }

    public void setDropDownViewResource(int spinnerItem) {
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;
        TextView textViewCategory;
        CheckBox checkBoxPurchased;
        ImageButton buttonUpdate;
        ImageButton buttonDelete;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            checkBoxPurchased = itemView.findViewById(R.id.checkBoxPurchased);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}