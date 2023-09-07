package com.example.taskwave;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;
    private TextView greetingTextView;
    private EditText inputText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);
        greetingTextView = findViewById(R.id.greetingTextView);
        inputText = findViewById(R.id.editTextText);


        sharedPreferences = getSharedPreferences("MyItems", MODE_PRIVATE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        items = new ArrayList<>();
        Set<String> savedItems = sharedPreferences.getStringSet("items", new HashSet<String>());
        items.addAll(savedItems);

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();

        setGreetingMessage();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = items.get(position);
                Intent intent = new Intent(MainActivity.this, ItemDescriptionActivity.class);
                intent.putExtra("itemText", selectedItemText);
                startActivity(intent);
            }
        });
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                        items.remove(i);
                        itemsAdapter.notifyDataSetChanged();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet("items", new HashSet<>(items));
                        editor.apply();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }


    private void addItem() {
        EditText input = findViewById(R.id.editTextText);
        String itemText = input.getText().toString();

        if (!itemText.isEmpty()) {
            itemsAdapter.add(itemText);
            input.setText("");
            input.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("items", new HashSet<>(items));
            editor.apply();
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Text..", Toast.LENGTH_SHORT).show();
        }
    }

    private void setGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour >= 0 && hour < 12) {
            greeting = "Good morning..!!";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good afternoon..!!";
        } else {
            greeting = "Good evening..!!";
        }

        greetingTextView.setText(greeting);
    }
}
