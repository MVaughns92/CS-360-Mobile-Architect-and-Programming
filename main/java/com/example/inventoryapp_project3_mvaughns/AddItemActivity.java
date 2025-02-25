package com.example.inventoryapp_project3_mvaughns;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText editTextItemName, editTextItemQuantity, editTextItemThreshold;
    private Button buttonSaveItem;
    public DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        dbHelper = new DatabaseHelper(this);

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        editTextItemThreshold = findViewById(R.id.editTextItemThreshold);
        buttonSaveItem = findViewById(R.id.buttonSaveItem);

        // Create click listener to save items
        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextItemName.getText().toString();
                String quantityStr = editTextItemQuantity.getText().toString();
                String thresholdStr = editTextItemThreshold.getText().toString();

                // Check for empty fields
                if (name.isEmpty() || quantityStr.isEmpty() || thresholdStr.isEmpty()) {
                    Toast.makeText(AddItemActivity.this, "Please enter required information", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse quantity and threshold values
                int quantity = Integer.parseInt(quantityStr);
                int threshold = Integer.parseInt(thresholdStr);

                // Add item to database
                if (dbHelper.addItem(name, quantity, threshold)) {
                    Toast.makeText(AddItemActivity.this, "Item successfully added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // If item fails to be added
                    Toast.makeText(AddItemActivity.this, "Item was not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
