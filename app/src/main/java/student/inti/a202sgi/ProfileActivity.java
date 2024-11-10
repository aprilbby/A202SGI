package student.inti.a202sgi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private TextView displayNameTextView, displayEmailTextView, displayAgeTextView, displayGenderTextView, displayDescriptionTextView;
    private Button editProfileButton, resetPasswordButton, logoutButton;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        displayNameTextView = findViewById(R.id.displayNameTextView);
        displayEmailTextView = findViewById(R.id.displayEmailTextView);
        displayAgeTextView = findViewById(R.id.displayAgeTextView);
        displayGenderTextView = findViewById(R.id.displayGenderTextView);
        displayDescriptionTextView = findViewById(R.id.displayDescriptionTextView);

        editProfileButton = findViewById(R.id.editProfileButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        logoutButton = findViewById(R.id.logoutButton);

        displayEmailTextView.setText(currentUser.getEmail());

        loadUserProfile();

        editProfileButton.setOnClickListener(view -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        resetPasswordButton.setOnClickListener(view -> resetPassword());

        logoutButton.setOnClickListener(view -> {
            auth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadUserProfile() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    displayNameTextView.setText(name != null ? name : "N/A");
                    displayAgeTextView.setText(age != null ? age : "N/A");
                    displayGenderTextView.setText(gender != null ? gender : "N/A");
                    displayDescriptionTextView.setText(description != null ? description : "N/A");

                    Log.d(TAG, "Profile loaded successfully");
                } else {
                    Toast.makeText(ProfileActivity.this, "Profile data not found", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Profile data not found for user ID: " + currentUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading profile: " + error.getMessage());
            }
        });
    }

    private void resetPassword() {
        if (currentUser.getEmail() != null) {
            auth.sendPasswordResetEmail(currentUser.getEmail())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProfileActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Password reset email sent successfully");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error sending password reset email: " + e.getMessage());
                    });
        } else {
            Toast.makeText(this, "Email not available", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Email not available for password reset");
        }
    }
}


