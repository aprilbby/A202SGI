// ProfileFragment.java
package student.inti.a202sgi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private TextView displayName, displayEmail, displayAge, displayGender, displayDescription;
    private Button editProfileButton, resetPasswordButton, logoutButton;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        // Bind views
        displayName = view.findViewById(R.id.displayName);
        displayEmail = view.findViewById(R.id.displayEmail);
        displayAge = view.findViewById(R.id.displayAge);
        displayGender = view.findViewById(R.id.displayGender);
        displayDescription = view.findViewById(R.id.displayDescription);

        editProfileButton = view.findViewById(R.id.editProfileButton);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Load Profile Data
        loadUserProfile();

        // Edit Profile action
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Reset Password action
        resetPasswordButton.setOnClickListener(v -> resetPassword());

        // Logout action
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserProfile() {
        dbRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                displayName.setText(snapshot.child("name").getValue(String.class));
                displayEmail.setText(currentUser.getEmail());
                displayAge.setText(snapshot.child("age").getValue(String.class));
                displayGender.setText(snapshot.child("gender").getValue(String.class));
                displayDescription.setText(snapshot.child("description").getValue(String.class));
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
        );
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
        auth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

