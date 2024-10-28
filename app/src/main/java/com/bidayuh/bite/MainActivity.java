package com.bidayuh.bite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MaterialButton textTranslatorBtn, voiceTranslatorBtn, logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        textTranslatorBtn = findViewById(R.id.idBtnTextTranslator);
        voiceTranslatorBtn = findViewById(R.id.idBtnVoiceTranslator);
        logoutBtn = findViewById(R.id.idBtnLogout); // Ensure you have this button in your layout

        // Set up logout button listener
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Launch text translator
        textTranslatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextTranslatorActivity.class);
                startActivity(intent);
            }
        });

        // Launch voice translator
        voiceTranslatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VoiceTranslatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
