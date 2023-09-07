package com.example.taskwave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDescriptionActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private Button saveButton;
    private String selectedItemText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);


        Intent intent = getIntent();
        if (intent != null) {
            selectedItemText = intent.getStringExtra("itemText");
        }
        sharedPreferences = getSharedPreferences("ItemDescriptions", MODE_PRIVATE);
        String savedDescription = sharedPreferences.getString(selectedItemText, "");
        descriptionEditText.setText(savedDescription);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(selectedItemText, description);
                editor.apply();
                finish();
            }
        });
    }

}
