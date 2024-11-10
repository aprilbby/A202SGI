package student.inti.a202sgi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private TextView displayNameTextView, displayEmailTextView;
    private EditText editNameEditText, editSkillsEditText, editExperienceEditText, editEducationEditText;
    private Button editButton, saveButton, resetPasswordButton, logoutButton;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        displayEmailTextView = view.findViewById(R.id.displayEmailTextView);
        editNameEditText = view.findViewById(R.id.editNameEditText);
        editSkillsEditText = view.findViewById(R.id.editSkillsEditText);
        editExperienceEditText = view.findViewById(R.id.editExperienceEditText);
        editEducationEditText = view.findViewById(R.id.editEducationEditText);
        editButton = view.findViewById(R.id.editButton);
        saveButton = view.findViewById(R.id.saveButton);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        displayEmailTextView.setText(currentUser.getEmail());
        hideEditableFields();

        loadUserProfile();

        editButton.setOnClickListener(v -> showEditableFields());
        saveButton.setOnClickListener(v -> saveUserProfile());
        resetPasswordButton.setOnClickListener(v -> resetPassword());
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void hideEditableFields() {
        editNameEditText.setVisibility(View.GONE);
        editSkillsEditText.setVisibility(View.GONE);
        editExperienceEditText.setVisibility(View.GONE);
        editEducationEditText.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    private void showEditableFields() {
        editNameEditText.setVisibility(View.VISIBLE);
        editSkillsEditText.setVisibility(View.VISIBLE);
        editExperienceEditText.setVisibility(View.VISIBLE);
        editEducationEditText.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);

        // Set current text as hint for editing
        editNameEditText.setText(displayNameTextView.getText());

        editButton.setVisibility(View.GONE);
    }

    private void loadUserProfile() {
        dbRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String name = snapshot.child("name").getValue(String.class);
                String skills = snapshot.child("skills").getValue(String.class);
                String experience = snapshot.child("experience").getValue(String.class);
                String education = snapshot.child("education").getValue(String.class);

                displayNameTextView.setText(name != null ? name : "Full Name");
                editSkillsEditText.setText(skills != null ? skills : "");
                editExperienceEditText.setText(experience != null ? experience : "");
                editEducationEditText.setText(education != null ? education : "");
            }
        });
    }

    private void saveUserProfile() {
        String name = editNameEditText.getText().toString().trim();
        String skills = editSkillsEditText.getText().toString().trim();
        String experience = editExperienceEditText.getText().toString().trim();
        String education = editEducationEditText.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);
        profileData.put("skills", skills);
        profileData.put("experience", experience);
        profileData.put("education", education);

        dbRef.setValue(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    displayNameTextView.setText(name);
                    hideEditableFields();
                    editButton.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void resetPassword() {
        auth.sendPasswordResetEmail(currentUser.getEmail())
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to send reset email", Toast.LENGTH_SHORT).show()
                );
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

