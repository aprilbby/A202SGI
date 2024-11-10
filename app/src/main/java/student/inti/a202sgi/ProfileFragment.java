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

    private TextView nameTextView, ageTextView, genderTextView, descriptionTextView, emailTextView;
    private Button editProfileButton, resetPasswordButton, logoutButton;
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

        // Initialize UI elements
        emailTextView = view.findViewById(R.id.emailTextView);
        nameTextView = view.findViewById(R.id.nameTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);

        editProfileButton = view.findViewById(R.id.editProfileButton);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        emailTextView.setText(currentUser.getEmail());

        loadUserProfile();

        // Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Reset password
        resetPasswordButton.setOnClickListener(v -> resetPassword());

        // Logout
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserProfile() {
        dbRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String name = snapshot.child("name").getValue(String.class);
                String age = snapshot.child("age").getValue(String.class);
                String gender = snapshot.child("gender").getValue(String.class);
                String description = snapshot.child("description").getValue(String.class);

                nameTextView.setText(name != null ? name : "Name");
                ageTextView.setText(age != null ? age : "Age");
                genderTextView.setText(gender != null ? gender : "Gender");
                descriptionTextView.setText(description != null ? description : "Description");
            }
        });
    }

    private void resetPassword() {
        auth.sendPasswordResetEmail(currentUser.getEmail())
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to send reset email", Toast.LENGTH_SHORT).show());
    }

    private void logout() {
        auth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}


