package com.example.inventoryapp_project3_mvaughns;

import android.content.Context;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private DatabaseHelper dbHelper;
    private Context context;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.inventory, parent, false);
    }

    @Override
    // Bind data to view
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewItemName = view.findViewById(R.id.editTextItemName);
        TextView textViewItemQuantity = view.findViewById(R.id.editTextItemQuantity);
        ImageButton buttonDecrement = view.findViewById(R.id.decreaseItemButton);
        ImageButton buttonIncrement = view.findViewById(R.id.increaseItemButton);
        ImageButton buttonDeleteItem = view.findViewById(R.id.removeItemButton);

        final int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int itemQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        int itemThreshold = cursor.getInt(cursor.getColumnIndexOrThrow("threshold"));
        int notificationSent = cursor.getInt(cursor.getColumnIndexOrThrow("notification_sent"));

        textViewItemName.setText(itemName);
        textViewItemQuantity.setText(String.valueOf(itemQuantity));

        // Create click listener for decrease button is clicked
        buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemQuantity > 0) {
                    int newQuantity = itemQuantity - 1;
                    dbHelper.updateItemQuantity(itemId, newQuantity);
                    notifyIfThresholdCrossed(itemId, itemName, newQuantity, itemThreshold, notificationSent);
                    Cursor newCursor = dbHelper.getAllItems();
                    swapCursor(newCursor);
                }
            }
        });

        // Create click listener for increase button is clicked
        buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = itemQuantity + 1;
                dbHelper.updateItemQuantity(itemId, newQuantity);
                notifyIfThresholdCrossed(itemId, itemName, newQuantity, itemThreshold, notificationSent);
                Cursor newCursor = dbHelper.getAllItems();
                swapCursor(newCursor);
            }
        });

        // Create click listener for delete button is clicked
        buttonDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteItem(itemId);
                Cursor newCursor = dbHelper.getAllItems();
                swapCursor(newCursor);
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Create function to alert user if item out of stock
    private void notifyIfThresholdCrossed(int itemId, String itemName, int newQuantity, int threshold, int notificationSent) {
        if (newQuantity <= threshold && notificationSent == 0) {
            // If quantity is at or out of stock but no notice sent
            sendSMSNotification(itemName + " is out of stock!");
            dbHelper.updateNotificationSent(itemId, 1);
        }
        else if (newQuantity > threshold && notificationSent == 1) {
            // If quantity is in stock and notice has been sent
            sendSMSNotification(itemName + " is in stock!");
            dbHelper.updateNotificationSent(itemId, 0);
        }
    }

    private void sendSMSNotification(String message) {
        // Set up SMS notifications to phone
        String phoneNumber = "9184066456";
        try {
            // Send SMS message
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
        }
        // Handle exceptions
        catch (Exception e) {
            Toast.makeText(context, "Message send failure", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
