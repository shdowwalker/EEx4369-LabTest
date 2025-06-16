package com.s23010285.sashika;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameInput, passwordInput;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        dbHelper = new AppDatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                long result = dbHelper.insertUser(username, password);
                if (result != -1) {
                    Toast.makeText(this, "Login info saved!", Toast.LENGTH_SHORT).show();
                    // Go to map view after saving
                    Intent intent = new Intent(MainActivity.this, intergrateGmap.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Error saving login info.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
