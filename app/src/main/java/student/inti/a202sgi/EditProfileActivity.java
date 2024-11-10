package student.inti.a202sgi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameEditText, ageEditText, genderEditText, descriptionEditText;
    private Button saveChangesButton;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        loadUserProfile();

        saveChangesButton.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        dbRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                nameEditText.setText(snapshot.child("name").getValue(String.class));
                ageEditText.setText(snapshot.child("age").getValue(String.class));
                genderEditText.setText(snapshot.child("gender").getValue(String.class));
                descriptionEditText.setText(snapshot.child("description").getValue(String.class));
            }
        });
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);
        profileData.put("age", age);
        profileData.put("gender", gender);
        profileData.put("description", description);

        dbRef.updateChildren(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show());
    }
}



