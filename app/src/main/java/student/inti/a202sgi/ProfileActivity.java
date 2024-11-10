package student.inti.a202sgi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private TextView displayNameTextView, displayEmailTextView;
    private EditText editNameEditText;
    private Button editButton, saveButton, resetPasswordButton;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Profile");
        }

        // Initialize Firebase and UI components
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");
        currentUser = auth.getCurrentUser();

        displayNameTextView = findViewById(R.id.displayNameTextView);
        displayEmailTextView = findViewById(R.id.displayEmailTextView);
        editNameEditText = findViewById(R.id.editNameEditText);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Show email and disable editing
        displayEmailTextView.setText(currentUser.getEmail());
        editNameEditText.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        // Load current profile data
        loadUserProfile();

        // Edit button functionality
        editButton.setOnClickListener(view -> {
            editNameEditText.setVisibility(View.VISIBLE);
            editNameEditText.setText(displayNameTextView.getText());
            saveButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
        });

        // Save button functionality
        saveButton.setOnClickListener(view -> saveUserProfile());

        // Reset password functionality
        resetPasswordButton.setOnClickListener(view -> resetPassword());
    }

    private void loadUserProfile() {
        dbRef.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String name = task.getResult().child("name").getValue(String.class);
                displayNameTextView.setText(name);
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = editNameEditText.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);

        dbRef.child(currentUser.getUid()).setValue(profileData)
                .addOnSuccessListener(aVoid -> {
                    // Show success message
                    Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    // Immediately update display and hide edit views
                    displayNameTextView.setText(name);
                    editNameEditText.setVisibility(View.GONE);
                    saveButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                );
    }

    private void resetPassword() {
        auth.sendPasswordResetEmail(currentUser.getEmail())
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(ProfileActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
