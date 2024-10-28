package com.bidayuh.bite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Locale;

public class TextTranslatorActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceText;
    private MaterialButton translateBtn;
    private TextView translateIV;

    String[] fromlanguage = {"From", "English", "Malay", "Bidayuh"};
    String[] tolanguage = {"To", "English", "Malay", "Bidayuh"};
    private static final int REQUEST_PERMISSION_CODE = 1;

    int fromLanguageCode, toLanguageCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_translator); // Update with your XML layout for text translator

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceText = findViewById(R.id.idEditSource);
        translateBtn = findViewById(R.id.idBtnTranslation);
        translateIV = findViewById(R.id.idTranslatedTV);

        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromlanguage);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner_item, tolanguage);
        toSpinner.setAdapter(toAdapter);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromlanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(tolanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateIV.setVisibility(View.VISIBLE);
                translateIV.setText("");
                if (sourceText.getText().toString().isEmpty()) {
                    Toast.makeText(TextTranslatorActivity.this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode == 0) {
                    Toast.makeText(TextTranslatorActivity.this, "Please select Source Language", Toast.LENGTH_SHORT).show();
                } else if (toLanguageCode == 0) {
                    Toast.makeText(TextTranslatorActivity.this, "Please select language to make translation", Toast.LENGTH_SHORT).show();
                } else {
                    translateText(fromLanguageCode, toLanguageCode, sourceText.getText().toString());
                }
            }
        });
    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String source) {
        translateIV.setText("Downloading model, please wait...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translateIV.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translateIV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TextTranslatorActivity.this, "Failed to translate! Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TextTranslatorActivity.this, "Failed to download model! Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language) {
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;

            case "Malay":
                languageCode = FirebaseTranslateLanguage.MS;
                break;

            case "Bidayuh":
                languageCode = FirebaseTranslateLanguage.BE;
                break;

            default:
                languageCode = 0;
        }
        return languageCode;
    }
}
