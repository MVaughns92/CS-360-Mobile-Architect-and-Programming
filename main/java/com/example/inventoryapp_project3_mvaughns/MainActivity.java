package com.example.inventoryapp_project3_mvaughns;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridView gridViewInventory;
    private Button buttonAddItem, buttonSMSNotification;
    private DatabaseHelper dbHelper;
    private CustomCursorAdapter adapter;
    private TextView textViewItemName, textViewItemQuantity;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        dbHelper = new DatabaseHelper(this);

        gridViewInventory = findViewById(R.id.gridViewInventory);
        buttonAddItem = findViewById(R.id.addItemButton);
        buttonSMSNotification = findViewById(R.id.buttonSMSNotification);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddItemActivity.class));
            }
        });

        buttonSMSNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SMSActivity.class));
            }
        });
        // Load inventory data from database when activity is created
        loadInventoryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInventoryData();
    }
    // Load inventory data from database into GridView
    private void loadInventoryData() {
        Cursor cursor = dbHelper.getAllItems();
        String[] from = { "name", "quantity" };
        int[] to = {R.id.textViewItemName, R.id.textViewItemQuantity};

        adapter = new CustomCursorAdapter(this, R.layout.inventory, cursor, from, to, 0);
        gridViewInventory.setAdapter(adapter);
    }
}