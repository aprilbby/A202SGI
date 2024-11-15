package student.inti.a202sgi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView displayNameTextView, displayEmailTextView, displayAgeTextView, displayGenderTextView, displayDescriptionTextView;
    private Button editProfileButton, resetPasswordButton, logoutButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        displayNameTextView = findViewById(R.id.displayNameTextView);
        displayEmailTextView = findViewById(R.id.displayEmailTextView);
        displayAgeTextView = findViewById(R.id.displayAgeTextView);
        displayGenderTextView = findViewById(R.id.displayGenderTextView);
        displayDescriptionTextView = findViewById(R.id.displayDescriptionTextView);

        editProfileButton = findViewById(R.id.editProfileButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        logoutButton = findViewById(R.id.logoutButton);

        loadUserProfile();

        editProfileButton.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));
        resetPasswordButton.setOnClickListener(view -> Toast.makeText(this, "Password reset feature unavailable with SQLite", Toast.LENGTH_SHORT).show());
        logoutButton.setOnClickListener(view -> {
            Toast.makeText(this, "Logout feature unavailable with SQLite", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void loadUserProfile() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "id = ?", new String[]{"1"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            displayNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            displayAgeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("age")));
            displayGenderTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            displayDescriptionTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            displayEmailTextView.setText("user@example.com"); // Placeholder email
            cursor.close();
        } else {
            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
        }
    }
}






