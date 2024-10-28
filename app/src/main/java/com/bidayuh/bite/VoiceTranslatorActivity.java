package com.bidayuh.bite;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceTranslatorActivity extends AppCompatActivity {

    private ImageView micIV;
    private Spinner fromSpinner, toSpinner;
    private TextView selectedLanguagesTV; // TextView to show selected languages
    private static final int REQUEST_PERMISSION_CODE = 1;

    String[] fromlanguage = {"From", "English", "Malay", "Bidayuh"};
    String[] tolanguage = {"To", "English", "Malay", "Bidayuh"};

    private int fromLanguageCode, toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_translator);

        micIV = findViewById(R.id.idIVMic);
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        selectedLanguagesTV = findViewById(R.id.idSelectedLanguagesTV);

        // Set up the spinners for language selection
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fromlanguage);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tolanguage);
        toSpinner.setAdapter(toAdapter);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                fromLanguageCode = position; // Store the selected language code
                updateSelectedLanguages(); // Update the display
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                toLanguageCode = position; // Store the selected language code
                updateSelectedLanguages(); // Update the display
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something to translate");

                try {
                    startActivityForResult(intent, REQUEST_PERMISSION_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(VoiceTranslatorActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateSelectedLanguages() {
        String fromLanguage = fromlanguage[fromLanguageCode];
        String toLanguage = tolanguage[toLanguageCode];
        selectedLanguagesTV.setText(String.format("Translating from %s to %s", fromLanguage, toLanguage));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                // Handle the recognized speech result
                String spokenText = result.get(0);
                // TODO: Implement translation logic for voice input
                Toast.makeText(this, "Recognized: " + spokenText, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(VoiceTranslatorActivity.this, "No speech recognized. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(VoiceTranslatorActivity.this, "Speech input was canceled or failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
